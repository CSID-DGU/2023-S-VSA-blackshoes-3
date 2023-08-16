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
  Alert,
  ActivityIndicator as Spinner,
} from 'react-native';
import axios from 'axios';
import {SERVER_IP} from '../../config';

export default function FindPw({navigation}) {
  const [emailInput, setEmailInput] = useState('');
  const [passwordInput, setPasswordInput] = useState('');
  const [emailConfirm, setEmailConfirm] = useState('');
  const [passwordConfirmInput, setPasswordConfirmInput] = useState('');
  const [confirmIndex, setConfirmIndex] = useState(0);

  const [value, setValue] = useState('naver.com');
  const [modal, setModal] = useState(false);
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
  const confirmEmail = async () => {
    const fullEmail = emailInput + '@' + value;
    console.log(fullEmail);

    if (confirmIndex === 0) {
      if (!emailInput) {
        Alert.alert(
          '이메일 입력',
          '이메일을 입력해주세요.',
          [{text: 'OK', onPress: () => console.log('OK Pressed')}],
          {cancelable: false},
        );
        return;
      } else {
        setConfirmIndex(1);
        try {
          const response = await axios.post(
            `${SERVER_IP}user-service/mail/send-verification-code`,
            {
              email: fullEmail,
            },
            {
              headers: {
                'Content-Type': 'application/json',
              },
            },
          );
          console.log('Response:', response.data);
        } catch (error) {
          if (error.response) {
            console.error('Error:', error.response.data.error);
          } else {
            console.error('Error:', error.message);
          }
        }

        Alert.alert(
          '이메일 인증',
          '인증번호가 발송되었습니다.',
          [{text: 'OK', onPress: () => console.log('OK Pressed')}],
          {cancelable: false},
        );
        setConfirmIndex(2);

        return;
      }
    } else {
      try {
        console.log('emailConfirm', emailConfirm);
        console.log('fullEmail', fullEmail);
        const response = await axios.post(
          `${SERVER_IP}user-service/mail/verify-code`,
          {
            email: fullEmail,
            verificationCode: emailConfirm,
          },
          {
            headers: {
              'Content-Type': 'application/json',
            },
          },
        );

        console.log('Response:', response.data);
      } catch (error) {
        if (error.response) {
          console.error('Error:', error.response.data.error);
        } else {
          console.error('Error:', error.message);
        }
      }
      setConfirmIndex(3);
    }
  };

  const handleSignUp = () => {
    if (confirmIndex !== 3) {
      Alert.alert(
        '이메일 인증',
        '이메일 인증을 완료해주세요.',
        [{text: 'OK', onPress: () => console.log('OK Pressed')}],
        {cancelable: false},
      );
      return;
    }

    if (
      !emailInput ||
      !passwordInput ||
      !passwordConfirmInput ||
      !emailConfirm
    ) {
      Alert.alert(
        '회원가입 실패',
        '모든 항목을 입력해주세요.',
        [{text: 'OK', onPress: () => console.log('OK Pressed')}],
        {cancelable: false},
      );
      return;
    } else if (passwordInput !== passwordConfirmInput) {
      Alert.alert(
        '회원가입 실패',
        '비밀번호가 일치하지 않습니다.',
        [{text: 'OK', onPress: () => console.log('OK Pressed')}],
        {cancelable: false},
      );
      return;
    } else if (passwordInput.length < 8) {
      Alert.alert(
        '회원가입 실패',
        '비밀번호는 8자리 이상이어야 합니다.',
        [{text: 'OK', onPress: () => console.log('OK Pressed')}],
        {cancelable: false},
      );
      return;
    } else {
      submitData();
    }
  };

  const submitData = async () => {
    const fullEmail = emailInput + '@' + value;
    console.log(fullEmail);
    console.log(passwordInput);
    try {
      const response = await axios.put(
        `${SERVER_IP}user-service/users/password`,
        {
          email: fullEmail,
          password: passwordInput,
        },
      );
      console.log('Response:', response.data);
      navigation.navigate('Login');
    } catch (error) {
      console.log(error);
    }
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
          <TouchableOpacity
            style={[
              styles.button,
              {
                backgroundColor:
                  confirmIndex === 1 || confirmIndex === 3
                    ? '#E6E6E6'
                    : '#1DAE86',
              },
            ]}
            disabled={confirmIndex === 1}
            onPress={confirmEmail}>
            {confirmIndex === 0 ? (
              <Text style={styles.buttonText}>인증번호 발송</Text>
            ) : confirmIndex === 1 ? (
              <Spinner size="small" color="white" />
            ) : confirmIndex === 2 ? (
              <Text style={styles.buttonText}>확인</Text>
            ) : (
              <Text style={styles.buttonText}>인증완료</Text>
            )}
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
      <TouchableOpacity style={styles.signUp} onPress={handleSignUp}>
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
  birthText: {
    fontSize: 16,
    color: 'black',
  },
});
