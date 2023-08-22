import React, {useState, useEffect} from 'react';
import {View, Text, StyleSheet, TouchableOpacity} from 'react-native';
import axios from '../../utils/axiosInstance';
import {set} from 'lodash';

export default function SignIn({navigation}) {
  return (
    <View style={styles.container}>
      <View style={styles.explainContainer}>
        <Text stlye={styles.greyText}>서비스 설명 이미지</Text>
      </View>

      <View style={styles.contentsContainer}>
        <View style={styles.buttonContainer}>
          <TouchableOpacity
            style={[styles.login, styles.button]}
            onPress={() => {
              navigation.navigate('Login');
            }}>
            <Text style={styles.whiteText}>로그인</Text>
          </TouchableOpacity>
          <TouchableOpacity
            style={[styles.googleLogin, styles.button]}
            onPress={() => navigation.navigate('Social', {what: 'google'})}>
            <Text style={styles.text}>구글 로그인</Text>
          </TouchableOpacity>
          <TouchableOpacity
            style={[styles.kakaoLogin, styles.button]}
            onPress={() => navigation.navigate('Social', {what: 'kakao'})}>
            <Text style={styles.text}>카카오 로그인</Text>
          </TouchableOpacity>
          <TouchableOpacity
            style={[styles.naverLogin, styles.button]}
            onPress={() => navigation.navigate('Social', {what: 'naver'})}>
            <Text style={styles.whiteText}>네이버 로그인</Text>
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
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F2F8FF',
    alignItems: 'center',
  },
  explainContainer: {
    justifyContent: 'center',
    alignItems: 'center',
    flex: 0.45,
    width: '100%',
  },
  contentsContainer: {
    backgroundColor: 'white',
    alignItems: 'center',
    flex: 0.55,
    width: '100%',
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 4,
    elevation: 3,
  },
  buttonContainer: {
    alignItems: 'center',
    flex: 0.7,
    width: '100%',
    gap: 15,
    paddingTop: 25,
  },
  linkContainer: {
    flex: 0.3,
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    width: '100%',
    gap: 10,
  },
  button: {
    borderRadius: 10,
    width: '80%',
    alignItems: 'center',
    height: 54,
    justifyContent: 'center',
    flexShrink: 0,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 4,
    elevation: 3,
  },

  login: {
    backgroundColor: '#1DAE86',
    borderRadius: 10,
  },

  googleLogin: {backgroundColor: 'white'},
  kakaoLogin: {backgroundColor: '#FEE500'},
  naverLogin: {backgroundColor: '#03C75A'},
  whiteText: {
    color: 'white',
    fontSize: 16,
  },
  text: {
    color: 'black',
    fontSize: 16,
  },
  greyText: {
    color: '#BDBDBD',
    fontSize: 16,
  },
});
