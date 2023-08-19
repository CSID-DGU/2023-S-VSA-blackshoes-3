import React from 'react';
import {View, Text, TouchableOpacity, StyleSheet, Modal} from 'react-native';

export const SearchTypeModal = ({
  sortModalVisible,
  setSortModalVisible,
  setValue,
  items,
}) => {
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
                {item.label === 'sellerName'
                  ? '판매자명으로 검색'
                  : item.label === 'videoName'
                  ? '영상 제목으로 검색'
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
    gap: 35,
    justifyContent: 'center',
    width: 0,
    height: 200,
    backgroundColor: 'white',
    borderRadius: 20,
    padding: 20,
  },
  modalContents: {
    alignItems: 'center',
  },
  modalText: {
    fontSize: 20,
    color: 'black',
  },
});
