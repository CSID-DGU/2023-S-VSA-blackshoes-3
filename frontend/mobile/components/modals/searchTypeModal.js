import React from 'react';
import {View, Text, TouchableOpacity, StyleSheet, Modal} from 'react-native';

export const SearchTypeModal = ({
  sortModalVisible,
  setSortModalVisible,
  setValue,
  items,
}) => {
  console.log(items);
  return (
    <Modal
      transparent={true}
      visible={sortModalVisible}
      onRequestClose={() => {
        setSortModalVisible(!sortModalVisible);
      }}>
      <TouchableOpacity
        style={styles.outerContainer}
        activeOpacity={1}
        onPress={() => setSortModalVisible(false)}>
        <View style={styles.modalContainer}>
          {items.map(item => (
            <TouchableOpacity
              style={styles.modalContents}
              key={item.value}
              onPress={() => {
                setValue(item.value);
                setSortModalVisible(false);
              }}>
              <Text style={styles.modalText}>
                {item.label === 'Seller'
                  ? '판매자명으로 검색'
                  : item.label === 'Video'
                  ? '영상 제목으로 검색'
                  : item.label === 'Tag'
                  ? '태그로 검색'
                  : item.label === 'GPT'
                  ? 'GPT로 검색'
                  : ''}
              </Text>
            </TouchableOpacity>
          ))}
        </View>
      </TouchableOpacity>
    </Modal>
  );
};

const styles = StyleSheet.create({
  outerContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
  },
  modalContainer: {
    gap: 40,
    justifyContent: 'center',
    height: 315,
    width: 240,
    backgroundColor: 'white',
    borderRadius: 20,
    padding: 20,
  },
  modalContents: {
    paddingHorizontal: 20,
  },
  modalText: {
    fontSize: 20,
    color: 'black',
  },
});
