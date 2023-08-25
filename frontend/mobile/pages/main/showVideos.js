/* eslint-disable react-native/no-inline-styles */
/* eslint-disable react-hooks/exhaustive-deps */
import React, {useState, useEffect, useRef} from 'react';
import {
  View,
  StyleSheet,
  ScrollView,
  ActivityIndicator as Spinner,
} from 'react-native';
import DropDownPicker from 'react-native-dropdown-picker';
import {TouchableOpacity} from 'react-native';
import {VideoThumbnail} from '../../components/contents/thumbnailBox';
import NavigationBar from '../../components/tools/navigationBar';
import axiosInstance from '../../utils/axiosInstance';

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
      const response = await axiosInstance.get(
        `content-slave-service/videos/sort?s=${value}&page=${page}&size=10`,
      );
      return [response.data.payload.videos, response.data.payload.totalPages];
    } catch (error) {
      console.error(error);
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
            dropDownContainerStyle={{
              marginTop: 11,
              width: 105,
              borderWidth: 0,
            }}
          />
        </View>

        <ScrollView
          ref={scrollViewRef}
          style={styles.scrollContainer}
          contentContainerStyle={{alignItems: 'center', paddingTop: 5}}
          onScroll={handleScroll}
          scrollEventThrottle={400}>
          {videoData.length > 0 ? (
            videoData.map((e, i) => {
              return (
                <TouchableOpacity
                  style={styles.videoThumbnailContainer}
                  key={i}
                  onPress={() => navigation.navigate('Play', {video: e})}>
                  <VideoThumbnail key={i} video={e} navigation={navigation} />
                </TouchableOpacity>
              );
            })
          ) : (
            <View style={styles.spinnerContainer}>
              <Spinner size="big" color="black" />
            </View>
          )}
        </ScrollView>
      </View>
      <NavigationBar route={route} />
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
  spinnerContainer: {
    marginTop: 200,
  },
  scrollContainer: {
    marginTop: 10,
    width: '100%',
  },

  videoThumbnailContainer: {
    backgroundColor: 'white',

    alignItems: 'center',
    paddingTop: 17,
    paddingBottom: 10,
    marginBottom: 10,
    width: '92%',
  },

  dropDownButton: {
    marginTop: 10,
    minHeight: 40,
    width: 105,

    borderWidth: 0,
  },
  dropdownpickerContainer: {
    justifyContent: 'center',
    zIndex: 2,
    marginLeft: 15,

    borderWidth: 0,
  },
});
