import React from 'react';
import {View, StyleSheet, Text, Image} from 'react-native';

export const Ad = ({adContents, logoUri}) => {
  return (
    <View style={styles.container}>
      <Image
        style={styles.logo}
        source={{uri: `data:image/png;base64,${logoUri}`}}
      />
      <Text style={styles.text}>{adContents}</Text>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    width: '90%',
    borderRadius: 10,
    backgroundColor: 'white',
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 1,
    },
    shadowOpacity: 0.25,
    shadowRadius: 10,
    elevation: 3,
    paddingHorizontal: 25,
    paddingVertical: 10,
    flexDirection: 'row',
    alignItems: 'center',
    gap: 20,
  },
  logo: {
    width: 40,
    height: 40,
    borderRadius: 15,
  },
  text: {
    fontSize: 25,
    fontStyle: 'italic',
    fontWeight: 'bold',
  },
});
