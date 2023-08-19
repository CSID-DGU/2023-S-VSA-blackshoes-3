import React from 'react';
import {
  View,
  StyleSheet,
  Text,
  Image,
  TouchableOpacity,
  Linking,
} from 'react-native';
import WebView from 'react-native-webview';

export const Ad = ({adContents, logoUri}) => {
  const handleOpenUrl = () => {
    Linking.openURL(adContents.adUrl);
  };

  return (
    <View style={styles.container}>
      <WebView source={{uri: adContents.adUrl}} style={styles.webview} />
      <View style={styles.infoContainer}>
        <Image
          style={styles.logo}
          source={{uri: `data:image/png;base64,${logoUri}`}}
        />
        <Text style={styles.text}>{adContents.adContent}</Text>
        <TouchableOpacity onPress={handleOpenUrl}>
          <Text>이동</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    width: '100%',
    paddingHorizontal: 15,
    backgroundColor: '#DEDEDE',
    paddingVertical: 15,
  },
  webview: {
    width: '100%',
    height: 190,
  },
  infoContainer: {
    width: '100%',

    paddingHorizontal: 15,
    paddingTop: 10,
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
