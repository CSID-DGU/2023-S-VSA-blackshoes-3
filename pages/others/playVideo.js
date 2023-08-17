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
  TextInput,
} from 'react-native';
import Orientation from 'react-native-orientation-locker';
import axiosInstance from '../../utils/axiosInstance';
import Icon from 'react-native-vector-icons/MaterialCommunityIcons';
import VideoPlayer from 'react-native-video-controls';
import {Ad} from '../../components/contents/advertiseBox';
import {useSelector} from 'react-redux';

export default function Play({route, navigation}) {
  const [videoData, setVideoData] = useState([]);
  const [currentTime, setCurrentTime] = useState(0);
  const [isFullScreen, setFullScreen] = useState(false);
  const [controlsVisible, setControlsVisible] = useState(false);
  const [openAd, setOpenAd] = useState(null);
  const [like, setLike] = useState(false);
  const [countLike, setConutLike] = useState(route.params.video.likes);
  const [tagIds, setTagIds] = useState([]);
  const [commentIndex, setCommentIndex] = useState(false);
  const [comment, setComment] = useState(null);
  const [commentInput, setCommentInput] = useState('');
  const userId = useSelector(state => state.USER);

  const toggleControls = () => {
    setControlsVisible(!controlsVisible);
  };

  const videoRef = useRef(null);
  useEffect(() => {
    getData();
  }, []);

  useEffect(() => {
    if (videoData.length === 0) {
      return;
    } else {
      registerHistory();

      checkLike();
      getComment();
    }
  }, [videoData]);

  useEffect(() => {
    if (tagIds.length === 0) {
      return;
    } else {
      registerHistoryTag();
    }
  }, [tagIds]);

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

  const getData = async () => {
    try {
      const response = await axiosInstance.get(
        `content-slave-service/videos/video?type=videoId&q=${route.params.video.videoId}`,
      );
      setVideoData(response.data.payload.video);
      setTagIds(response.data.payload.video.videoTags.map(tag => tag.tagId));
    } catch (e) {
      console.log(e);
    }
  };

  const checkLike = async () => {
    const response = await axiosInstance.get(
      `personalized-service/${userId}/videos/liked/${videoData.videoId}`,
    );
    setLike(response.data.payload.isLiked);
  };

  const likes = () => {
    if (like) {
      try {
        const response = axiosInstance.delete(
          `personalized-service/${userId}/videos/liked/${videoData.videoId}`,
        );
        console.log(response);
        setLike(false);
        setConutLike(countLike - 1);
      } catch (e) {
        console.log(e);
      }
    } else {
      try {
        const response = axiosInstance.post(
          `personalized-service/${userId}/videos/liked`,
          {
            videoId: videoData.videoId,
            sellerId: videoData.sellerId,
          },
        );
        console.log(response);
        setLike(true);
        setConutLike(countLike + 1);
      } catch (e) {
        console.log(e.response);
      }
    }
  };

  const registerHistory = async () => {
    try {
      const response = axiosInstance.post(
        `personalized-service/${userId}/videos/history`,
        {
          videoId: videoData.videoId,
          sellerId: videoData.sellerId,
        },
      );
      console.log('시청기록 응답 : ', response.data.payload);
    } catch (e) {
      console.log(e);
    }
  };

  const registerHistoryTag = async () => {
    console.log('태그 아이디즈 : ', tagIds);
    console.log('유저아이디 : ', userId);
    try {
      const response = axiosInstance.post(
        `personalized-service/${userId}/tags/viewed`,
        {
          tagIdList: tagIds,
        },
      );
      console.log('response of tag');
      console.log(response);
    } catch (e) {
      console.log(e);
    }
  };

  const getComment = async () => {
    try {
      console.log('셀러아이디 : ', videoData.sellerId);
      const response = await axiosInstance.get(
        `comment-service/comments/${videoData.sellerId}/${videoData.videoId}?page=0&size=10`,
      );
      setComment(response.data.payload);
    } catch (e) {
      console.log(e);
    }
  };

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
              disableVolume={true}
              onProgress={({currentTime}) => setCurrentTime(currentTime)}
              onTouchEnd={toggleControls}
            />
          </>
        )}
      </View>

      {!isFullScreen && (
        <View style={styles.contentsContainer}>
          <View style={styles.videoInfoContainer}>
            <Text style={styles.title}>{videoData.videoName}</Text>
            <View style={styles.subInfoContainer}>
              <View style={styles.subInfoContainer1}>
                <Text style={styles.smallText}>
                  {videoData.createdAt && videoData.createdAt.slice(0, 10)}
                </Text>

                <Text style={styles.smallText}>
                  조회수 {route.params.video.views}회
                </Text>

                <Text style={styles.smallText}>좋아요 {countLike}회</Text>
              </View>

              <TouchableOpacity style={styles.likesContainer} onPress={likes}>
                <Icon
                  name="cards-heart"
                  size={20}
                  color={like ? 'red' : 'grey'}
                />
                <Text style={styles.likesText}>Like ~</Text>
              </TouchableOpacity>
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
                      onPress={() => navigation.navigate('ThemeVideo', {item})}>
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
            <View style={styles.commentContainer}>
              {!commentIndex ? (
                <TouchableOpacity
                  style={styles.firstCommentContainer}
                  onPress={() => setCommentIndex(true)}>
                  {comment ? (
                    comment.comments.length === 0 ? (
                      <Text style={styles.commentContents}>
                        아직 댓글이 없습니다. 댓글을 등록해주세요.
                      </Text>
                    ) : (
                      <>
                        <Text style={styles.commentUserName}>
                          {comment.comments[0].nickname}
                        </Text>
                        <Text style={styles.commentContents}>
                          {comment.comments[0].content}
                        </Text>
                      </>
                    )
                  ) : (
                    <Text>wait hi</Text>
                  )}
                </TouchableOpacity>
              ) : (
                <View style={styles.allCommentsContainer}>
                  <TouchableOpacity onPress={() => setCommentIndex(false)}>
                    <Text>close</Text>
                  </TouchableOpacity>
                  {comment ? (
                    comment.comments.length === 0 ? (
                      <Text style={styles.commentContents}>
                        아직 댓글이 없습니다.
                      </Text>
                    ) : (
                      <>
                        <TextInput
                          style={styles.comments.commentInput}
                          placeholder="댓글 입력"
                          placeholderTextColor={'#c9c9c9'}
                          onChangeText={text => setCommentInput(text)}
                          value={commentInput}
                          onSubmitEditing={() => {
                            // submitComment();
                          }}
                        />
                        <TouchableOpacity
                          style={styles.submitButton}
                          // onPress={() => submitComment()}
                        >
                          <Text>submit</Text>
                        </TouchableOpacity>
                        {comment.comments.map((e, i) => {
                          <TouchableOpacity
                            style={styles.allCommentsBox}
                            key={i}>
                            <Text>hi</Text>
                          </TouchableOpacity>;
                        })}
                      </>
                    )
                  ) : (
                    <Text>wait hi</Text>
                  )}
                </View>
              )}
            </View>
          </ScrollView>
        </View>
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
    alignItems: 'flex-start',
  },

  videoInfoContainer: {
    paddingTop: 20,
    paddingBottom: 10,
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
    marginRight: 15,
  },
  subInfoContainer: {
    marginTop: 5,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  subInfoContainer1: {
    flexDirection: 'row',
    alignItems: 'flex-start',
  },
  icon: {marginRight: 5},
  sellerInfoContainer: {
    paddingVertical: 5,
    flexDirection: 'row',
    alignItems: 'center',
    marginLeft: 15,
    gap: 10,
  },
  logo: {
    width: 30,
    height: 30,
    borderRadius: 15,
  },
  likesContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    marginRight: 8,
    gap: 5,
    backgroundColor: 'white',
    paddingHorizontal: 13,
    paddingVertical: 2.5,
    borderRadius: 10,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 1,
    },
    shadowOpacity: 0.25,
    shadowRadius: 10,
    elevation: 2,
  },
  likesText: {
    fontSize: 16,
    fontWeight: 'bold',
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
