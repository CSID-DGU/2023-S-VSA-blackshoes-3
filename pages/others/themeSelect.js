/* eslint-disable react-native/no-inline-styles */
/* eslint-disable react/no-unstable-nested-components */
import React, {useState} from 'react';

import {
  ScrollView,
  TouchableOpacity,
  View,
  Text,
  StyleSheet,
  FlatList,
  ImageBackground,
} from 'react-native';
import {themeList} from '../../constant/themes';

import {regionList} from '../../constant/themes';
import axiosInstance from '../../utils/axiosInstance';
import {useSelector} from 'react-redux';

export default function ThemeSelect({navigation}) {
  const [selectedItem, setSelectedItem] = useState([]);

  const userId = useSelector(state => state.USER);

  const handlePress = e => {
    if (selectedItem.includes(e)) {
      setSelectedItem(() => selectedItem.filter(el => el !== e));
    } else {
      setSelectedItem(() => [...selectedItem, e]);
    }
  };

  const submitData = async () => {
    if (selectedItem.length !== 0) {
      console.log('hi');
      try {
        const response = await axiosInstance.post(
          `personalized-service/${userId}/tags/subscribed/init`,
          {
            tagIdList: selectedItem,
          },
        );
        navigation.navigate('Home');

        console.log(response);
      } catch (e) {
        console.error(e);
      }
    } else {
      navigation.navigate('Home');
    }
  };

  const Item = ({item, type}) => {
    const isItemSelected = selectedItem.includes(item.tagId);
    return (
      <View style={type === 0 ? styles.item : styles.themeItem}>
        <ImageBackground
          style={StyleSheet.absoluteFill}
          source={item.src}
          resizeMode="cover">
          {isItemSelected && (
            <View
              style={{
                ...StyleSheet.absoluteFill,
                backgroundColor: 'rgba(70,70,70, 0.8)',
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
          <ScrollView
            horizontal
            showsHorizontalScrollIndicator={true}
            style={styles.horizontalScrollContainer}>
            <View style={styles.areaScroll}>
              {regionList.map((e, i) => {
                return <Item key={i} item={e} type={0} />;
              })}
            </View>
          </ScrollView>
        </View>
        <View style={styles.textContainer}>
          <Text style={styles.littleTitle}>테마</Text>
        </View>

        <FlatList
          style={styles.themeScrollcontainer}
          contentContainerStyle={styles.itemContainer}
          data={themeList}
          renderItem={({item}) => <Item item={item} type={1} />}
          numColumns={3}
        />
      </View>
      <TouchableOpacity style={styles.button} onPress={submitData}>
        <Text style={styles.buttonText}>Start</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  contentsContainer: {
    flex: 1,
    paddingHorizontal: 5,
    alignItems: 'center',
  },
  areaScrollContainer: {
    flexDirection: 'row',
    alignItems: 'center',

    borderRadius: 10,
    height: 110,

    marginTop: 10,
    marginBottom: 30,
  },
  horizontalScrollContainer: {
    marginHorizontal: 15,
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
  itemContainer: {
    paddingBottom: 20,
    paddingHorizontal: 23,
  },
  themeScrollcontainer: {
    width: '100%',
    paddingTop: 10,
  },
  scrollbutton: {
    width: 20,
  },
  item: {
    borderRadius: 20,
    overflow: 'hidden',
    width: 85,
    height: 100,
    marginRight: 6,
    marginBottom: 15,
  },
  themeItem: {
    borderRadius: 20,
    overflow: 'hidden',
    width: 100,
    height: 85,
    marginBottom: 15,
    marginRight: 15,
  },
  touch: {
    width: '100%',
    height: '100%',
    justifyContent: 'flex-end',
    alignItems: 'flex-start',
    padding: 8,
  },
  title: {
    fontSize: 25,
    fontWeight: 'bold',
    color: 'black',
    marginLeft: 20,
    marginVertical: 15,
  },
  littleTitle: {
    fontSize: 20,
    color: '#4D4D4D',
    marginLeft: 15,
    fontWeight: '700',
    width: 100,
  },
  text: {
    fontWeight: '600',
    color: 'white',
    fontSize: 17,
    letterSpacing: 3,
    padding: 2,
  },
  selectedText: {
    fontWeight: '600',
    color: 'rgba(230,230,230, 0.8)',
    fontSize: 17,
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
    fontWeight: 'bold',
    fontSize: 20,
    letterSpacing: 3,
    color: 'black',
  },
});
