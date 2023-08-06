/* eslint-disable react/no-unstable-nested-components */
import React, {useRef, useState} from 'react';
import {
  ScrollView,
  TouchableOpacity,
  View,
  Text,
  StyleSheet,
  ImageBackground,
  Image,
} from 'react-native';
import Toolbar from '../components/toolBar';
import {regionList} from '../constant';
import {themeList} from '../constant';

export default function Home({navigation, route}) {
  const [scrollPosition, setScrollPosition] = useState(0);
  const [scrollPosition2, setScrollPosition2] = useState(0);
  const scrollViewRef = useRef();
  const scrollViewRef2 = useRef();

  const handleIconPress = (direction, scrollRef, setScrollPos) => {
    let currentScrollPosition =
      scrollRef.current?.getScrollableNode().scrollLeft || 0;

    if (direction === 'left') {
      let newScrollPosition = currentScrollPosition - 200;
      scrollRef.current.scrollTo({
        x: newScrollPosition,
        y: 0,
        animated: true,
      });
      setScrollPos(newScrollPosition);
    } else if (direction === 'right') {
      let newScrollPosition = currentScrollPosition + 200;
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
              handleIconPress('left', scrollViewRef, setScrollPosition)
            }>
            <Image source={require('../assets/themeImg/arrow-left.png')} />
          </TouchableOpacity>
          <ScrollView
            horizontal
            showsHorizontalScrollIndicator={true}
            onScroll={event => handleScroll(event, setScrollPosition)}
            ref={scrollViewRef}
            style={styles.horizontalScrollContainer}>
            <View style={styles.areaScroll}>
              {regionList.map((e, i) => {
                return <Item key={i} item={e} />;
              })}
            </View>
          </ScrollView>
          <TouchableOpacity
            style={styles.scrollbutton}
            onPress={() =>
              handleIconPress('right', scrollViewRef, setScrollPosition)
            }>
            <Image source={require('../assets/themeImg/arrow-right.png')} />
          </TouchableOpacity>
        </View>
        <View style={styles.textContainer}>
          <Text style={styles.title}>인기 테마</Text>
        </View>

        <View style={styles.areaScrollContainer}>
          <TouchableOpacity
            style={styles.scrollbutton}
            onPress={() =>
              handleIconPress('left', scrollViewRef2, setScrollPosition2)
            }>
            <Image source={require('../assets/themeImg/arrow-left.png')} />
          </TouchableOpacity>
          <ScrollView
            horizontal
            showsHorizontalScrollIndicator={false}
            onScroll={event => handleScroll(event, setScrollPosition2)}
            ref={scrollViewRef2}
            style={styles.horizontalScrollContainer}>
            <View style={styles.areaScroll}>
              {themeList.map((e, i) => {
                return <Item key={i} item={e} />;
              })}
            </View>
          </ScrollView>
          <TouchableOpacity
            style={styles.scrollbutton}
            onPress={() =>
              handleIconPress('right', scrollViewRef2, setScrollPosition2)
            }>
            <Image source={require('../assets/themeImg/arrow-right.png')} />
          </TouchableOpacity>
        </View>
        <View style={styles.textContainer}>
          <Text style={styles.title}>인기 상품</Text>
        </View>
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
    paddingHorizontal: 10,
    alignItems: 'center',
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
  textContainer: {
    marginTop: 5,
    alignItems: 'flex-start',
    width: 360,
  },

  areaScroll: {
    flexDirection: 'row',
  },

  areaScrollContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: 'white',
    borderRadius: 10,
    height: 110,
    width: 350,
    marginTop: 15,
    marginBottom: 5,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 5,
  },
  horizontalScrollContainer: {
    marginTop: 7,
  },
  item: {
    borderRadius: 10,
    overflow: 'hidden',
    width: 95,
    height: 95,
    marginRight: 14,
    backgroundColor: 'white',
    marginBottom: 15,
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
    marginTop: 20,
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
