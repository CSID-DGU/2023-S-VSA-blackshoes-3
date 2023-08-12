import React from 'react';
import {Image, View, TouchableOpacity, StyleSheet} from 'react-native';
import {useNavigation} from '@react-navigation/native';

export default function Toolbar({route}) {
  const navigation = useNavigation();
  return (
    <View style={styles.style_Toolbar}>
      <TouchableOpacity onPress={() => navigation.navigate('Home')}>
        <Image
          style={styles.img_style}
          source={
            route.name.includes('Home')
              ? require('../assets/toolbarImg/home_s.png')
              : require('../assets/toolbarImg/home_d.png')
          }
        />
      </TouchableOpacity>
      <TouchableOpacity onPress={() => navigation.navigate('Video')}>
        <Image
          style={styles.img_style}
          source={
            route.name.includes('Video')
              ? require('../assets/toolbarImg/video_s.png')
              : require('../assets/toolbarImg/video_d.png')
          }
        />
      </TouchableOpacity>
      <TouchableOpacity onPress={() => navigation.navigate('My')}>
        <Image
          style={styles.img_style}
          source={
            route.name.includes('My')
              ? require('../assets/toolbarImg/my_s.png')
              : require('../assets/toolbarImg/my_d.png')
          }
        />
      </TouchableOpacity>
      <TouchableOpacity onPress={() => navigation.navigate('Log')}>
        <Image
          style={styles.img_style}
          source={
            route.name.includes('Log')
              ? require('../assets/toolbarImg/log_s.png')
              : require('../assets/toolbarImg/log_d.png')
          }
        />
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  style_Toolbar: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 45,
    height: 65,
    backgroundColor: 'white',
  },
  img_style: {
    width: 36,
    height: 36,
  },
});
