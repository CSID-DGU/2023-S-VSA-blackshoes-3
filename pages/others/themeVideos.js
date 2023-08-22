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
  },
  contentsContainer: {
    flex: 1,
  },

  scrollContainer: {
    marginTop: 15,
    width: '100%',
  },
  textContainer: {
    marginTop: 15,
  },

  title: {
    fontSize: 23,
    color: 'black',
    marginLeft: 15,
    fontWeight: '700',
    width: 200,
  },

  videoThumbnailContainer: {
    alignItems: 'center',
    paddingTop: 17,
    paddingBottom: 10,
    marginBottom: 7,
    width: '92%',
    backgroundColor: 'white',
  },
});
