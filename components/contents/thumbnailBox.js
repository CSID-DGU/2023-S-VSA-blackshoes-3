import React, {useState} from 'react';
import {View, StyleSheet, Text, Image, TouchableOpacity} from 'react-native';

export const VideoThumbnail = ({video, navigation}) => {
  //   "videoId":"v43039bd-2199-11ee-9ef2-0a0027000003",
  // "videoName":"test25",
  // "thumbnailUrl":"test25.com/thumbnail",
  // "sellerName":"seller3",
  // "sellerLogoUrl":null,
  // "createdAt":"2023-07-25 12:49:23.480000",
  // "likes":320,
  // "views":440,
  // "adClicks":24
  // },
  console.log('in videoTHumbnail', video.thumbnailUrl);
  return (
    // <TouchableOpacity
    //   style={styles.container}
    //   onPress={() => navigation.navigate('Play', {video})}>
    <>
      <Image
        style={styles.imgStyle}
        source={{uri: video.thumbnailUrl, cache: 'force-cache'}}
      />
      <View style={styles.informationContainer}>
        <Image
          style={styles.logo}
          source={{uri: `data:image/png;base64,${video.sellerLogo}`}}
        />

        <View style={styles.infoTextContainer}>
          <View style={styles.infoTextBox}>
            <Text style={styles.infoTitle}>{video.videoName}</Text>
            <Text style={styles.infoText}>
              업로드일: {video.createdAt.slice(0, 10)}
            </Text>
          </View>
          <View style={styles.infoTextBox}>
            <Text style={styles.infoText}>조회수: {video.views}</Text>
            <Text style={styles.infoText}>좋아요: {video.likes}</Text>
          </View>
        </View>
      </View>
    </>
    // </TouchableOpacity>
  );
};

const styles = StyleSheet.create({
  // container: {
  //   backgroundColor: 'white',
  //   alignItems: 'center',
  //   paddingTop: 17,
  //   paddingBottom: 10,
  //   marginVertical: 7,
  //   shadowColor: '#000',
  //   shadowOffset: {
  //     width: 0,
  //     height: 2,
  //   },
  //   shadowOpacity: 0.25,
  //   shadowRadius: 4,
  //   elevation: 3,
  //   width: '90%',
  //   borderRadius: 10,
  // },

  imgStyle: {
    width: '90%',
    height: 180,
  },
  informationContainer: {
    flexDirection: 'row',
    marginTop: 10,
    height: 50,
    alignItems: 'center',
  },
  logo: {
    width: 42,
    height: 42,
    borderRadius: 15,
    resizeMode: 'contain',
  },
  infoTextContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    width: '80%',
    paddingLeft: 15,
    paddingRight: 10,
  },
  infoTextBox: {
    justifyContent: 'center',
    height: '100%',
  },
  infoTitle: {
    fontSize: 18,
    fontWeight: 'bold',
  },

  infoText: {
    marginBottom: 2,
    fontSize: 13,
    color: '#808080',
  },
});
