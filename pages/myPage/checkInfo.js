/* eslint-disable react-hooks/exhaustive-deps */
import React, {useEffect, useState} from 'react';
import axios from 'axios';
import {SERVER_IP} from '../../config';
import {useDispatch, useSelector} from 'react-redux';
import {deleteUserId} from '../../storage/actions';
import {View, Text, StyleSheet, TouchableOpacity, Image} from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import axiosInstance from '../utils/axiosInstance';

export default function CheckInfo({navigation}) {
  const dispatch = useDispatch();
  const data = useSelector(state => state);
  const [userData, setUserData] = useState({});

  const getUserData = async () => {
    try {
      console.log(data);
      console.log('hi', data);
      const response = await axiosInstance.get(
        `:8001/user-service/users/${data.USER}`,
        {
          headers: {
            Authorization: `Bearer ${data.ACCESS}`,
          },
        },
      );

      console.log(response.data.payload);
      setUserData(response.data);
    } catch (e) {
      console.error(e);
    }
  };

  useEffect(() => {
    getUserData();
    console.log('엑세스 : ', data.ACCESS);
    console.log('리프레쉬 : ', data.REFRESH);
  }, []);

  const signOut = async () => {
    try {
      await axiosInstance.post(
        `:8001/user-service/users/${data.USER}/withdrawal`,
        {
          password: 'test12345@',
        },
        {
          headers: {
            Authorization: `Bearer ${data.ACCESS}`,
          },
        },
      );
      await AsyncStorage.removeItem('user');
      await AsyncStorage.removeItem('accessToken');
      await AsyncStorage.removeItem('refreshToken');
      dispatch(deleteUserId());
      navigation.navigate('Login');
    } catch (e) {
      console.error(e);
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>회원 정보 조회</Text>
      <View style={styles.contentsContainer}>
        <Text style={styles.text}>Email</Text>
        <Text style={styles.text}>이름</Text>
      </View>
      <TouchableOpacity style={styles.signOutButton} onPress={signOut}>
        <Text>회원 탈퇴</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {},
  signOutButton: {},
});
