/* eslint-disable react-native/no-inline-styles */
/* eslint-disable quotes */
import React, {useState, useEffect} from 'react';

import {View, ActivityIndicator as Spinner} from 'react-native';

import {WebView} from 'react-native-webview';

import axios from 'axios';
import {SERVER_IP} from '../../config';

const KakaoLogin = ({navigation}) => {
  const [kakaoUrl, setKakaoUrl] = useState('');

  useEffect(() => {
    kakao();
  }, []);

  const kakao = async () => {
    try {
      const response = await axios.post(`${SERVER_IP}oauth2/authorize/kakao`);
      console.log('response : ', response.data.accounts_login_url);
      setKakaoUrl(response.data.accounts_login_url);
      console.log('fine');
    } catch (e) {
      console.log(e);
    }
  };
  const handleWebViewNavigationStateChange = navState => {
    setKakaoUrl(navState.url);
  };
  console.log('url : ', kakaoUrl);

  return (
    <>
      {kakaoUrl === '' ? (
        <View style={{flex: 1}}>
          <Spinner size="big" color="black" />
        </View>
      ) : (
        <View style={{flex: 1}}>
          <WebView
            source={{
              uri: kakaoUrl,
            }}
            onNavigationStateChange={handleWebViewNavigationStateChange}
          />
        </View>
      )}
    </>
  );
};

export default KakaoLogin;
