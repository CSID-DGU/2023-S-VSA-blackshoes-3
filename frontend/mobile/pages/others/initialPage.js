/* eslint-disable react-hooks/exhaustive-deps */
import React, {useEffect} from 'react';
import {StyleSheet, Animated, View, TouchableOpacity, Text} from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import {useDispatch} from 'react-redux';
import {setAll} from '../../storage/actions';
import axiosInstance from '../../utils/axiosInstance';

export default function Init({navigation}) {
  const dispatch = useDispatch();
  const dropAnimations = Array.from('WANDER').map(
    () => new Animated.Value(-100),
  );
  const loadUserData = async () => {
    const email = await AsyncStorage.getItem('email');
    if (email) {
      const password = await AsyncStorage.getItem('pass');
      console.log(email);
      console.log(password);
      const registerData = {
        email: email,
        password: password,
      };
      try {
        const userResponse = await axiosInstance.post(
          'user-service/users/login',
          registerData,
        );

        console.log(userResponse);
        dispatch(setAll(userResponse.data.payload));
        // try {
        //   console.log('hi', userResponse.data.payload.userId);
        //   const tagResponse = await axios.get(
        //     `${SERVER_IP}personalized-service/${userResponse.data.payload.userId}/tags/subscribed`,
        //   );

        //   console.log('hi tagIds', tagResponse.data.payload.subscribedTagList);
        //   dispatch(setTag(tagResponse.data.payload.subscribedTagList));
        // } catch (e) {
        //   console.log(e);
        // }

        navigation.navigate('Home');
      } catch (e) {
        console.log(e);
      }
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
      <TouchableOpacity style={styles.first} onPress={loadUserData}>
        <View style={styles.row}>
          {Array.from('WANDER').map((letter, index) => (
            <Animated.View
              key={index}
              style={[{transform: [{translateY: dropAnimations[index]}]}]}>
              <Text style={styles.Text}>{letter}</Text>
            </Animated.View>
          ))}
        </View>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#E2F5FF',
  },
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
