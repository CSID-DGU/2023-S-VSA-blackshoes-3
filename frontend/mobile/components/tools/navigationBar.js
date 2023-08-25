import React from 'react';
import {Image, View, TouchableOpacity, StyleSheet} from 'react-native';
import {useNavigation} from '@react-navigation/native';

export default function NavigationBar({route}) {
  const navigation = useNavigation();
  return (
    <View style={styles.style_Toolbar}>
      <TouchableOpacity
        onPress={() => navigation.navigate('Home')}
        style={styles.imgContainer}>
        <Image
          style={styles.img_style}
          source={
            route.name.includes('Home')
              ? require('../../assets/toolbarImg/home_s.png')
              : require('../../assets/toolbarImg/home_d.png')
          }
        />
      </TouchableOpacity>
      <TouchableOpacity
        onPress={() => navigation.navigate('ShowVideo')}
        style={styles.imgContainer}>
        <Image
          style={styles.img_style}
          source={
            route.name.includes('ShowVideo')
              ? require('../../assets/toolbarImg/video_s.png')
              : require('../../assets/toolbarImg/video_d.png')
          }
        />
      </TouchableOpacity>
      <TouchableOpacity
        onPress={() => navigation.navigate('My')}
        style={styles.imgContainer}>
        <Image
          style={styles.img_style}
          source={
            route.name.includes('My')
              ? require('../../assets/toolbarImg/my_s.png')
              : require('../../assets/toolbarImg/my_d.png')
          }
        />
      </TouchableOpacity>
      <TouchableOpacity
        onPress={() => navigation.navigate('Log')}
        style={styles.imgContainer}>
        <Image
          style={styles.img_style}
          source={
            route.name.includes('Log')
              ? require('../../assets/toolbarImg/log_s.png')
              : require('../../assets/toolbarImg/log_d.png')
          }
        />
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  style_Toolbar: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',

    height: 65,
    backgroundColor: 'white',
  },
  imgContainer: {
    paddingHorizontal: 25,
    paddingVertical: 10,
  },
  img_style: {
    width: 36,
    height: 36,
  },
});
