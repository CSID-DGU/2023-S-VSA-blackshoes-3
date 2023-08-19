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
  Image,
} from 'react-native';
import {regionList} from '../../constant/themes';
import {themeList} from '../../constant/themes';
import {useDispatch, useSelector} from 'react-redux';
import {setTag, setNickName} from '../../storage/actions';
import axiosInstance from '../../utils/axiosInstance';
import NavigationBar from '../../components/tools/navigationBar';
import {VideoThumbnail} from '../../components/contents/thumbnailBox';

export default function Home({navigation, route}) {
  const [scrollPosition, setScrollPosition] = useState(0);
  const [scrollPosition2, setScrollPosition2] = useState(0);
  const [viewedTag, setViewedTag] = useState([]);
  const [recommendedVideos, setRecommendedVideos] = useState([]);
  const scrollViewRef = useRef();
  const scrollViewRef2 = useRef();

  const [page, setPage] = useState(0);
  const [isEndOfScroll, setEndOfScroll] = useState(false);

  const userId = useSelector(state => state.USER);
  const dispatch = useDispatch();

  useEffect(() => {
    getSubscribedData();
    getViewData();
    getUserData();
  }, []);

  useEffect(() => {
    if (viewedTag.length === 0) {
      const randomTags = getRandomTags();
      getRecommandVideos(randomTags);
    } else {
      getRecommandVideos(viewedTag);
    }
  }, [viewedTag, page]);

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
      const tagIds = tags.map(tag => tag.tagId).join(',');
      const response = await axiosInstance.get(
        `content-slave-service/videos/tagIds?q=${tagIds}&u=${userId}&page=${page}`,
      );
      console.log('in getRecommandVideos : ', response.data.payload);
      setRecommendedVideos(prevVideos => [
        ...prevVideos,
        ...response.data.payload.videos,
      ]);
    } catch (e) {
      console.log(e);
    }
  };

  // const getRecommandVideos = async () => {
  //   try {
  //     console.log('viewTag array : ', viewedTag);
  //     const response = await axiosInstance.get(
  //       `content-slave-service/videos/tagIds?q=${viewedTag[0].tagId},${viewedTag[1].tagId},${viewedTag[2].tagId},${viewedTag[3].tagId},${viewedTag[4].tagId}&u=${userId}&page=0`,
  //     );
  //     setRecommendedVideos(response.data.payload.videos);
  //   } catch (e) {
  //     console.log(e);
  //   }
  // };

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

  const handleIconPress = (direction, scrollRef, setScrollPos, currentPos) => {
    if (direction === 'left') {
      let newScrollPosition = currentPos - 250;
      scrollRef.current.scrollTo({
        x: newScrollPosition,
        y: 0,
        animated: true,
      });
      setScrollPos(newScrollPosition);
    } else if (direction === 'right') {
      let newScrollPosition = currentPos + 250;
      scrollRef.current.scrollTo({
        x: newScrollPosition,
        y: 0,
        animated: true,
      });
      setScrollPos(newScrollPosition);
    }
  };

  const handleScroll = (event, setScrollPos) => {
    setScrollPos(event.nativeEvent.contentOffset.x);
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
        <TouchableOpacity
          style={styles.touch}
          onPress={() => navigation.navigate('ThemeVideo', {item})}>
          <Text style={styles.text}>{item.tagName}</Text>
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
            <TouchableOpacity
              style={styles.scrollbutton}
              onPress={() =>
                handleIconPress(
                  'left',
                  scrollViewRef,
                  setScrollPosition,
                  scrollPosition,
                )
              }>
              <Image source={require('../../assets/themeImg/arrow-left.png')} />
            </TouchableOpacity>
            <ScrollView
              horizontal
              showsHorizontalScrollIndicator={true}
              onScroll={event => handleScroll(event, setScrollPosition)}
              ref={scrollViewRef}>
              <View style={styles.areaScroll}>
                {regionList.map((e, i) => {
                  return <Item key={i} item={e} />;
                })}
              </View>
            </ScrollView>
            <TouchableOpacity
              style={styles.scrollbutton}
              onPress={() =>
                handleIconPress(
                  'right',
                  scrollViewRef,
                  setScrollPosition,
                  scrollPosition,
                )
              }>
              <Image
                source={require('../../assets/themeImg/arrow-right.png')}
              />
            </TouchableOpacity>
          </View>
          <View style={styles.textContainer}>
            <Text style={styles.title}>인기 테마</Text>
          </View>

          <View style={styles.areaScrollContainer}>
            <TouchableOpacity
              style={styles.scrollbutton}
              onPress={() =>
                handleIconPress(
                  'left',
                  scrollViewRef2,
                  setScrollPosition2,
                  scrollPosition2,
                )
              }>
              <Image source={require('../../assets/themeImg/arrow-left.png')} />
            </TouchableOpacity>
            <ScrollView
              horizontal
              showsHorizontalScrollIndicator={false}
              onScroll={event => handleScroll(event, setScrollPosition2)}
              ref={scrollViewRef2}>
              <View style={styles.areaScroll}>
                {themeList.map((e, i) => {
                  return <Item key={i} item={e} />;
                })}
              </View>
            </ScrollView>
            <TouchableOpacity
              style={styles.scrollbutton}
              onPress={() =>
                handleIconPress(
                  'right',
                  scrollViewRef2,
                  setScrollPosition2,
                  scrollPosition2,
                )
              }>
              <Image
                source={require('../../assets/themeImg/arrow-right.png')}
              />
            </TouchableOpacity>
          </View>
          <View style={styles.textContainer}>
            <Text style={styles.title}>추천 영상</Text>
          </View>

          {recommendedVideos.length > 0 &&
            recommendedVideos.map((e, i) => {
              return (
                <TouchableOpacity
                  style={styles.videoThumbnailContainer}
                  key={i}
                  onPress={() => navigation.navigate('Play', {video: e})}>
                  <VideoThumbnail key={i} video={e} navigation={navigation} />
                </TouchableOpacity>
              );
            })}
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
  },
  contentsContainer: {
    flex: 1,
    paddingHorizontal: 10,
    alignItems: 'center',
    width: '100%',
  },
  textContainer: {
    alignItems: 'flex-start',
    width: 360,
    marginTop: 10,
  },

  areaScroll: {
    flexDirection: 'row',
    alignItems: 'center',

    paddingVertical: 10,
  },

  areaScrollContainer: {
    backgroundColor: '#DEDEDE',
    flexDirection: 'row',
    alignItems: 'center',
    width: '95%',
    paddingVertical: 2,
    marginTop: 15,
    marginBottom: 7,
  },

  scrollContainer: {
    width: '100%',
  },

  videoThumbnailContainer: {
    backgroundColor: '#DEDEDE',
    alignItems: 'center',
    paddingTop: 17,
    paddingBottom: 10,
    marginVertical: 7,

    width: '95%',
  },

  item: {
    borderRadius: 50,
    overflow: 'hidden',
    width: 65,
    height: 65,
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

  scrollbutton: {
    width: 20,
  },
  title: {
    fontSize: 23,
    fontFamily: 'AppleSDGothicNeoB00',

    color: '#4D4D4D',
    marginLeft: 15,
    fontWeight: '700',
    width: 120,
  },

  touch: {
    width: '100%',
    height: '100%',
    alignItems: 'center',
    justifyContent: 'center',
  },
  text: {
    fontWeight: 'bold',
    fontStyle: 'italic',
    color: 'white',
    fontSize: 21,
    letterSpacing: 3,
    padding: 2,
    borderRadius: 5,
    textShadowOffset: {width: 0, height: 2},
    textShadowRadius: 2,
    textShadowColor: 'gray',
  },
});
