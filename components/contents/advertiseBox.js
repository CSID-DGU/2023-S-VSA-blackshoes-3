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
          alignItems: 'flex-end',
        }}>
        <View
          style={{
            flexDirection: 'row',
            alignItems: 'flex-end',
            width: '100%',
            justifyContent: 'space-between',
          }}>
          <View
            style={{
              flexDirection: 'row',
              alignItems: 'flex-end',
              gap: 7,
            }}>
            <Text style={styles.commentTitle}>광고</Text>
          </View>
          <TouchableOpacity onPress={handleOpenUrl}>
            <Icon name="log-in" size={20} color={'black'} />
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
  container: {
    lineHeight: 25,
    backgroundColor: '#C2C2C2',
    borderRadius: 10,
    paddingVertical: 5,
    flexDirection: 'row',
  },
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

  goButton: {
    fontSize: 13,
    fontWeight: '600',
  },
});

// backgroundColor: 'white',
// paddingHorizontal: 20,
// borderRadius: 10,
// shadowColor: '#000',
// paddingVertical: 12,
// marginBottom: 5,
