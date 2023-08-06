import React, {useState, useEffect} from 'react';
import {View, StyleSheet, Text} from 'react-native';

export default function MyLog() {
  return (
    <View style={styles.container}>
      <Text>log</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 10,
  },
  toolbarContainer: {
    flex: 0.1,
  },
  contentsContainer: {
    flex: 0.9,
    justifyContent: 'flex-start',
    alignItems: 'center',
  },
});
