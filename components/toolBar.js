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
              ? require('../assets/toolbarImg/home1.png')
              : require('../assets/toolbarImg/home.png')
          }
        />
      </TouchableOpacity>
      <TouchableOpacity onPress={() => navigation.navigate('Video')}>
        <Image
          style={styles.img_style}
          source={
            route.name.includes('Video')
              ? require('../assets/toolbarImg/video1.png')
              : require('../assets/toolbarImg/video.png')
          }
        />
      </TouchableOpacity>
      <TouchableOpacity onPress={() => navigation.navigate('My')}>
        <Image
          style={styles.img_style}
          source={
            route.name.includes('My')
              ? require('../assets/toolbarImg/my1.png')
              : require('../assets/toolbarImg/my.png')
          }
        />
      </TouchableOpacity>
      <TouchableOpacity onPress={() => navigation.navigate('Log')}>
        <Image
          style={styles.img_style}
          source={
            route.name.includes('Log')
              ? require('../assets/toolbarImg/log1.png')
              : require('../assets/toolbarImg/log.png')
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
    paddingHorizontal: 20,
  },
});
