/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable react-native/no-inline-styles */
/* eslint-disable quotes */
import React, {useState, useEffect} from 'react';

import {View, Alert, ActivityIndicator as Spinner} from 'react-native';

import {WebView} from 'react-native-webview';

import {SERVER_IP} from '../../config';
import axios from 'axios';
import {useDispatch} from 'react-redux';
import {
  setUserId,
  setRefreshToken,
  setAccessToken,
} from '../../storage/actions';

const getParams = url => {
  const paramPart = url.split('?')[1];
  if (!paramPart) return {};

  const paramStrings = paramPart.split('&');

  return paramStrings.reduce((acc, paramString) => {
    const [key, value] = paramString.split('=');
    acc[key] = decodeURIComponent(value);
    return acc;
  }, {});
};

const SocialLogin = ({route, navigation}) => {
  const [url, setUrl] = useState('');

  const dispatch = useDispatch();

  const handleWebViewNavigationStateChange = navState => {
    setUrl(navState.url);
  };

  console.log('url : ', url);

  useEffect(() => {
    if (url === 'http://13.125.69.94:8001/login?error') {
      Alert.alert(
        '로그인 불가',
        '회원 정보가 없습니다. 회원가입을 먼저 진행해 주세요.',
        [{text: 'OK', onPress: () => console.log('OK Pressed')}],
        {cancelable: false},
      );
      navigation.navigate('SignIn');
      return;
    }
    if (url.includes('social-login')) {
      const params = getParams(url);
      dispatch(setUserId(params['userId']));
      dispatch(setAccessToken(params['access-token']));
      dispatch(setRefreshToken(params['refresh-token']));
      // console.log('User Id: ', params['userId']);
      // console.log('Access Token:', params['access-token']);
      // console.log('Refresh Token:', params['refresh-token']);
      navigation.navigate('Home');
    }
  }, [url]);

  return (
    <View style={{flex: 1}}>
      {url.includes('social-login') ? (
        <View style={{marginTop: 30}}>
          <Spinner size="big" color="black" />
        </View>
      ) : (
        <WebView
          source={{
            uri: `${SERVER_IP}oauth2/authorize/${route.params.what}`,
          }}
          onNavigationStateChange={handleWebViewNavigationStateChange}
        />
      )}
    </View>
  );
};

export default SocialLogin;
