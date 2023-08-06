/* eslint-disable react-native/no-inline-styles */
/* eslint-disable react/no-unstable-nested-components */
import React, {useRef, useState} from 'react';

import {
  ScrollView,
  TouchableOpacity,
  View,
  Text,
  StyleSheet,
  FlatList,
  ImageBackground,
  Image,
} from 'react-native';
import {themeList} from '../constant';
import {regionList} from '../constant';

export default function ThemeSelect({navigation}) {
  const [selectedItem, setSelectedItem] = useState([]);
  const [scrollPosition, setScrollPosition] = useState(0);
  const scrollViewRef = useRef();

  const handlePress = e => {
    if (selectedItem.includes(e)) {
      setSelectedItem(() => selectedItem.filter(el => el !== e));
    } else {
      setSelectedItem(() => [...selectedItem, e]);
    }
  };

  const handleIconPress = direction => {
    if (direction === 'left') {
      let newScrollPosition = scrollPosition - 200;
      scrollViewRef.current.scrollTo({
        x: newScrollPosition,
        y: 0,
        animated: true,
      });
    } else if (direction === 'right') {
      let newScrollPosition = scrollPosition + 200;
      scrollViewRef.current.scrollTo({
        x: newScrollPosition,
        y: 0,
        animated: true,
      });
    }
  };

  const handleScroll = event => {
    setScrollPosition(event.nativeEvent.contentOffset.x);
  };
  const Item = ({item}) => {
    const isItemSelected = selectedItem.includes(item.tagId);
    return (
      <View style={styles.item}>
        <ImageBackground
          style={StyleSheet.absoluteFill}
          source={item.src}
          resizeMode="cover">
          {isItemSelected && (
            <View
              style={{
                ...StyleSheet.absoluteFill,
                backgroundColor: 'rgba(236, 255, 251, 1)',
              }}
            />
          )}
          <TouchableOpacity
            style={styles.touch}
            onPress={() => handlePress(item.tagId)}>
            <Text style={isItemSelected ? styles.selectedText : styles.text}>
              {item.tagName}
            </Text>
          </TouchableOpacity>
        </ImageBackground>
      </View>
    );
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>관심있는 태그를 구독해보세요.</Text>
      <View style={styles.contentsContainer}>
        <View style={styles.textContainer}>
          <Text style={styles.littleTitle}>지역</Text>
        </View>
        <View style={styles.areaScrollContainer}>
          <TouchableOpacity
            style={styles.scrollbutton}
            onPress={() => handleIconPress('left')}>
            <Image source={require('../assets/themeImg/arrow-left.png')} />
          </TouchableOpacity>
          <ScrollView
            horizontal
            showsHorizontalScrollIndicator={true}
            onScroll={handleScroll}
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
            onPress={() => handleIconPress('right')}>
            <Image source={require('../assets/themeImg/arrow-right.png')} />
          </TouchableOpacity>
        </View>
        <View style={styles.textContainer}>
          <Text style={styles.littleTitle}>테마</Text>
        </View>

        <FlatList
          style={styles.themeScrollcontainer}
          contentContainerStyle={styles.itemContainer}
          data={themeList}
          renderItem={({item}) => <Item item={item} />}
          numColumns={3}
        />
      </View>
      <TouchableOpacity
        style={styles.button}
        onPress={() => navigation.navigate('Home')}>
        <Text style={styles.buttonText}>Start</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F2F8FF',
  },
  contentsContainer: {
    flex: 1,
    paddingHorizontal: 10,
    alignItems: 'center',
  },
  areaScrollContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: 'white',
    borderRadius: 10,
    height: 110,
    width: 350,
    marginTop: 15,
    marginBottom: 30,
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
  textContainer: {
    marginTop: 5,
    alignItems: 'flex-start',
    width: 360,
  },
  areaScroll: {
    flexDirection: 'row',
    paddingLeft: 3,
  },
  itemContainer: {paddingBottom: 20, paddingHorizontal: 19},
  themeScrollcontainer: {
    marginTop: 15,
    width: 350,
    backgroundColor: 'white',
    borderTopRightRadius: 10,
    borderTopLeftRadius: 10,
    paddingTop: 20,
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
  item: {
    borderRadius: 10,
    overflow: 'hidden',
    width: 95,
    height: 95,
    marginRight: 14,
    marginBottom: 15,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 7,
  },

  touch: {
    width: '100%',
    height: '100%',
    alignItems: 'center',
    justifyContent: 'center',
  },

  title: {
    fontSize: 25,
    fontWeight: 'bold',
    color: '#545454',
    marginLeft: 20,
    marginVertical: 15,
  },
  littleTitle: {
    fontSize: 20,
    color: '#4D4D4D',
    marginLeft: 15,
    fontWeight: '700',
    fontStyle: 'italic',
    width: 100,
  },
  text: {
    fontWeight: 'bold',
    fontStyle: 'italic',
    color: 'white',
    fontSize: 21,
    letterSpacing: 3,
    padding: 2,
    textShadowOffset: {width: 0, height: 2},
    textShadowRadius: 2,
    textShadowColor: 'gray',
  },
  selectedText: {
    fontWeight: 'bold',
    fontStyle: 'italic',
    color: 'black',
    fontSize: 21,
    letterSpacing: 3,
    padding: 2,
  },
  button: {
    flex: 0.08,
    backgroundColor: '#FFF9EA',
    justifyContent: 'center',
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 5,
  },
  buttonText: {
    fontStyle: 'italic',
    fontWeight: 'bold',
    fontSize: 20,
    letterSpacing: 3,
    color: 'black',
  },
});
