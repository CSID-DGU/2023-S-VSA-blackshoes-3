/* eslint-disable react/no-unstable-nested-components */
/* eslint-disable react-native/no-inline-styles */
/* eslint-disable react-hooks/exhaustive-deps */
import React, {useState, useRef, useEffect} from 'react';
import {
  View,
  StyleSheet,
  ScrollView,
  Text,
  TouchableOpacity,
  ImageBackground,
} from 'react-native';
import {useSelector} from 'react-redux';
import {VideoThumbnail} from '../../components/contents/thumbnailBox';
import axiosInstance from '../../utils/axiosInstance';
import NavigationBar from '../../components/tools/navigationBar';
import {regionList} from '../../constant/themes';
import {themeList} from '../../constant/themes';

export default function MyVideo({navigation, route}) {
  const [view, setView] = useState(0);
  const [videoData, setVideoData] = useState([]);
  const [selectedTagId, setSelectedTagId] = useState('');
  const [page, setPage] = useState(0);
  const [maxPage, setMaxPage] = useState(10);
  const userId = useSelector(state => state.USER);
  const [isEndOfScroll, setEndOfScroll] = useState(false);
  const scrollViewRef = useRef(null);
  const [tagId, setTagId] = useState([]);
  const [theme, setTheme] = useState([]);

  useEffect(() => {
    if (view === 0) {
      getSubscribedDATA();
    } else {
      getDataLike();
      setSelectedTagId('');
      setTheme([]);
    }
  }, [view]);

  useEffect(() => {}, []);

  useEffect(() => {
    if (theme.length > 0) {
      setSelectedTagId(theme[0].tagId);
    }
  }, [theme]);

  useEffect(() => {
    if (tagId.length > 0) {
      getMatchingThemeNames(tagId, regionList, themeList);
    }
  }, [tagId]);

  useEffect(() => {
    if (selectedTagId !== '') {
      getData(0);
    }
  }, [selectedTagId]);

  useEffect(() => {
    if (isEndOfScroll) {
      if (page < maxPage || page === maxPage) {
        setPage(prevPage => prevPage + 1);
      }
    }
  }, [isEndOfScroll]);

  useEffect(() => {
    if (page === 0) {
      getData(0);
    } else {
      getData(page);
    }
  }, [page]);

  function getMatchingThemeNames(tagIds, regionLists, themeLists) {
    const matchedRegionThemes = regionLists.filter(region =>
      tagIds.includes(region.tagId),
    );

    const matchedThemeListThemes = themeLists.filter(theme1 =>
      tagIds.includes(theme1.tagId),
    );
    setTheme([...matchedRegionThemes, ...matchedThemeListThemes]);
  }

  const Item = ({item}) => (
    <View style={styles.item}>
      <ImageBackground
        style={StyleSheet.absoluteFill}
        source={item.src}
        resizeMode="cover">
        {item.tagId === selectedTagId && (
          <View
            style={{
              ...StyleSheet.absoluteFill,
              backgroundColor: 'rgba(70,70,70, 0.8)',
            }}
          />
        )}

        <TouchableOpacity
          style={styles.touch}
          onPress={() => setSelectedTagId(item.tagId)}>
          <Text
            style={
              item.tagId === selectedTagId ? styles.selectedText : styles.text
            }>
            {item.tagName}
          </Text>
        </TouchableOpacity>
      </ImageBackground>
    </View>
  );

  const getData = async key => {
    try {
      let response;
      if (view === 0) {
        response = await axiosInstance.get(
          `content-slave-service/videos/tagId?q=${selectedTagId}&s=recent&page=${page}&size=10`,
        );
        console.log(
          'response.data.payload.videos : ',
          response.data.payload.videos,
        );
      } else {
        response = await axiosInstance.get(
          `personalized-service/${userId}/videos/liked?page=${page}&size=10`,
        );
      }

      if (key === 0) {
        setVideoData(response.data.payload.videos);
        setMaxPage(response.data.payload.totalPages);
      } else {
        setVideoData(prevVideoData => [
          ...prevVideoData,
          ...response.data.payload.videos,
        ]);
      }
    } catch (e) {
      console.log(e);
    }
  };

  const handleScroll = event => {
    const offsetY = event.nativeEvent.contentOffset.y;
    const contentHeight = event.nativeEvent.contentSize.height;
    const layoutHeight = event.nativeEvent.layoutMeasurement.height;

    if (offsetY + layoutHeight >= contentHeight - 1) {
      if (!isEndOfScroll) {
        setEndOfScroll(true);
      }
    } else {
      if (isEndOfScroll) {
        setEndOfScroll(false);
      }
    }
  };

  const getDataLike = async () => {
    try {
      const response = await axiosInstance.get(
        `personalized-service/${userId}/videos/liked?page=0&size=10`,
      );
      const likedVideoIds = response.data.payload.likedVideos.likedVideoIdList;
      if (likedVideoIds && likedVideoIds.length > 0) {
        await getLikedVideo(likedVideoIds);
      }
    } catch (e) {
      if (e.response && e.response.status === 404) {
        console.log(e.response.status);
        setVideoData([]);
      }
    }
  };

  const getLikedVideo = async likedVideoIds => {
    try {
      const videoIdString = likedVideoIds.join(',');
      const response = await axiosInstance.get(
        `content-slave-service/videos/videoIds?q=${videoIdString}`,
      );
      setVideoData(response.data.payload.videos);
    } catch (e) {
      console.log(e);
    }
  };

  const handlepress = e => {
    if (view === e) {
      return;
    } else {
      setView(e);
    }
  };

  const getSubscribedDATA = async () => {
    try {
      const response = await axiosInstance.get(
        `personalized-service/${userId}/tags/subscribed`,
      );
      setTagId(response.data.payload.subscribedTagList);
    } catch (e) {
      console.error(e);
    }
  };

  return (
    <View style={styles.container}>
      <View style={styles.contentsContainer}>
        <View style={styles.buttonContainer}>
          <TouchableOpacity
            style={[
              styles.button,
              {borderTopLeftRadius: 10, borderBottomLeftRadius: 10},
              view === 0
                ? {backgroundColor: '#21C99B'}
                : {backgroundColor: 'white'},
            ]}
            onPress={() => handlepress(0)}>
            <Text
              style={[styles.buttonText, view === 0 ? {color: 'white'} : {}]}>
              태그
            </Text>
          </TouchableOpacity>
          <TouchableOpacity
            style={[
              styles.button,
              {
                borderTopRightRadius: 10,
                borderBottomRightRadius: 10,
              },
              view === 1
                ? {backgroundColor: '#21C99B'}
                : {backgroundColor: 'white'},
            ]}
            onPress={() => handlepress(1)}>
            <Text
              style={[styles.buttonText, view === 1 ? {color: 'white'} : {}]}>
              좋아요
            </Text>
          </TouchableOpacity>
        </View>
        <View style={styles.horizontalContainer}>
          {view === 0 &&
            (theme.length > 0 ? (
              <ScrollView
                horizontal
                showsHorizontalScrollIndicator={true}
                contentContainerStyle={{
                  alignItems: 'center',
                  gap: 20,
                  paddingHorizontal: 15,
                }}>
                <View style={styles.areaScroll}>
                  {theme.map((e, i) => {
                    return <Item key={i} item={e} />;
                  })}
                </View>
              </ScrollView>
            ) : (
              <Text style={styles.alertText}>태그를 구독해주세요.</Text>
            ))}
        </View>

        <ScrollView
          ref={scrollViewRef}
          onScroll={handleScroll}
          style={styles.videoContainer}
          contentContainerStyle={{alignItems: 'center'}}>
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
          ) : view === 0 ? (
            <Text style={styles.alertText}>해당 태그에 영상이 없습니다.</Text>
          ) : (
            <Text style={styles.alertText}>좋아요한 영상이 없습니다.</Text>
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
  videoContainer: {
    marginTop: 5,
    width: '100%',
  },
  spinnerContainer: {
    marginTop: 50,
  },

  buttonContainer: {
    flexDirection: 'row',
    marginTop: 15,
    paddingHorizontal: 15,
  },

  button: {
    justifyContent: 'center',
    alignItems: 'center',
    width: 75,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 2,
    paddingVertical: 5,
    paddingHorizontal: 12,
  },
  buttonText: {
    fontSize: 16,
    fontWeight: '600',
    letterSpacing: 1,
  },
  horizontalContainer: {marginTop: 15},

  item: {
    borderRadius: 50,
    overflow: 'hidden',
    width: 60,
    height: 60,
    marginRight: 14,
    backgroundColor: 'white',
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 5,
  },
  touch: {
    width: '100%',
    height: '100%',
    alignItems: 'center',
    justifyContent: 'center',
  },

  areaScroll: {
    flexDirection: 'row',
    paddingVertical: 10,
  },
  text: {
    color: 'white',
    fontWeight: '700',
    fontSize: 14,
    letterSpacing: 1,
  },
  selectedText: {
    color: 'rgba(230,230,230, 0.8)',
    fontWeight: '700',
    fontSize: 14,
    letterSpacing: 1,
  },
  alertText: {
    fontSize: 23,
    marginTop: 30,
    textAlign: 'center',
  },

  videoThumbnailContainer: {
    alignItems: 'center',
    paddingTop: 17,
    paddingBottom: 10,
    marginVertical: 7,
    backgroundColor: 'white',
    width: '92%',
  },
});
