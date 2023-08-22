/* eslint-disable react-native/no-inline-styles */
import React from 'react';
import {
  View,
  StyleSheet,
  Text,
  Image,
  TouchableOpacity,
  Linking,
} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import WebView from 'react-native-webview';

export const Ad = ({adContents, logoUri}) => {
  const handleOpenUrl = () => {
    Linking.openURL(adContents.adUrl);
  };

  return (
    <>
      <View
        style={{
          flexDirection: 'row',
          gap: 7,
        }}>
        <View
          style={{
            flexDirection: 'row',
            width: '100%',
            justifyContent: 'space-between',
          }}>
          <Text style={styles.commentTitle}>광고</Text>

          <TouchableOpacity style={styles.moveAdButton} onPress={handleOpenUrl}>
            <Text style={styles.moveAdButtonText}>상품 확인</Text>
          </TouchableOpacity>
        </View>

        {/* <Text style={styles.goButton}>상품 확인</Text> */}
      </View>
      <View style={styles.adContentsContainer}>
        {adContents &&
          (adContents.length > 40 ? (
            <Text style={styles.text}>
              {adContents.adContent.slice(0, 40) + '...'}
            </Text>
          ) : (
            <Text style={styles.text}>{adContents.adContent}</Text>
          ))}
      </View>
      {/* <WebView
        source={{uri: adContents.adUrl}}
        startInLoadingState={true}
        style={styles.webview}
        onLoad={() => {
          // WebView가 완전히 로드된 후에만 아래의 스크립트를 실행
          this.webview.injectJavaScript('window.scrollTo(0, 0);');
        }}
        ref={webview => {
          this.webview = webview;
        }}
      /> */}
    </>
  );
};

const styles = StyleSheet.create({
  webview: {
    width: '100%',
    height: 400,
  },
  commentTitle: {
    fontWeight: 'bold',
    fontSize: 16,
    color: 'black',
  },
  text: {
    color: 'black',
  },
  adContentsContainer: {
    flexDirection: 'row',
    backgroundColor: '#F5F5F5',
    justifyContent: 'center',
    alignItems: 'center',
    borderRadius: 10,
    paddingVertical: 2,
    gap: 10,
    marginTop: 5,
    paddingHorizontal: 5,
  },

  moveAdButton: {
    paddingHorizontal: 8,
    borderRadius: 10,
    backgroundColor: '#21C99B',
  },
  moveAdButtonText: {
    color: 'white',
    fontWeight: '600',
  },
});

// backgroundColor: 'white',
// paddingHorizontal: 20,
// borderRadius: 10,
// shadowColor: '#000',
// paddingVertical: 12,
// marginBottom: 5,
