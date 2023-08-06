/* eslint-disable react-native/no-inline-styles */
/* eslint-disable react-hooks/exhaustive-deps */
import React, {useState, useEffect, useRef} from 'react';
import {View, StyleSheet, ScrollView, Text} from 'react-native';
import DropDownPicker from 'react-native-dropdown-picker';
import {VideoThumbnail} from '../components/videothumbnail';
import {SERVER_IP} from '../config';
import Toolbar from '../components/toolBar';
import axios from 'axios';
export default function Video({navigation, route}) {
  const [open, setOpen] = useState(false);
  const [page, setPage] = useState(0);
  const [maxPage, setMaxPage] = useState(10);
  const [videoData, setVideoData] = useState([]);
  const scrollViewRef = useRef(null);

  const [value, setValue] = useState('recent');
  const [items, setItems] = useState([
    {label: '최신 순', value: 'recent'},
    {label: '조회 순', value: 'views'},
    {label: '좋아요 순', value: 'likes'},
  ]);
  const [isEndOfScroll, setEndOfScroll] = useState(false);

  useEffect(() => {
    if (page === 0) {
      fetchData(0);
    } else {
      setPage(0);
    }
  }, [value]);

  useEffect(() => {
    if (isEndOfScroll) {
      setPage(prevPage => prevPage + 1);
    }
  }, [isEndOfScroll]);

  useEffect(() => {
    if (page === 0) {
      fetchData(0);
    } else {
      if (page !== maxPage) {
        fetchData(1);
      }
    }
  }, [page]);

  const fetchData = async key => {
    if (key === 0) {
      scrollViewRef.current.scrollTo({x: 0, y: 0, animated: true});
      const newVideoData = await getData();
      setVideoData(newVideoData[0]);
      setMaxPage(newVideoData[1]);
    } else {
      const newVideoData = await getData();
      setVideoData(prevVideoData => {
        return [...prevVideoData, ...newVideoData[0]];
      });
    }
  };

  const getData = async () => {
    try {
      const response = await axios.get(
        `${SERVER_IP}:8011/content-slave-service/videos/sort?s=${value}&page=${page}&size=10`,
      );
      return [response.data.payload.videos, response.data.payload.totalPages];
    } catch (error) {
      console.error('Error occurred while fetching data:', error);
    }
  };

  const handleScroll = event => {
    const offsetY = event.nativeEvent.contentOffset.y;
    const contentHeight = event.nativeEvent.contentSize.height;
    const layoutHeight = event.nativeEvent.layoutMeasurement.height;

    if (offsetY + layoutHeight >= contentHeight) {
      if (!isEndOfScroll) {
        setEndOfScroll(true);
      }
    } else {
      if (isEndOfScroll) {
        setEndOfScroll(false);
      }
    }
  };

  return (
    <View style={styles.container}>
      <View style={styles.contentsContainer}>
        <View style={styles.dropdownpickerContainer}>
          <DropDownPicker
            open={open}
            value={value}
            items={items}
            setOpen={setOpen}
            setValue={setValue}
            setItems={setItems}
            style={styles.dropDownButton}
            containerStyle={{width: 107}}
          />
        </View>

        <ScrollView
          ref={scrollViewRef}
          style={styles.videoContainer}
          contentContainerStyle={{alignItems: 'center'}}
          onScroll={handleScroll}
          scrollEventThrottle={400}>
          {videoData.length > 0 &&
            videoData.map((e, i) => {
              return (
                <VideoThumbnail key={i} video={e} navigation={navigation} />
              );
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
    marginTop: 10,
    width: '100%',
  },

  dropDownButton: {
    marginTop: 10,
    marginLeft: 18,
    minHeight: 40,
    width: 105,
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
    justifyContent: 'center',
    zIndex: 2,
  },
});
