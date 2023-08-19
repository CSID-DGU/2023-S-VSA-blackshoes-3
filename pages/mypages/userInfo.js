/* eslint-disable radix */
/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable react-native/no-inline-styles */
import React, {useEffect, useState} from 'react';
import {
  TouchableOpacity,
  StyleSheet,
  View,
  TextInput,
  Text,
  Alert,
  ScrollView,
} from 'react-native';
import {SERVER_IP} from '../../config';
import {useDispatch, useSelector} from 'react-redux';
import {deleteUserId} from '../../storage/actions';
import AsyncStorage from '@react-native-async-storage/async-storage';
import axiosInstance from '../../utils/axiosInstance';

export default function CheckInfo({navigation}) {
  ///user-service/users/join
  const dispatch = useDispatch();
  const userId = useSelector(state => state.USER);
  const [nameInput, setNameInput] = useState('');
  const [passInput, setPassInput] = useState('');
  const [oldPasswordInput, setOldPasswordInput] = useState('');
  const [newPasswordInput, setNewPasswordInput] = useState('');
  const [disable, setDisable] = useState(false);

  const [userData, setUserData] = useState({});

  function formatDate(input) {
    const parts = input.split('-');
    if (parts.length !== 3) {
      return input;
    }
    const [year, month, day] = parts;
    return (
      <View style={styles.birthContainer}>
        <Text style={styles.birthText}>{year}년</Text>
        <Text style={styles.birthText}>{parseInt(month)}월</Text>
        <Text style={styles.birthText}> {parseInt(day)}일</Text>
      </View>
    );
  }

  const getUserData = async () => {
    try {
      const response = await axiosInstance.get(`user-service/users/${userId}`);
      console.log('회원정보 조회 : ', response.data.payload);
      setUserData(response.data.payload);
    } catch (e) {
      console.error(e);
    }
  };

  useEffect(() => {
    getUserData();
  }, []);
  console.log(userData);
  const modifyName = async () => {
    try {
      const response = await axiosInstance.put(`user-service/users/${userId}`, {
        nickname: nameInput,
      });
      console.log(response.data);
      Alert.alert(
        '이름 변경 완료',
        '이름이 변경되었습니다.',
        [{text: 'OK', onPress: () => console.log('OK Pressed')}],
        {cancelable: false},
      );
      setDisable(true);
    } catch (e) {
      console.error(e);
    }
  };

  const modifyPassword = async () => {
    try {
      const response = await axiosInstance.put(
        `user-service/users/${userId}/password`,
        {
          oldPassword: oldPasswordInput,
          newPassword: newPasswordInput,
        },
      );
      console.log(response.data);
      Alert.alert(
        '비밀번호 변경 완료',
        '비밀번호가 변경되었습니다.',
        [{text: 'OK', onPress: () => console.log('OK Pressed')}],
        {cancelable: false},
      );
    } catch (e) {
      console.error(e);
    }
  };

  const signOut = async () => {
    try {
      // const pass = await AsyncStorage.getItem('pass');
      await axiosInstance.post(
        `${SERVER_IP}user-service/users/${userId}/withdrawal`,
        {
          password: passInput,
        },
      );
      await AsyncStorage.removeItem('email');
      await AsyncStorage.removeItem('pass');
      dispatch(deleteUserId());
      navigation.navigate('SignIn');
    } catch (e) {
      console.error(e);
    }
  };

  return (
    <View style={styles.container}>
      <ScrollView contentContainerStyle={styles.scrollContainer}>
        <Text style={styles.title}>회원 정보 조회</Text>
        <View style={styles.contentsContainer}>
          <Text style={styles.text}>Email</Text>
          <Text style={styles.infoText}>{userData.email}</Text>
          <Text style={styles.text}>이름</Text>
          <View style={styles.rowContainer}>
            <TextInput
              style={[styles.infoText, {width: 212}]}
              placeholder={userData.nickname}
              placeholderTextColor={'grey'}
              onChangeText={text => setNameInput(text)}
              value={nameInput}
            />

            <TouchableOpacity
              style={styles.modifyButton}
              onPress={modifyName}
              disabled={disable}>
              <Text style={styles.modifyButtonText}>변경</Text>
            </TouchableOpacity>
          </View>

          <Text style={styles.text}>생년월일</Text>
          {userData?.birthdate && formatDate(userData?.birthdate)}
        </View>
        <Text style={styles.title}>비밀번호 변경</Text>

        <View style={styles.passwordContainer}>
          <View style={styles.rowContainer1}>
            <Text style={styles.text}>기존 비밀번호</Text>
          </View>

          <TextInput
            style={styles.infoText}
            placeholder="기존 비밀번호를 입력하세요."
            placeholderTextColor={'grey'}
            onChangeText={text => setOldPasswordInput(text)}
            value={oldPasswordInput}
          />
          <Text style={styles.text}>새 비밀번호</Text>
          <TextInput
            style={styles.infoText}
            placeholder="새 비밀번호를 입력하세요."
            placeholderTextColor={'grey'}
            onChangeText={text => setNewPasswordInput(text)}
            value={newPasswordInput}
          />
        </View>
        <View style={{alignItems: 'flex-end'}}>
          <TouchableOpacity
            style={styles.modifyPasswordButton}
            onPress={modifyPassword}>
            <Text style={styles.modifyButtonText}>변경</Text>
          </TouchableOpacity>
        </View>

        <Text style={styles.title}>회원 탈퇴</Text>
        <View style={styles.passwordContainer}>
          <View style={styles.rowContainer1}>
            <Text style={styles.text}>비밀번호 입력</Text>
          </View>
          <TextInput
            style={styles.infoText}
            placeholder="비밀번호를 입력하세요."
            placeholderTextColor={'#C4C4C4'}
            onChangeText={text => setPassInput(text)}
            value={passInput}
          />
        </View>
        <View style={{alignItems: 'flex-end'}}>
          <TouchableOpacity style={styles.signOutButton} onPress={signOut}>
            <Text style={styles.modifyButtonText}>회원 탈퇴</Text>
          </TouchableOpacity>
        </View>
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    paddingVertical: 10,
  },
  contentsContainer: {
    backgroundColor: '#F2F8FF',
    paddingTop: 10,
    paddingBottom: 25,
    paddingHorizontal: 20,
    gap: 10,
    marginTop: 20,
    marginBottom: 30,
  },
  scrollContainer: {
    width: '100%',
    paddingHorizontal: 23,
    paddingBottom: 20,
  },
  passwordContainer: {
    backgroundColor: '#F2F8FF',
    paddingTop: 10,
    paddingBottom: 35,
    paddingHorizontal: 20,
    gap: 10,
    marginTop: 20,
    marginBottom: 10,
  },
  rowContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'flex-end',
    marginBottom: 13,
  },
  rowContainer1: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'flex-start',
  },
  infoText: {
    backgroundColor: 'white',
    color: 'black',
    paddingHorizontal: 10,
    paddingVertical: 10,
    textAlign: 'center',
    fontWeight: 'bold',
    fontSize: 18,
    borderRadius: 10,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 1,
  },
  birthText: {
    width: 90,
    color: 'black',
    backgroundColor: 'white',
    marginTop: 5,
    paddingHorizontal: 10,
    paddingVertical: 10,
    textAlign: 'center',
    fontWeight: 'bold',
    fontSize: 18,
    borderRadius: 10,
    marginBottom: 10,
  },

  title: {
    fontSize: 25,
    color: 'black',
    fontWeight: '800',
    marginTop: 10,
    marginLeft: 5,
  },
  text: {
    fontSize: 21,
    fontWeight: 'bold',
    marginTop: 12,
  },

  modifyButton: {
    paddingVertical: 12.7,
    backgroundColor: '#21C99B',
    paddingHorizontal: 20,
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 10,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 1,
  },
  modifyButtonText: {
    fontSize: 16,
    color: 'white',
    fontWeight: 'bold',
    letterSpacing: 2,
  },
  signOutButtonContainer: {
    alignItems: 'flex-end',
  },
  modifyPasswordButton: {
    paddingVertical: 12.7,
    backgroundColor: '#21C99B',
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 10,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 1,
    width: 100,
    marginBottom: 20,
  },
  signOutButton: {
    paddingVertical: 12.7,
    backgroundColor: '#21C99B',
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 10,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 1,
    width: 100,
    marginBottom: 20,
  },
  birthContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
});
