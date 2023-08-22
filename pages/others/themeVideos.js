/* eslint-disable react-native/no-inline-styles */
/* eslint-disable react-hooks/exhaustive-deps */
import React, {useState, useEffect} from 'react';
import {
  View,
  StyleSheet,
  Text,
  TouchableOpacity,
  ScrollView,
} from 'react-native';
import axiosInstance from '../../utils/axiosInstance';
import {VideoThumbnail} from '../../components/contents/thumbnailBox';
export default function ThemeVideo({route, navigation}) {
  const [videoData, setVideoData] = useState([]);

  useEffect(() => {
    getData();
  }, []);

  const getData = async () => {
    const response = await axiosInstance.get(
      `content-slave-service/videos/search?type=tagName&q=${route.params.item.tagName}&s=recent&page=0&size=10`,
    );
    setVideoData(response.data.payload.videos);
  };

  return (
    <View style={styles.container}>
      <View style={styles.contentsContainer}>
        <View style={styles.textContainer}>
          <Text style={styles.title}>{route.params.item.tagName}</Text>
        </View>

        <ScrollView
          style={styles.scrollContainer}
          contentContainerStyle={{alignItems: 'center'}}>
          {videoData.length > 0 &&
            videoData.map((e, i) => {
              return (
                <TouchableOpacity
                  style={styles.videoThumbnailContainer}
                  key={i}
                  onPress={() => navigation.navigate('Play', {video: e})}>
                  <VideoThumbnail key={i} video={e} navigation={navigation} />
                </TouchableOpacity>
              );
            })}
        </ScrollView>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'space-between',
    backgroundColor: '#F2F8FF',
  },
  contentsContainer: {
    flex: 1,
  },

  scrollContainer: {
    marginTop: 15,
    width: '100%',
  },
  textContainer: {width: '100%'},

  title: {
    fontSize: 30,
    color: '#525252',
    fontWeight: 'bold',
    marginTop: 15,
    paddingHorizontal: 20,
    fontStyle: 'italic',
    textShadowOffset: {width: 0, height: 1},
    textShadowRadius: 2,
    textShadowColor: 'gray',
  },

  videoThumbnailContainer: {
    backgroundColor: 'white',
    alignItems: 'center',
    paddingTop: 17,
    paddingBottom: 10,
    marginVertical: 7,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 4,
    elevation: 3,
    width: '90%',
    borderRadius: 10,
  },
});
