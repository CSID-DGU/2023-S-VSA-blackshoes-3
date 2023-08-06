/* eslint-disable react-native/no-inline-styles */
/* eslint-disable react-hooks/exhaustive-deps */
import React, {useState, useEffect} from 'react';
import {
  View,
  StyleSheet,
  ScrollView,
  Text,
  TouchableOpacity,
} from 'react-native';
import {VideoThumbnail} from '../components/videothumbnail';
import {SERVER_IP} from '../config';
import Toolbar from '../components/toolBar';
import axios from 'axios';

export default function MyVideo({navigation, route}) {
  const [view, setView] = useState('tag');
  const [videoData, setVideoData] = useState([]);

  //임시
  const [value, setValue] = useState('recent');
  const [items, setItems] = useState([
    {label: '최신 순', value: 'recent'},
    {label: '조회 순', value: 'views'},
    {label: '좋아요 순', value: 'likes'},
  ]);

  useEffect(() => {
    getData();
  }, [view]);

  const getData = async () => {
    const response = await axios.get(
      //임시
      `${SERVER_IP}:8011/content-slave-service/videos/sort?s=likes&page=0&size=10`,
      // `${SERVER_IP}:8011/content-slave-service/videos/{userId}/personalized?q=${view}&page=0&size=10`
    );

    setVideoData(response.data.payload.videos);
  };

  const handlepress = e => {
    if (view === e) {
      return;
    } else {
      setView(e);
    }
  };

  return (
    <View style={styles.container}>
      <View style={styles.contentsContainer}>
        <View style={styles.buttonContainer}>
          <TouchableOpacity
            style={styles.button}
            onPress={() => handlepress('tag')}>
            <Text>태그</Text>
          </TouchableOpacity>
          <TouchableOpacity
            style={styles.button}
            onPress={() => handlepress('like')}>
            <Text>좋아요</Text>
          </TouchableOpacity>
        </View>
        <ScrollView
          style={styles.videoContainer}
          contentContainerStyle={{alignItems: 'center'}}>
          {videoData.length > 0 &&
            videoData.map((e, i) => {
              return <VideoThumbnail key={i} video={e} />;
            })}
        </ScrollView>
      </View>

      <View style={styles.toolbarContainer}>
        <Toolbar route={route} />
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
  toolbarContainer: {
    height: 60,
    backgroundColor: 'white',
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: -2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 10,
  },
  videoContainer: {
    marginTop: 20,
    width: '100%',
  },

  buttonContainer: {
    flexDirection: 'row',
  },

  button: {
    justifyContent: 'center',
    alignItems: 'center',
    width: 60,
    // borderRadius:10,
    // shadowColor: "#000",
    // shadowOffset: {
    //   width: 0,
    //   height: 2,
    // },
    // shadowOpacity: 0.25,
    // shadowRadius: 3.84,
    // elevation: 5,
  },

  dropDownButton: {
    minHeight: 40,
    width: 107,
    borderColor: 'gray',
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 5,
  },
  dropdownpickerContainer: {
    zIndex: 2,
  },
});
