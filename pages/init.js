/* eslint-disable react-hooks/exhaustive-deps */
import React, {useEffect} from 'react';
import {StyleSheet, Animated, View, Text} from 'react-native';

import AsyncStorage from '@react-native-async-storage/async-storage';
import {useDispatch} from 'react-redux';
import {setUserId, setAccessToken, setRefreshToken} from '../storage/actions';

export default function Init({navigation}) {
  const dispatch = useDispatch();
  const dropAnimations = Array.from('WANDER').map(
    () => new Animated.Value(-100),
  );

  const loadUserData = async () => {
    const userId = await AsyncStorage.getItem('user');
    if (userId) {
      dispatch(setUserId(userId));
      dispatch(setAccessToken(await AsyncStorage.getItem('accessToken')));
      dispatch(setRefreshToken(await AsyncStorage.getItem('refreshToken')));
      navigation.navigate('Home');
    } else {
      navigation.navigate('SignIn');
    }
  };

  useEffect(() => {
    loadUserData();
    const animate = () => {
      dropAnimations.forEach(animation => animation.setValue(-100));

      Animated.stagger(
        200,
        dropAnimations.map(animation =>
          Animated.timing(animation, {
            toValue: 0,
            duration: 700,
            useNativeDriver: true,
          }),
        ),
      ).start(() => {
        setTimeout(animate, 1000);
      });
    };

    animate();
  }, []);

  return (
    <View style={styles.container}>
      {/* style={styles.container}
      source={require('../assets/first.jpg')}> */}
      <View style={styles.overlay} />
      <View style={styles.first}>
        <View style={styles.row}>
          {Array.from('WANDER').map((letter, index) => (
            <Animated.View
              key={index}
              style={[{transform: [{translateY: dropAnimations[index]}]}]}>
              <Text style={styles.Text}>{letter}</Text>
            </Animated.View>
          ))}
        </View>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#E2F5FF',
  },
  // overlay: {
  //   ...StyleSheet.absoluteFillObject,
  //   backgroundColor: 'rgba(0, 0, 0, 0.2)',
  // },
  first: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  Text: {
    color: 'white',
    fontSize: 40,
    letterSpacing: 12,
    fontWeight: 'bold',
    marginBottom: 30,
    fontStyle: 'italic',
    textAlign: 'center',
    textShadowOffset: {width: 2, height: 2},
    textShadowRadius: 2,
    textShadowColor: 'gray',
  },
  row: {
    flexDirection: 'row',
  },
});
