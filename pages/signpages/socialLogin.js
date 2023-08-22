/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable react-native/no-inline-styles */
/* eslint-disable quotes */
import React, {useState, useEffect} from 'react';

import {View, Alert, ActivityIndicator as Spinner} from 'react-native';

import {WebView} from 'react-native-webview';

import {SERVER_IP} from '../../config';

const SocialLogin = ({route, navigation}) => {
  const [url, setUrl] = useState('');

  // useEffect(() => {
  //   kakao();
  // }, []);
  console.log('route.params.what : ', route.params.what);

  // const kakao = async () => {
  //   try {
  //     const response = await axios.post(
  //       `${SERVER_IP}oauth2/authorize/${route.params.what}`,
  //     );
  //     console.log('response : ', response);
  //     setKakaoUrl(response.data.accounts_login_url);
  //     console.log('fine');
  //   } catch (e) {
  //     console.log(e);
  //     if (e.response && e.response.status === 401) {
  //     }
  //   }
  // };

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
    //if(url === '')
  }, [url]);

  return (
    <>
      {/* {kakaoUrl === '' ? (
        <View style={{flex: 1}}>
          <Spinner size="big" color="black" />
        </View>
      ) : ( */}
      <View style={{flex: 1}}>
        <WebView
          source={{
            uri: `${SERVER_IP}oauth2/authorize/${route.params.what}`,
          }}
          onNavigationStateChange={handleWebViewNavigationStateChange}
        />
      </View>
      {/* //) */}
    </>
  );
};

export default SocialLogin;
