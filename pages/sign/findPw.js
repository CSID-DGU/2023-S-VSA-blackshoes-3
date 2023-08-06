/* eslint-disable react-native/no-inline-styles */
import React, {useEffect, useState} from 'react';
import {
  TouchableOpacity,
  StyleSheet,
  TextInput,
  View,
  Text,
  Modal,
  FlatList,
} from 'react-native';
export default function FindPw({navigation}) {
  const [emailInput, setEmailInput] = useState('');
  const [passwordInput, setPasswordInput] = useState('');
  const [emailConfirm, setEmailConfirm] = useState('');
  const [passwordConfirmInput, setPasswordConfirmInput] = useState('');

  const [value, setValue] = useState('naver.com');
  const [modal, setModal] = useState(false);
  const [items, setItems] = useState([
    {label: '직접입력', value: '직접입력'},
    {label: 'naver.com', value: 'naver.com'},
    {label: 'daum.net', value: 'daum.net'},
    {label: 'gmail.com', value: 'gmail.com'},
    {label: 'nate.com', value: 'nate.com'},
    {label: 'hanmail.net', value: 'hanmail.net'},
    {label: 'hotmail.com', value: 'hotmail.com'},
    {label: 'yahoo.com', value: 'yahoo.com'},
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

  return (
    <View style={styles.container}>
      <Text style={styles.title}>비밀번호 찾기</Text>
      <View style={styles.emailContainer}>
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
              onChangeText={text => setValue(text)}
              value={value}
            />
          ) : (
            <TouchableOpacity onPress={() => openModal(0)} style={styles.email}>
              <Text style={styles.birthText}>{value}</Text>
            </TouchableOpacity>
          )}
        </View>
        <View style={styles.rowContainer}>
          <TextInput
            style={styles.textInput}
            placeholder="인증번호를 입력해 주세요"
            placeholderTextColor={'#C4C4C4'}
            onChangeText={text => setEmailConfirm(text)}
            value={emailConfirm}
          />
          <TouchableOpacity style={styles.button}>
            <Text style={styles.buttonText}>인증번호 발송</Text>
          </TouchableOpacity>
        </View>
      </View>
      <View style={styles.normalContainer}>
        <Text style={styles.text}>새 비밀번호</Text>
        <TextInput
          style={styles.longTextInput}
          placeholder="비밀번호를 입력해 주세요"
          placeholderTextColor={'#C4C4C4'}
          onChangeText={text => setPasswordInput(text)}
          value={passwordInput}
        />
      </View>
      <View style={styles.normalContainer}>
        <Text style={styles.text}>새 비밀번호 확인</Text>
        <TextInput
          style={styles.longTextInput}
          placeholder="비밀번호를 확인해 주세요"
          placeholderTextColor={'#C4C4C4'}
          onChangeText={text => setPasswordConfirmInput(text)}
          value={passwordConfirmInput}
        />
      </View>
      <TouchableOpacity style={styles.signUp}>
        <Text style={styles.signUpText}>비밀번호 변경</Text>
      </TouchableOpacity>
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
    backgroundColor: '#F2F8FF',
    paddingHorizontal: 20,
    paddingVertical: 5,
    gap: 10,
  },
  text: {
    fontSize: 20,
    fontWeight: '400',
  },
  title: {
    fontSize: 25,
    color: '#525252',
    fontWeight: 'bold',
    marginTop: 15,
    marginBottom: 7,
  },
  emailContainer: {
    width: '100%',
    paddingVertical: 10,
  },
  rowContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    width: '100%',
    paddingTop: 10,
  },
  normalContainer: {
    gap: 10,
    marginBottom: 10,
  },
  email: {
    justifyContent: 'center',
    paddingLeft: 15,
    width: 123,
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
    width: 190,
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

  button: {
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#1DAE86',
    borderRadius: 17,
    width: 123,
    height: 54,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 3,
  },
  buttonText: {
    color: 'white',
    fontSize: 16,
    // fontFamily: 'AppleSDGothicNeoB00',
  },
  longTextContainer: {
    marginTop: 2,
    backgroundColor: '#E8E9EB',
    padding: 10,
    borderRadius: 10,
    height: 124,
    width: '100%',
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 3,
  },
  longText: {
    fontSize: 14,
    lineHeight: 20,
  },
  checkContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'flex-end',
    gap: 5,
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
