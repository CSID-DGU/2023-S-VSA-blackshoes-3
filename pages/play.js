/* eslint-disable quotes */
/* eslint-disable no-shadow */
/* eslint-disable react-native/no-inline-styles */
/* eslint-disable react-hooks/exhaustive-deps */
import React, {useEffect, useRef, useState} from 'react';
import {
  StyleSheet,
  View,
  Text,
  Image,
  ScrollView,
  TouchableOpacity,
} from 'react-native';
import Orientation from 'react-native-orientation-locker';
import axios from 'axios';
import {SERVER_IP} from '../config';
import Toolbar from '../components/toolBar';
import Icon from 'react-native-vector-icons/MaterialCommunityIcons';
import VideoPlayer from 'react-native-video-controls';
import {Ad} from '../components/ad';

export default function Play({route, navigation}) {
  const [videoData, setVideoData] = useState([]);
  const [currentTime, setCurrentTime] = useState(0);
  const [isFullScreen, setFullScreen] = useState(false);
  const [controlsVisible, setControlsVisible] = useState(false);
  const [openAd, setOpenAd] = useState(null);
  const [comment, setComment] = useState([]);

  const toggleControls = () => {
    setControlsVisible(!controlsVisible);
  };

  const videoRef = useRef(null);
  useEffect(() => {
    getData();
    // getComment();
  }, []);

  const getData = async () => {
    const response = await axios.get(
      `${SERVER_IP}:8011/content-slave-service/videos/video?type=videoId&q=${route.params.video.videoId}`,
    );
    setVideoData(response.data.payload.video);
  };

  // const getComment = async () => {
  //   const response = await axios.get(`http://192.168.0.8:3001/api/v1/comments`);
  //   console.log(response.data);
  //   setComment(response.data);
  // };
  useEffect(() => {
    if (videoData.length === 0) {
      return;
    }
    if (videoData.videoAds.length === 0) {
      return;
    }

    videoData.videoAds.some(ad => {
      if (
        parseInt(ad.startTime, 10) < currentTime * 1000 &&
        parseInt(ad.endTime, 10) > currentTime * 1000
      ) {
        setOpenAd(ad.adContent);
        return true;
      } else {
        setOpenAd(null);
      }
      return false;
    });
    return;
  }, [currentTime]);

  const seekForward = () => {
    if (videoRef.current) {
      const newTime = currentTime + 10;
      videoRef.current.seekTo(newTime);
    }
  };

  const seekBackward = () => {
    if (videoRef.current) {
      const newTime = Math.max(0, currentTime - 10);
      videoRef.current.seekTo(newTime);
    }
  };

  // const renderComment = item => {
  //   return (
  //     <View style={styles.commentContainer}>
  //       <View style={styles.commentInfoContainer}></View>
  //     </View>
  //   );
  // };

  //480p.m3u8 / 720p.m3u8 / 1080p.m3u8

  return (
    <View style={styles.container}>
      <View
        style={
          isFullScreen ? styles.fullScreenVideoContainer : styles.videoContainer
        }>
        {videoData.videoUrl && (
          <>
            <VideoPlayer
              ref={videoRef}
              source={{uri: videoData.videoUrl}}
              fullscreen={isFullScreen}
              onEnterFullscreen={() => {
                setFullScreen(true);
                Orientation.lockToLandscape();
              }}
              onExitFullscreen={() => {
                setFullScreen(false);
                Orientation.lockToPortrait();
              }}
              controlTimeout={3000}
              style={isFullScreen ? styles.fullScreenVideo : {width: '100%'}}
              resizeMode="contain"
              disableBack={true}
              onSettingModal={(visible, props) => {
                console.log(visible, props);
                // UI 상호작용을 잠시 멈춰둔다
              }}
              disableVolume={true}
              onProgress={({currentTime}) => setCurrentTime(currentTime)}
              onTouchEnd={toggleControls}
            />
            {controlsVisible && (
              <View style={styles.controlContainer}>
                <TouchableOpacity
                  style={styles.controlButton}
                  onPress={seekBackward}>
                  <Icon name="step-backward" size={40} color={'#D4D4D4'} />
                </TouchableOpacity>
                <TouchableOpacity
                  style={styles.controlButton}
                  onPress={seekForward}>
                  <Icon name="step-forward" size={40} color={'#D4D4D4'} />
                </TouchableOpacity>
              </View>
            )}
          </>
        )}
      </View>

      {!isFullScreen && (
        <>
          <View style={styles.contentsContainer}>
            <View style={styles.videoInfoContainer}>
              <Text style={styles.title}>{videoData.videoName}</Text>
              <View style={styles.subInfoContainer}>
                <Text style={styles.smallText}>
                  조회수 {route.params.video.views}회
                </Text>
                <Text style={styles.smallText}>
                  {videoData.createdAt && videoData.createdAt.slice(0, 10)}
                </Text>
                <Icon
                  style={styles.icon}
                  name="heart-outline"
                  size={20}
                  color={'gray'}
                />
                <Text style={styles.smallText}>
                  조회수 {route.params.video.likes}회
                </Text>
              </View>
            </View>
            <View style={styles.sellerInfoContainer}>
              <Image
                style={styles.logo}
                source={{
                  uri: `data:image/png;base64,${videoData.sellerLogo}`,
                }}
              />
              <Text style={styles.title}>{route.params.video.sellerName}</Text>
            </View>
            <View style={styles.scrollContainer}>
              <ScrollView
                horizontal
                showsHorizontalScrollIndicator={false}
                contentContainerStyle={{
                  width: 400,
                  alignItems: 'center',
                  gap: 20,
                  paddingHorizontal: 15,
                }}>
                {videoData.videoTags &&
                  videoData.videoTags.map((item, index) => {
                    return (
                      <TouchableOpacity
                        key={index}
                        style={styles.tagNameContainer}
                        onPress={() =>
                          navigation.navigate('ThemeVideo', {item})
                        }>
                        <Text style={styles.tagName}>{item.tagName}</Text>
                      </TouchableOpacity>
                    );
                  })}
              </ScrollView>
            </View>

            <ScrollView
              style={styles.infoContainer}
              contentContainerStyle={{alignItems: 'center'}}>
              <View style={styles.bottomContainer}>
                {openAd && (
                  <Ad adContents={openAd} logoUri={videoData.sellerLogo} />
                )}
              </View>
            </ScrollView>
          </View>
          <Toolbar route={route} />
        </>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'space-between',
    backgroundColor: '#F2F8FF',
  },
  fullScreenContainer: {
    flex: 1,
    justifyContent: 'center',
    backgroundColor: 'black',
  },
  videoContainer: {
    height: 240,
    justifyContent: 'center',
    alignItems: 'center',
  },
  fullScreenVideoContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  fullScreenVideo: {
    position: 'absolute',
    top: 0,
    left: 0,
    bottom: 0,
    right: 0,
  },
  controlContainer: {
    gap: 80,
    position: 'absolute',
    flexDirection: 'row',
    justifyContent: 'center',
    zIndex: 1,
    top: '50%',
    transform: [{translateY: -25}],
  },
  bottomContainer: {
    paddingVertical: 20,
    gap: 10,
    width: '100%',
    alignItems: 'center',
  },
  controlButton: {
    marginHorizontal: 5,
  },

  infoContainer: {
    width: '100%',
    backgroundColor: 'white',
  },
  contentsContainer: {
    flex: 1,
    justifyContent: 'flex-start',
    alignItems: 'center',
  },

  videoInfoContainer: {
    paddingVertical: 15,
    paddingHorizontal: 20,
    gap: 10,
    width: '100%',
  },
  title: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#404040',
  },
  smallText: {
    color: 'gray',
    marginRight: 20,
  },
  subInfoContainer: {
    flexDirection: 'row',
  },
  icon: {marginRight: 5},
  sellerInfoContainer: {
    paddingBottom: 15,
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 15,
    width: '100%',
    gap: 15,
  },
  logo: {
    width: 35,
    height: 35,
    borderRadius: 15,
  },

  scrollContainer: {
    height: 60,
  },

  tagNameContainer: {
    backgroundColor: 'white',
    borderRadius: 5,
    justifyContent: 'center',
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 1,
    },
    shadowOpacity: 0.25,
    shadowRadius: 10,
    elevation: 2,
    marginBottom: 10,
    width: 75,
    height: 30,
  },
  tagName: {
    fontSize: 15,
    textAlign: 'center',
  },
  adContainer: {
    borderWidth: 0.8,
    width: '80%',
  },
});
