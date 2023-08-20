/* eslint-disable react-hooks/exhaustive-deps */
import React, {useCallback} from 'react';

import {
  View,
  StyleSheet,
  Image,
  Text,
  TouchableOpacity,
  TextInput,
  FlatList,
} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import {useNavigation} from '@react-navigation/native';

import {SearchTypeModal} from '../modals/searchTypeModal';
import {debounce} from 'lodash';

import axiosInstance from '../../utils/axiosInstance';

export const HeaderTitleComponent = ({
  value,
  search,
  setSearch,
  searchText,
  setSearchText,
  suggestions,
  setSuggestions,
}) => {
  const navigation = useNavigation();

  const fetchAutoCompleteSuggestions = async query => {
    if (query.length > 0 && value !== 'gpt') {
      try {
        const response = await axiosInstance.get(
          `content-slave-service/auto-completion?searchType=${value}&keyword=${query}`,
        );
        setSuggestions(response.data.payload.autoCompletionList);
      } catch (e) {
        console.error(e);
      }
    } else {
      setSuggestions([]);
    }
  };

  const debouncedFetchSuggestions = useCallback(
    debounce(text => {
      fetchAutoCompleteSuggestions(text);
    }, 200),
    [],
  );

  if (search) {
    return (
      <>
        <TextInput
          placeholder="검색"
          style={styles.searchBar}
          onChangeText={text => {
            setSearchText(text);
            debouncedFetchSuggestions(text);
          }}
          onSubmitEditing={() => {
            setSuggestions([]);
            setSearch(false);
            navigation.push('SearchedVideos', {searchText, value});
          }}
        />
        {suggestions.length > 0 && (
          <View style={styles.autoCompleteContainer}>
            <FlatList
              data={suggestions}
              keyExtractor={(item, index) => index.toString()}
              renderItem={({item}) => (
                <TouchableOpacity
                  style={styles.suggestionItem}
                  onPress={() => {
                    setSearchText(item);
                    setSuggestions([]);
                    setSearch(false);
                    navigation.push('SearchedVideos', {searchText});
                  }}>
                  <Text style={styles.autoCompleteText}>{item}</Text>
                </TouchableOpacity>
              )}
            />
          </View>
        )}
      </>
    );
  } else {
    return <Image source={require('../../assets/logo.png')} />;
  }
};

export const HeaderRightComponent = ({search, setSearch, setSuggestions}) => {
  const navigation = useNavigation();
  if (search) {
    return (
      <View style={styles.itemContainer}>
        <TouchableOpacity
          style={styles.button}
          onPress={() => {
            setSearch(false);
            setSuggestions([]);
          }}>
          <Icon style={styles.icon} name="close" size={30} color={'black'} />
        </TouchableOpacity>
      </View>
    );
  }

  return (
    <View style={styles.itemContainer}>
      <TouchableOpacity
        onPress={() => {
          setSearch(!search);
          setSuggestions([]);
        }}>
        <Icon
          style={styles.icon}
          name="search-outline"
          size={30}
          color={'black'}
        />
      </TouchableOpacity>
      <TouchableOpacity onPress={() => navigation.openDrawer()}>
        <Icon
          style={styles.icon}
          name="person-circle-outline"
          size={30}
          color={'black'}
        />
      </TouchableOpacity>
    </View>
  );
};

export const HeaderLeftComponent = ({
  search,
  setSortModalVisible,
  sortModalVisible,
  setValue,
  items,
  value,
}) => {
  if (search) {
    const getLabelForValue = val => {
      const item = items.find(i => i.value === val);
      return item ? item.label : val;
    };
    return (
      <>
        <TouchableOpacity
          style={styles.sortTypeButton}
          onPress={() => setSortModalVisible(true)}>
          <Text style={styles.sortTypeFont}>{getLabelForValue(value)}</Text>
          <Icon name="chevron-down-sharp" size={20} color={'white'} />
        </TouchableOpacity>
        <SearchTypeModal
          sortModalVisible={sortModalVisible}
          setSortModalVisible={setSortModalVisible}
          setValue={setValue}
          items={items}
        />
      </>
    );
  } else {
    return null;
  }
};

const styles = StyleSheet.create({
  searchBar: {
    position: 'relative',
    left: 20,
    backgroundColor: '#EBEBEB',
    width: 220,
    borderRadius: 10,
    height: 40,
    paddingHorizontal: 10,
  },
  autoCompleteContainer: {
    position: 'absolute',
    top: 55,
    left: 20,
    backgroundColor: 'white',
    width: '100%',
    zIndex: 2,
    padding: 15,
    borderRadius: 10,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 5,
  },
  suggestionItem: {paddingVertical: 5},
  autoCompleteText: {color: 'black'},
  itemContainer: {
    flexDirection: 'row',
    paddingHorizontal: 5,
  },
  icon: {
    marginRight: 8,
  },
  sortTypeButton: {
    flexDirection: 'row',
    backgroundColor: '#21C99B',
    width: 80,
    gap: 2,
    alignItems: 'center',
    marginLeft: 10,
    height: 39,
    justifyContent: 'center',
    borderRadius: 10,
  },
  sortTypeFont: {
    fontWeight: '600',
    color: 'white',
  },
});
