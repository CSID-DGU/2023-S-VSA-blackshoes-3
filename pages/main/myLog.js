/* eslint-disable react-native/no-inline-styles */
/* eslint-disable react-hooks/exhaustive-deps */
import React, {useState, useEffect, useRef} from 'react';
import {
  View,
  StyleSheet,
  Alert,
  ScrollView,
  TouchableOpacity,
} from 'react-native';
import {useSelector} from 'react-redux';
import Icon from 'react-native-vector-icons/Ionicons';
import {VideoThumbnail} from '../../components/contents/thumbnailBox';
import axiosInstance from '../../utils/axiosInstance';
import NavigationBar from '../../components/tools/navigationBar';

export default function MyLog({navigation, route}) {
  const [page, setPage] = useState(0);
  const [maxPage, setMaxPage] = useState(10);
  const [videoData, setVideoData] = useState([]);
  const userId = useSelector(state => state.USER);
  const scrollViewRef = useRef(null);
  const [deleteControl, setDeleteControl] = useState(false);
  const [deleteArray, setDeleteArray] = useState([]);
  const [isEndOfScroll, setEndOfScroll] = useState(false);

  useEffect(() => {
    getData();
  }, [deleteControl]);
  // useEffect(() => {
  //   if (page === 0) {
  //     fetchData(0);
  //   } else {
  //     setPage(0);
  //   }
  // }, [value]);

  // useEffect(() => {
  //   if (isEndOfScroll) {
  //     setPage(prevPage => prevPage + 1);
  //   }
  // }, [isEndOfScroll]);

  // useEffect(() => {
  //   if (page === 0) {
  //     fetchData(0);
  //   } else {
  //     if (page !== maxPage) {
  //       fetchData(1);
  //     }
  //   }
  // }, [page]);

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
      console.log(userId);
      const response = await axiosInstance.get(
        `personalized-service/${userId}/videos/history?page=0&size=10`,
      );
      console.log(response.data.payload.viewVideoIdList);
      const history = response.data.payload.viewedVideos.viewVideoIdList;

      await getVideoData(history);
    } catch (error) {
      console.error(error);
    }
  };

  const getVideoData = async history => {
    try {
      const videoIdString = history.join(',');
      const response = await axiosInstance.get(
        `content-slave-service/videos/videoIds?q=${videoIdString}`,
      );
      console.log(response.data.payload.videos);
      setVideoData(response.data.payload.videos);
    } catch (e) {
      console.log(e);
    }
  };

  // const handleScroll = event => {
  //   const offsetY = event.nativeEvent.contentOffset.y;
  //   const contentHeight = event.nativeEvent.contentSize.height;
  //   const layoutHeight = event.nativeEvent.layoutMeasurement.height;

  //   if (offsetY + layoutHeight >= contentHeight) {
  //     if (!isEndOfScroll) {
  //       setEndOfScroll(true);
  //     }
  //   } else {
  //     if (isEndOfScroll) {
  //       setEndOfScroll(false);
  //     }
  //   }
  // };
  const deleteSubmit = async () => {
    try {
      const deletePromises = deleteArray.map(videoId => {
        return axiosInstance.delete(
          `personalized-service/${userId}/videos/history/${videoId}`,
        );
      });
      await Promise.all(deletePromises);

      Alert.alert(
        '삭제 완료',
        '선택한 영상이 삭제되었습니다.',
        [{text: 'OK', onPress: () => console.log('OK Pressed')}],
        {cancelable: false},
      );

      setDeleteControl(false);
    } catch (e) {
      console.log(e);
      Alert.alert(
        '삭제 실패',
        '영상 삭제 중 문제가 발생했습니다.',
        [{text: 'OK', onPress: () => console.log('OK Pressed')}],
        {cancelable: false},
      );
    }
  };

  return (
    <View style={styles.container}>
      <View style={styles.contentsContainer}>
        {deleteControl && (
          <View style={styles.deleteButtonContainer}>
            <TouchableOpacity onPress={deleteSubmit}>
              <Icon name="trash-outline" size={30} color={'black'} />
            </TouchableOpacity>
            <TouchableOpacity
              onPress={() => {
                setDeleteArray([]);
                setDeleteControl(false);
              }}>
              <Icon name="close" size={30} color={'black'} />
            </TouchableOpacity>
          </View>
        )}
        <ScrollView
          ref={scrollViewRef}
          style={styles.scrollContainer}
          contentContainerStyle={{alignItems: 'center'}}
          // onScroll={handleScroll}
          scrollEventThrottle={400}>
          {videoData.length > 0 &&
            videoData.map((e, i) => {
              return (
                <TouchableOpacity
                  style={[
                    styles.videoThumbnailContainer,
                    deleteArray.includes(e.videoId)
                      ? {backgroundColor: '#FFDDDD'}
                      : deleteControl
                      ? {backgroundColor: '#D9D9D9'}
                      : {backgroundColor: 'white'},
                  ]}
                  key={i}
                  onPress={
                    deleteControl
                      ? () => {
                          if (deleteArray.includes(e.videoId)) {
                            setDeleteArray(prev =>
                              prev.filter(id => id !== e.videoId),
                            );
                          } else {
                            setDeleteArray(prev => [...prev, e.videoId]);
                          }
                        }
                      : () => navigation.navigate('Play', {video: e})
                  }
                  onLongPress={() => {
                    setDeleteControl(true);
                  }}>
                  <VideoThumbnail key={i} video={e} navigation={navigation} />
                </TouchableOpacity>
              );
            })}
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
    backgroundColor: '#F2F8FF',
  },

  contentsContainer: {
    flex: 1,
  },

  scrollContainer: {
    marginTop: 8,
    width: '100%',
  },
  deleteButtonContainer: {
    flexDirection: 'row',
    justifyContent: 'flex-end',
    alignItems: 'center',
    paddingHorizontal: 20,
    marginTop: 10,
    gap: 5,
  },
  videoThumbnailContainer: {
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
