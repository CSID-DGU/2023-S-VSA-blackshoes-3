/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable react/no-unstable-nested-components */
import React, {useRef, useEffect, useState} from 'react';
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
import {setTag} from '../../storage/actions';
import axiosInstance from '../../utils/axiosInstance';
import NavigationBar from '../../components/tools/navigationBar';

export default function Home({navigation, route}) {
  const [scrollPosition, setScrollPosition] = useState(0);
  const [scrollPosition2, setScrollPosition2] = useState(0);
  const scrollViewRef = useRef();
  const scrollViewRef2 = useRef();
  const userId = useSelector(state => state.USER);
  const dispatch = useDispatch();

  useEffect(() => {
    getData();
  }, []);

  const getData = async () => {
    try {
      const response = await axiosInstance.get(
        `personalized-service/${userId}/tags/subscribed`,
      );

      console.log('hi tags : ', response.data.payload.subscribedTagList);

      dispatch(setTag(response.data.payload.subscribedTagList));
    } catch (e) {
      console.error(e);
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
            <Image source={require('../../assets/themeImg/arrow-right.png')} />
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
            <Image source={require('../../assets/themeImg/arrow-right.png')} />
          </TouchableOpacity>
        </View>
        <View style={styles.textContainer}>
          <Text style={styles.title}>추천 영상</Text>
        </View>
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
    paddingHorizontal: 10,
    alignItems: 'center',
  },
  textContainer: {
    alignItems: 'flex-start',
    width: 360,
    marginTop: 15,
  },

  areaScroll: {
    flexDirection: 'row',
    alignItems: 'center',

    paddingVertical: 10,
  },

  areaScrollContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: 'white',
    borderRadius: 10,
    width: 350,
    paddingVertical: 2,
    marginTop: 15,
    marginBottom: 7,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 5,
  },

  item: {
    borderRadius: 50,
    overflow: 'hidden',
    width: 75,
    height: 75,
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
    fontSize: 25,
    color: '#4D4D4D',
    marginLeft: 15,
    fontWeight: '700',
    fontStyle: 'italic',
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
