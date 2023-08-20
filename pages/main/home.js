/* eslint-disable react-native/no-inline-styles */
/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable react/no-unstable-nested-components */
import React, {useRef, useEffect, useState} from 'react';
import globalStyles from '../../constant/styles';

import {
  ScrollView,
  TouchableOpacity,
  View,
  Text,
  StyleSheet,
  ImageBackground,
  ActivityIndicator as Spinner,
} from 'react-native';
import {regionList, themeList} from '../../constant/themes';
import {useDispatch, useSelector} from 'react-redux';
import {setTag, setNickName} from '../../storage/actions';
import axiosInstance from '../../utils/axiosInstance';
import NavigationBar from '../../components/tools/navigationBar';
import {VideoThumbnail} from '../../components/contents/thumbnailBox';

export default function Home({navigation, route}) {
  const [viewedTag, setViewedTag] = useState([]);
  const [recommendedVideos, setRecommendedVideos] = useState([]);
  const scrollViewRef = useRef();
  const scrollViewRef2 = useRef();
  const [themeRank, setThemeRank] = useState([]);
  const [selectedTag, setSelectedTag] = useState('');
  const [regionRank, setRegionRank] = useState([]);
  const [page, setPage] = useState(0);
  const [isEndOfScroll, setEndOfScroll] = useState(false);
  const userId = useSelector(state => state.USER);
  const dispatch = useDispatch();

  useEffect(() => {
    getSubscribedData();
    getViewData();
    getUserData();
    getThemeRank();
  }, []);

  useEffect(() => {
    if (viewedTag.length === 0) {
      const randomTags = getRandomTags();
      getRecommandVideos(randomTags);
    } else {
      getRecommandVideos(viewedTag);
    }
  }, [viewedTag, page]);

  useEffect(() => {
    if (selectedTag !== '') {
      getRecommandVideos(selectedTag);
    } else {
      return;
    }
  }, [selectedTag]);

  const getUserData = async () => {
    try {
      const response = await axiosInstance.get(`user-service/users/${userId}`);
      console.log('회원정보 조회 : ', response.data.payload);
      dispatch(setNickName(response.data.payload.nickname));
    } catch (e) {
      console.error(e);
    }
  };

  const getSubscribedData = async () => {
    try {
      const response = await axiosInstance.get(
        `personalized-service/${userId}/tags/subscribed`,
      );
      dispatch(setTag(response.data.payload.subscribedTagList));
    } catch (e) {
      console.error(e);
    }
  };

  const getRecommandVideos = async tags => {
    try {
      console.log('tags :', tags);

      if (selectedTag === '') {
        const tagIds = tags.map(tag => tag.tagId).join(',');
        const response = await axiosInstance.get(
          `content-slave-service/videos/tagIds?q=${tagIds}&u=${userId}&page=${page}`,
        );
        const newVideos = [
          ...recommendedVideos,
          ...response.data.payload.videos,
        ];
        console.log(newVideos.length);
        const uniqueVideos = Array.from(
          new Set(newVideos.map(v => v.videoId)),
        ).map(id => {
          return newVideos.find(v => v.videoId === id);
        });

        setRecommendedVideos(uniqueVideos);
      } else {
        const response = await axiosInstance.get(
          `content-slave-service/videos/tagIds?q=${tags}&u=${userId}&page=${page}`,
        );
        setRecommendedVideos(response.data.payload.videos);
      }
    } catch (e) {
      console.log(e);
    }
  };

  const getThemeRank = async () => {
    try {
      const response = await axiosInstance.get(
        'statistics-service/rank/tags/theme',
      );

      setThemeRank(response.data.payload.tagRank);
      const response2 = await axiosInstance.get(
        'statistics-service/rank/tags/region',
      );
      setRegionRank(response2.data.payload.tagRank);
    } catch (e) {
      console.log(e);
    }
  };

  const getRandomTags = () => {
    const combinedList = [...regionList, ...themeList];
    combinedList.sort(() => 0.5 - Math.random());
    return combinedList.slice(0, 5);
  };

  const getViewData = async () => {
    try {
      console.log('userId in getViewData : ', userId);
      const response = await axiosInstance.get(
        `personalized-service/${userId}/tags/viewed`,
      );
      console.log('response of getViewDATA : ', response.data.payload);
      setViewedTag(response.data.payload.viewedTags);
    } catch (e) {
      console.log(e);
    }
  };

  const handleScrollend = event => {
    const offsetY = event.nativeEvent.contentOffset.y;
    const contentHeight = event.nativeEvent.contentSize.height;
    const layoutHeight = event.nativeEvent.layoutMeasurement.height;

    if (offsetY + layoutHeight >= contentHeight) {
      if (!isEndOfScroll) {
        setEndOfScroll(true);
        setPage(prevPage => prevPage + 1);
      }
    } else {
      if (isEndOfScroll) {
        setEndOfScroll(false);
      }
    }
  };

  const Item = ({item}) => (
    <View style={styles.item}>
      <ImageBackground
        style={StyleSheet.absoluteFill}
        source={item.src}
        resizeMode="cover">
        {item.tagId === selectedTag && (
          <View
            style={{
              ...StyleSheet.absoluteFill,
              backgroundColor: 'rgba(70,70,70, 0.8)',
            }}
          />
        )}
        <TouchableOpacity
          style={styles.touch}
          onPress={() => {
            if (item.tagId === selectedTag) {
              setSelectedTag('');
            } else {
              setSelectedTag(item.tagId);
            }
          }}>
          <Text
            style={
              item.tagId === selectedTag ? styles.selectedText : styles.text
            }>
            {item.tagName}
          </Text>
        </TouchableOpacity>
      </ImageBackground>
    </View>
  );

  return (
    <View style={styles.container}>
      <ScrollView
        onScroll={handleScrollend}
        style={styles.scrollContainer}
        contentContainerStyle={{alignItems: 'center'}}>
        <View style={styles.contentsContainer}>
          <View style={styles.textContainer}>
            <Text style={styles.title}>인기 지역</Text>
          </View>
          <View style={styles.areaScrollContainer}>
            <ScrollView
              horizontal
              showsHorizontalScrollIndicator={false}
              ref={scrollViewRef}>
              <View style={styles.areaScroll}>
                {regionList
                  .filter(region =>
                    regionRank.some(rank => rank.tagId === region.tagId),
                  )
                  .map((item, index) => {
                    return <Item key={index} item={item} />;
                  })}
              </View>
            </ScrollView>
          </View>
          <View style={styles.textContainer}>
            <Text style={styles.title}>인기 테마</Text>
          </View>

          <View style={styles.areaScrollContainer}>
            <ScrollView
              horizontal
              showsHorizontalScrollIndicator={false}
              ref={scrollViewRef2}>
              <View style={styles.areaScroll}>
                {themeList
                  .filter(theme =>
                    themeRank.some(rank => rank.tagId === theme.tagId),
                  )
                  .map((item, index) => {
                    return <Item key={index} item={item} />;
                  })}
              </View>
            </ScrollView>
          </View>
          <View style={[styles.textContainer, {marginBottom: 9}]}>
            <Text style={styles.title}>추천 영상</Text>
          </View>

          {recommendedVideos.length > 0 ? (
            recommendedVideos.map((e, i) => {
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
        </View>
      </ScrollView>

      <NavigationBar route={route} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'space-between',
    paddingTop: 4,
  },
  spinnerContainer: {
    marginTop: 50,
  },
  contentsContainer: {
    flex: 1,
    paddingHorizontal: 10,
    alignItems: 'center',
    width: '100%',
  },
  textContainer: {
    alignItems: 'flex-start',
    width: 363,
    marginTop: 10,
  },

  areaScroll: {
    flexDirection: 'row',
    alignItems: 'center',

    paddingTop: 8,
    paddingBottom: 10,
  },

  areaScrollContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    width: '95%',
    paddingVertical: 2,

    marginBottom: 7,
  },

  scrollContainer: {
    width: '100%',
  },

  videoThumbnailContainer: {
    backgroundColor: 'white',
    alignItems: 'center',
    paddingTop: 17,
    paddingBottom: 10,
    marginVertical: 5,
    width: '95%',
  },

  item: {
    borderRadius: 20,
    overflow: 'hidden',
    width: 80,
    height: 60,
    marginRight: 10,
  },

  scrollbutton: {
    width: 20,
  },
  title: {
    fontSize: 23,
    fontFamily: 'AppleSDGothicNeoB00',
    color: 'black',
    marginLeft: 15,
    fontWeight: '600',
    width: 120,
  },

  touch: {
    width: '100%',
    height: '100%',
    alignItems: 'flex-start',
    justifyContent: 'flex-end',
  },
  text: {
    fontWeight: '600',
    color: 'white',
    fontSize: 15,
    letterSpacing: 3,
    paddingLeft: 10,
    paddingBottom: 8,
  },
  selectedText: {
    color: 'rgba(230,230,230, 0.8)',
    fontWeight: '600',
    fontSize: 15,
    letterSpacing: 3,
    paddingLeft: 10,
    paddingBottom: 8,
  },
});
