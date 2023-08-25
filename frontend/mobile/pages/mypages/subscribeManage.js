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
import {useDispatch, useSelector} from 'react-redux';
import {useFocusEffect} from '@react-navigation/native';
import {setTag} from '../../storage/actions';

export default function ThemeSelectEach() {
  const userId = useSelector(state => state.USER);
  const tag = useSelector(state => state.TAG);
  const [selectedItem, setSelectedItem] = useState(tag);

  const dispatch = useDispatch();

  useFocusEffect(
    React.useCallback(() => {
      return () => {
        dispatch(setTag(selectedItem));
      };
    }, [selectedItem, dispatch]),
  );

  const handlePress = e => {
    if (selectedItem.includes(e)) {
      unSubscribe(e);
    } else {
      subscribe(e);
    }
  };

  const unSubscribe = async e => {
    try {
      const response = await axiosInstance.delete(
        `personalized-service/${userId}/tags/subscribed/${e}`,
      );
      setSelectedItem(() => selectedItem.filter(el => el !== e));

      console.log(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  const subscribe = async e => {
    try {
      const response = await axiosInstance.post(
        `personalized-service/${userId}/tags/subscribed`,
        {
          tagId: e,
        },
      );
      console.log(response.data);
      setSelectedItem(() => [...selectedItem, e]);
    } catch (error) {
      console.log(error);
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
              {/* <Text style={styles.text}> */}
              {item.tagName}
            </Text>
          </TouchableOpacity>
        </ImageBackground>
      </View>
    );
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>구독 관리</Text>
      <View style={styles.contentsContainer}>
        <View style={styles.textContainer}>
          <Text style={styles.littleTitle}>지역</Text>
        </View>
        <View style={styles.areaScrollContainer}>
          <ScrollView
            horizontal
            showsHorizontalScrollIndicator={false}
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
});
