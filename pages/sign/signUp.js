/* eslint-disable react-native/no-inline-styles */
import React, {useEffect, useState} from 'react';
import {
  TouchableOpacity,
  StyleSheet,
  Alert,
  View,
  TextInput,
  Text,
  Modal,
  ScrollView,
  FlatList,
} from 'react-native';
import CheckBox from '@react-native-community/checkbox';
import {KeyboardAwareScrollView} from 'react-native-keyboard-aware-scroll-view';

export default function SignUp({navigation}) {
  const [value, setValue] = useState('naver.com');
  const email = [
    {label: '직접입력', value: '직접입력'},
    {label: 'naver.com', value: 'naver.com'},
    {label: 'gmail.com', value: 'gmail.com'},
    {label: 'daum.com', value: 'daum.net'},
    {label: 'nate.com', value: 'nate.com'},
    {label: 'hanmail.net', value: 'hanmail.net'},
    {label: 'hotmail.com', value: 'hotmail.com'},
    {label: 'yahoo.com', value: 'yahoo.com'},
  ];
  const [toggleCheckBox, setToggleCheckBox] = useState(false);
  const [emailInput, setEmailInput] = useState('');
  const [passwordInput, setPasswordInput] = useState('');
  const [passwordConfirmInput, setPasswordConfirmInput] = useState('');
  const [nameInput, setNameInput] = useState('');
  const [emailConfirm, setEmailConfirm] = useState('');
  const [year, setYear] = useState('2000');
  const [month, setMonth] = useState('1');
  const [day, setDay] = useState('1');
  const [birthDate, setBirthDate] = useState(null);

  const years = Array.from({length: 100}, (_, i) => ({
    label: `${2023 - i}`,
    value: `${2023 - i}`,
  }));
  const months = Array.from({length: 12}, (_, i) => ({
    label: `${i + 1}`,
    value: `${i + 1}`,
  }));
  const days = Array.from({length: 31}, (_, i) => ({
    label: `${i + 1}`,
    value: `${i + 1}`,
  }));

  useEffect(() => {
    if (year && month && day) {
      setBirthDate(`${year}-${month}-${day}`);
    }
  }, [year, month, day]);

  const [modalVisible, setModalVisible] = useState(false);
  const [selectModal, setSelectModal] = useState(0);

  const openModal = selected => {
    setModalVisible(true);
    setSelectModal(selected);
  };

  const closeModal = () => {
    setModalVisible(false);
  };

  const handleSelect = e => {
    if (selectModal === 0) {
      setValue(e);
    } else if (selectModal === 1) {
      setYear(e);
    } else if (selectModal === 2) {
      setMonth(e);
    } else {
      setDay(e);
    }
    closeModal();
  };
  const handleSignUp = () => {
    //인증번호 axios로 보내서 확인 받기
    if (
      !emailInput ||
      !passwordInput ||
      !passwordConfirmInput ||
      !nameInput ||
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
    } else if (!toggleCheckBox) {
      Alert.alert(
        '약관 동의 필요',
        '서비스 이용을 위해 약관에 동의해주세요.',
        [{text: 'OK', onPress: () => console.log('OK Pressed')}],
        {cancelable: false},
      );
      return;
    } else {
      //회원가입 정보 보내기
      navigation.navigate('Login');
    }
  };

  return (
    <KeyboardAwareScrollView resetScrollToCoords={{x: 0, y: 0}}>
      <View style={styles.container}>
        <Text style={styles.title}>회원가입</Text>
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
              <TouchableOpacity
                onPress={() => openModal(0)}
                style={styles.email}>
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
          <Text style={styles.text}>비밀번호</Text>
          <TextInput
            style={styles.longTextInput}
            placeholder="비밀번호를 입력해 주세요"
            placeholderTextColor={'#C4C4C4'}
            onChangeText={text => setPasswordInput(text)}
            value={passwordInput}
          />
        </View>
        <View style={styles.normalContainer}>
          <Text style={styles.text}>비밀번호 확인</Text>
          <TextInput
            style={styles.longTextInput}
            placeholder="비밀번호를 확인해 주세요"
            placeholderTextColor={'#C4C4C4'}
            onChangeText={text => setPasswordConfirmInput(text)}
            value={passwordConfirmInput}
          />
        </View>
        <View style={styles.normalContainer}>
          <Text style={styles.text}>이름</Text>
          <TextInput
            style={styles.longTextInput}
            placeholder="이름을 입력해 주세요"
            placeholderTextColor={'#C4C4C4'}
            onChangeText={text => setNameInput(text)}
            value={nameInput}
          />
        </View>
        <View style={styles.birthContainer}>
          <Text style={styles.text}>생년월일</Text>
          <View style={styles.rowContainer}>
            <TouchableOpacity
              onPress={() => openModal(1)}
              style={styles.dropdown}>
              <Text style={styles.birthText}>{year} 년</Text>
            </TouchableOpacity>

            <TouchableOpacity
              onPress={() => openModal(2)}
              style={styles.dropdown}>
              <Text style={styles.birthText}>{month} 월</Text>
            </TouchableOpacity>

            <TouchableOpacity
              onPress={() => openModal(3)}
              style={styles.dropdown}>
              <Text style={styles.birthText}>{day} 일</Text>
            </TouchableOpacity>
          </View>
        </View>
        <View style={styles.normalContainer}>
          <Text style={styles.text}>개인정보 약관</Text>
          <ScrollView style={styles.longTextContainer}>
            <Text style={styles.longText}>
              약관내용입니다. 제 1조 n항 본 약관에 회사는 'Wander'를 말하며
              사용자는 '본인'을 가르킨다. 회사의 어쩌고저쩌고 아래로 스크롤이
              가능합니다.
              내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용
            </Text>
          </ScrollView>
        </View>
        <View style={styles.checkContainer}>
          <CheckBox
            disabled={false}
            value={toggleCheckBox}
            onValueChange={newValue => setToggleCheckBox(newValue)}
          />
          <Text style={styles.checkText}>동의합니다.</Text>
        </View>
        <TouchableOpacity style={styles.signUp} onPress={handleSignUp}>
          <Text style={styles.signUpText}>회원가입</Text>
        </TouchableOpacity>
      </View>
      <Modal
        animationType="slide"
        transparent={true}
        visible={modalVisible}
        onRequestClose={closeModal}>
        <View style={styles.modalContainer}>
          <View style={styles.modalInContainer}>
            <FlatList
              contentContainerStyle={{
                justifyContent: 'space-between',
                paddingVertical: 15,
              }}
              data={
                selectModal === 0
                  ? email
                  : selectModal === 1
                  ? years
                  : selectModal === 2
                  ? months
                  : days
              }
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
    </KeyboardAwareScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    backgroundColor: '#F2F8FF',
    paddingHorizontal: 20,
    paddingVertical: 5,
    gap: 10,
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
  birthContainer: {
    gap: 3,
  },

  dropdown: {
    justifyContent: 'center',
    alignItems: 'center',
    width: 102,
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
    elevation: 1.5,
    marginBottom: 12,
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

  title: {
    fontSize: 25,
    color: '#525252',
    fontWeight: 'bold',
    marginTop: 15,
  },
  text: {
    fontSize: 20,
    fontWeight: '400',
  },
  birthText: {
    fontSize: 16,
    color: 'black',
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
  checkText: {
    color: '#9D9D9D',
  },
  signUp: {
    borderRadius: 10,
    height: 54,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#1DAE86',
    marginBottom: 40,
  },
  signUpText: {
    color: 'white',
    fontSize: 16,
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
});
