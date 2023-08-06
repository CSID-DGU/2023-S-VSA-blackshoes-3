/* eslint-disable react-hooks/exhaustive-deps */
import React, {useState, useEffect} from 'react';
import {
  TouchableOpacity,
  Text,
  StyleSheet,
  Animated,
  Dimensions,
} from 'react-native';

export const VideoModal = ({visible, video, onClose}) => {
  const [modalAnimatedValue] = useState(new Animated.Value(0));

  useEffect(() => {
    Animated.timing(modalAnimatedValue, {
      toValue: visible ? 1 : 0,
      duration: 300,
      useNativeDriver: true,
    }).start();
  }, [visible]);

  const modalStyle = {
    ...styles.modal,
    opacity: modalAnimatedValue,
    transform: [
      {
        translateY: modalAnimatedValue.interpolate({
          inputRange: [0, 1],
          outputRange: [modalHeight, 0],
        }),
      },
    ],
  };

  return visible ? (
    <Animated.View style={modalStyle}>
      {/* {video} */}
      <TouchableOpacity onPress={onClose}>
        <Text>Close Modal</Text>
      </TouchableOpacity>
    </Animated.View>
  ) : null;
};

const modalHeight = Dimensions.get('window').height * 0.8;

const styles = StyleSheet.create({
  modal: {
    flex: 1,
    position: 'absolute',
    bottom: 0,
    width: '100%',
    height: modalHeight,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'white',
    borderTopLeftRadius: 20,
    borderTopRightRadius: 20,
  },
});
