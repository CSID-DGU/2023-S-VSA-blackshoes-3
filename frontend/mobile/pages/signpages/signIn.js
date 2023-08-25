import React from 'react';
import {View, Image, Text, StyleSheet, TouchableOpacity} from 'react-native';

export default function SignIn({navigation}) {
  return (
    <View style={styles.container}>
      <View style={styles.explainContainer}>
        <Image
          source={require('../../assets/first_real_real.jpg')}
          resizeMode="contain"
        />
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
            <Image
              source={require('../../assets/google_logo.png')}
              resizeMode="contain"
            />
            <Text style={styles.text}>구글 로그인</Text>
          </TouchableOpacity>
          <TouchableOpacity
            style={[styles.kakaoLogin, styles.button]}
            onPress={() => navigation.navigate('Social', {what: 'kakao'})}>
            <Image
              source={require('../../assets/kakao_logo.png')}
              resizeMode="contain"
            />
            <Text style={styles.text}>카카오 로그인</Text>
          </TouchableOpacity>
          <TouchableOpacity
            style={[styles.naverLogin, styles.button]}
            onPress={() => navigation.navigate('Social', {what: 'naver'})}>
            <Image
              source={require('../../assets/naver_logo.png')}
              resizeMode="contain"
            />
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
    flex: 0.4,
    width: '100%',
  },
  contentsContainer: {
    backgroundColor: 'white',
    alignItems: 'center',
    flex: 0.6,
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
    gap: 20,
    paddingTop: 40,
  },
  linkContainer: {
    flex: 0.25,
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    width: '100%',
    gap: 10,
  },
  button: {
    flexDirection: 'row',
    gap: 10,
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
