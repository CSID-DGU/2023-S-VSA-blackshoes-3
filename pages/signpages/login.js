/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable react-native/no-inline-styles */
import React, {useEffect, useState} from 'react';
import {
  TouchableOpacity,
  StyleSheet,
  View,
  TextInput,
  Text,
  Modal,
  FlatList,
} from 'react-native';
import CheckBox from '@react-native-community/checkbox';
import AsyncStorage from '@react-native-async-storage/async-storage';
import {useDispatch, useSelector} from 'react-redux';
import {setAll} from '../../storage/actions';
import axios from 'axios';
import {SERVER_IP} from '../../config';

export default function Login({navigation}) {
  const dispatch = useDispatch();
  const [emailInput, setEmailInput] = useState('');
  const [passwordInput, setPasswordInput] = useState('');
  const [value, setValue] = useState('naver.com');
  const [modal, setModal] = useState(false);
  const [toggleCheckBox, setToggleCheckBox] = useState(false);
  const confirmFirst = useSelector(state => state.FIRST_EMAIL);

  const [items, setItems] = useState([
    {label: '직접입력', value: '직접입력'},
    {label: 'naver.com', value: 'naver.com'},
    {label: 'daum.net', value: 'daum.net'},
    {label: 'gmail.com', value: 'gmail.com'},
  ]);

  const openModal = () => {
    setModal(true);
  };
  const closeModal = () => {
    setModal(false);
  };
  const handleSelect = e => {
    setValue(e);
    closeModal();
  };

  const submitData = async () => {
    const fullEmail = emailInput + '@' + value;
    const registerData = {
      email: fullEmail,
      password: passwordInput,
    };

    try {
      const userResponse = await axios.post(
        `${SERVER_IP}user-service/users/login`,
        registerData,
        {
          headers: {
            'Content-Type': 'application/json',
          },
        },
      );

      console.log('Response:', userResponse.data.payload.userId);
      if (toggleCheckBox) {
        AsyncStorage.setItem('email', fullEmail);
        AsyncStorage.setItem('pass', passwordInput);
      }
      dispatch(setAll(userResponse.data.payload));
      if (confirmFirst === emailInput) {
        navigation.navigate('ThemeSelect');
      } else {
        // try {
        //   console.log('hi', userResponse.data.payload.userId);
        //   const tagResponse = await axios.get(
        //     `${SERVER_IP}:8051/personalized-service/${userResponse.data.payload.userId}/tags/subscribed`,
        //   );

        //   console.log('hi tagIds', tagResponse.data.payload.subscribedTagList);
        //   dispatch(setTag(tagResponse.data.payload.subscribedTagList));
        // } catch (e) {
        //   console.log(e);
        // }

        navigation.navigate('Home');
      }
    } catch (error) {
      if (error.response) {
        console.error('Error:', error.response.data.error);
      } else {
        console.error('Error1:', error.message);
      }
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>로그인</Text>
      <View style={styles.contentsContainer}>
        <Text style={styles.text}>Email</Text>
        <View style={styles.rowContainer}>
          <TextInput
            style={styles.textInput}
            placeholder="이메일을 입력해 주세요"
            placeholderTextColor={'#C4C4C4'}
            onChangeText={text => setEmailInput(text)}
            value={emailInput}
          />
          <Text>@</Text>
          {value === '직접입력' ? (
            <TextInput
              style={[styles.emailInput, {paddingLeft: 15}]}
              placeholder="직접입력"
              placeholderTextColor={'#C4C4C4'}
              placeholderTextSize={10}
            />
          ) : (
            <TouchableOpacity onPress={openModal} style={styles.email}>
              <Text style={styles.birthText}>{value}</Text>
            </TouchableOpacity>
          )}
        </View>
        <View style={styles.normalContainer}>
          <Text style={styles.text}>비밀번호</Text>
          <TextInput
            style={styles.longTextInput}
            placeholder="비밀번호를 입력해 주세요"
            placeholderTextColor={'#C4C4C4'}
            onChangeText={text => setPasswordInput(text)}
            value={passwordInput}
          />
        </View>
        <View style={styles.asyncContainer}>
          <Text style={styles.asyncText}>자동로그인</Text>
          <CheckBox
            disabled={false}
            value={toggleCheckBox}
            onValueChange={newValue => setToggleCheckBox(newValue)}
          />
        </View>
        <TouchableOpacity style={styles.signUp} onPress={submitData}>
          <Text style={styles.signUpText}>로그인</Text>
        </TouchableOpacity>
      </View>
      <View style={styles.linkContainer}>
        <TouchableOpacity onPress={() => navigation.navigate('SignUp')}>
          <Text style={styles.greyText}>회원가입</Text>
        </TouchableOpacity>
        <Text>|</Text>
        <TouchableOpacity onPress={() => navigation.navigate('FindPw')}>
          <Text style={styles.greyText}>비밀번호 찾기</Text>
        </TouchableOpacity>
      </View>

      <Modal
        animationType="slide"
        transparent={true}
        visible={modal}
        onRequestClose={closeModal}>
        <View style={styles.modalContainer}>
          <View style={styles.modalInContainer}>
            <FlatList
              contentContainerStyle={{
                justifyContent: 'space-between',
                paddingVertical: 15,
              }}
              data={items}
              keyExtractor={item => item.value}
              renderItem={({item}) => (
                <TouchableOpacity
                  onPress={() => handleSelect(item.value)}
                  style={styles.modalContents}>
                  <Text style={styles.modalText}>{item.label}</Text>
                </TouchableOpacity>
              )}
            />
          </View>
        </View>
      </Modal>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    paddingHorizontal: 10,
    paddingVertical: 20,
    gap: 10,
  },
  contentsContainer: {
    paddingTop: 25,
    backgroundColor: '#F2F8FF',
    paddingHorizontal: 15,
    borderRadius: 15,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 3,
  },
  linkContainer: {
    marginTop: 20,
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    width: '100%',
    gap: 10,
  },
  asyncContainer: {
    flexDirection: 'row',
    width: '100%',
    justifyContent: 'flex-end',
    alignItems: 'center',
    marginTop: 10,
    gap: 3,
  },
  title: {
    marginLeft: 10,
    fontSize: 25,
    color: '#525252',
    fontWeight: 'bold',
    marginTop: 15,
    marginBottom: 13,
  },
  text: {
    fontSize: 20,
    fontWeight: '400',
  },
  greyText: {
    color: '#BDBDBD',
    fontSize: 16,
  },
  rowContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    width: '100%',
    paddingTop: 10,
    marginBottom: 20,
  },
  normalContainer: {
    gap: 10,
    marginBottom: 10,
  },
  asyncText: {
    color: '#9D9D9D',
  },
  birthText: {
    fontSize: 16,
    color: 'black',
  },
  email: {
    justifyContent: 'center',
    paddingLeft: 15,
    width: 120,
    height: 54,
    backgroundColor: '#F7FFF5',
    borderRadius: 17,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 3,
  },

  textInput: {
    fontSize: 15,
    color: 'black',
    paddingLeft: 15,
    width: 185,
    height: 54,
    backgroundColor: 'white',
    borderRadius: 10,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 3,
  },

  longTextInput: {
    fontSize: 15,
    color: 'black',
    paddingHorizontal: 15,
    width: '100%',
    height: 54,
    backgroundColor: 'white',
    borderRadius: 10,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 3,
  },

  emailInput: {
    width: 123,
    height: 54,
    backgroundColor: 'white',
    borderRadius: 10,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 3,
  },
  modalContainer: {
    backgroundColor: 'rgba(0,0,0,0.5)',
    justifyContent: 'center',
    alignItems: 'center',
    height: '100%',
  },
  modalInContainer: {
    height: '80%',
    borderRadius: 10,
    overflow: 'hidden',
    backgroundColor: 'white',
  },
  modalContents: {
    backgroundColor: 'white',
    width: 300,
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: 15,
  },
  modalText: {
    textAlign: 'center',
    fontSize: 20,
  },
  signUp: {
    borderRadius: 10,
    height: 54,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#1DAE86',
    marginTop: 20,
    marginBottom: 30,
  },
  signUpText: {
    color: 'white',
    fontSize: 16,
  },
});
