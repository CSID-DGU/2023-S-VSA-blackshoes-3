/* eslint-disable react-native/no-inline-styles */
/* eslint-disable react-hooks/exhaustive-deps */
import React, {useState, useEffect, useRef} from 'react';
import {
  View,
  StyleSheet,
  Alert,
  ScrollView,
  TouchableOpacity,
  Text,
} from 'react-native';
import {useSelector} from 'react-redux';
import Icon from 'react-native-vector-icons/Ionicons';
import {VideoThumbnail} from '../../components/contents/thumbnailBox';
import axiosInstance from '../../utils/axiosInstance';
import NavigationBar from '../../components/tools/navigationBar';

export default function MyLog({navigation, route}) {
  const [page, setPage] = useState(0);
  const [videoData, setVideoData] = useState([]);
  const userId = useSelector(state => state.USER);
  const [deleteControl, setDeleteControl] = useState(false);
  const [deleteArray, setDeleteArray] = useState([]);

  useEffect(() => {
    getData();
  }, [page, deleteControl]);

  const getData = async () => {
    try {
      const response = await axiosInstance.get(
        `personalized-service/${userId}/videos/history?page=${page}&size=10`,
      );
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
      setVideoData(prevVideos => [
        ...prevVideos,
        ...response.data.payload.videos,
      ]);
    } catch (e) {
      console.log(e);
    }
  };

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

  const handleScrollend = event => {
    const offsetY = event.nativeEvent.contentOffset.y;
    const contentHeight = event.nativeEvent.contentSize.height;
    const layoutHeight = event.nativeEvent.layoutMeasurement.height;

    if (offsetY + layoutHeight >= contentHeight) {
      setPage(prevPage => prevPage + 1);
    }
  };

  return (
    <View style={styles.container}>
      <View style={styles.contentsContainer}>
        <View style={styles.textContainer}>
          <Text style={styles.title}>시청 기록</Text>

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
        </View>

        <ScrollView
          style={styles.scrollContainer}
          contentContainerStyle={{alignItems: 'center'}}
          onScroll={handleScrollend}
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
                      : {backgroundColor: '#DEDEDE'},
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
  },

  contentsContainer: {
    flex: 1,
  },
  textContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingLeft: 10,
    width: 360,
    marginTop: 10,
  },
  title: {
    fontSize: 23,
    color: '#4D4D4D',
    marginLeft: 15,
    fontWeight: '700',
    fontStyle: 'italic',
    width: 120,
  },
  scrollContainer: {
    marginTop: 8,
    width: '100%',
  },
  deleteButtonContainer: {
    flexDirection: 'row',
    justifyContent: 'flex-end',
    alignItems: 'center',
    gap: 5,
  },
  videoThumbnailContainer: {
    alignItems: 'center',
    paddingTop: 17,
    paddingBottom: 10,
    marginVertical: 7,
    width: '90%',
  },
});
