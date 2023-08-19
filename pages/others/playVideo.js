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
import Icon2 from 'react-native-vector-icons/Ionicons';
import VideoPlayer from 'react-native-video-controls';
import {Ad} from '../../components/contents/advertiseBox';
import {useSelector} from 'react-redux';
import axios from 'axios';
import {set} from 'lodash';
import {VideoThumbnail} from '../../components/contents/thumbnailBox';

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
  const [comment, setComment] = useState([]);
  const [commentAmmount, setCommentAmmount] = useState(0);
  const [commentPage, setCommentPage] = useState(null);

  const [recommendVideoPage, setRecommendVideoPage] = useState(0);
  const [recommendedVideos, setRecommendedVideos] = useState([]);
  const [commentInput, setCommentInput] = useState('');
  const [modifying, setModifying] = useState('');
  const [modifyIndex, setModifyIndex] = useState(null);

  const userId = useSelector(state => state.USER);
  const nick = useSelector(state => state.NICK);

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
      console.log('페이지', recommendVideoPage);
      getRecommendVideo();
    }
  }, [tagIds, recommendVideoPage]);

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
        setOpenAd(ad);
        return true;
      } else {
        setOpenAd(null);
      }
      return false;
    });
    return;
  }, [currentTime]);
  console.log('In openAd : ', videoData.videoAds);

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

  const likes = async () => {
    if (like) {
      try {
        const response = await axiosInstance.delete(
          `personalized-service/${userId}/videos/liked/${videoData.videoId}`,
        );
        setLike(false);
        setConutLike(countLike - 1);

        const response2 = await axiosInstance.put(
          `statistics-service/statistics/${videoData.videoId}/like`,
          {
            userId: userId,
            action: 'dislike',
          },
        );
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

        setLike(true);
        setConutLike(countLike + 1);

        const response2 = axiosInstance.put(
          `statistics-service/${videoData.videoId}/likes`,
          {
            userId: userId,
            action: 'like',
          },
        );
      } catch (e) {
        console.log(e.response);
      }
    }
  };

  const registerHistory = async () => {
    try {
      const response = await axiosInstance.post(
        `personalized-service/${userId}/videos/history`,
        {
          videoId: videoData.videoId,
          sellerId: videoData.sellerId,
        },
      );

      console.log('videoData.videoId : ', videoData.videoId);
      const response2 = await axiosInstance.put(
        `statistics-service/${videoData.videoId}/views`,
        {
          userId: userId,
        },
      );
    } catch (e) {
      console.log(e);
    }
  };

  const registerHistoryTag = async () => {
    try {
      const response = axiosInstance.post(
        `personalized-service/${userId}/tags/viewed`,
        {
          tagIdList: tagIds,
        },
      );
      console.log('response of tag');
    } catch (e) {
      console.log(e);
    }
  };

  const getComment = async () => {
    try {
      const response = await axiosInstance.get(
        `comment-service/comments/video?videoId=${videoData.videoId}&page=0&size=10`,
      );
      setComment(response.data.payload.comments);
      setCommentAmmount(response.data.payload.totalElements);
    } catch (e) {
      console.log(e);
    }
  };

  const submitComment = async () => {
    try {
      console.log('userId submitComment : ', userId);
      const response = await axiosInstance.post(
        `comment-service/comments/${videoData.videoId}`,
        {
          userId: userId,
          nickname: nick,
          content: commentInput,
        },
      );
      getComment();

      setCommentInput('');
    } catch (e) {
      console.log(e);
    }
  };

  const deleteComment = async e => {
    try {
      const response = await axiosInstance.put(
        `comment-service/comments/${videoData.videoId}/${e.commentId}/delete`,
        {
          userId: userId,
        },
      );
      setComment(prevComments =>
        prevComments.filter(comment => comment.commentId !== e.commentId),
      );
    } catch (error) {
      console.log(error);
    }
  };

  const modifyComment = async e => {
    try {
      const response = await axiosInstance.put(
        `comment-service/comments/${videoData.videoId}/${e.commentId}`,
        {
          userId: userId,
          content: modifying,
        },
      );
      getData();
    } catch (error) {
      console.log(error);
    }
  };

  const getRecommendVideo = async () => {
    try {
      console.log(tagIds);

      const tagId = tagIds.join(',');

      const response = await axiosInstance.get(
        `content-slave-service/videos/tagIds?q=${tagId}&u=${userId}&page=${recommendVideoPage}`,
      );

      const filteredVideos = response.data.payload.videos.filter(
        video => video.videoId !== route.params.videoId,
      );

      setRecommendedVideos(prev => [...prev, ...filteredVideos]);
    } catch (e) {
      console.log(e);
    }
  };

  const handleScrollend = event => {
    const offsetY = event.nativeEvent.contentOffset.y;
    const contentHeight = event.nativeEvent.contentSize.height;
    const layoutHeight = event.nativeEvent.layoutMeasurement.height;
    console.log('offsetY : ', offsetY);
    console.log('contentHeight : ', contentHeight);
    console.log('layoutHeight : ', layoutHeight);

    if (offsetY + layoutHeight >= contentHeight - 1) {
      console.log('hi');
      setRecommendVideoPage(prevPage => prevPage + 1);
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
          {!commentIndex && (
            <ScrollView
              style={styles.scrollVerticalContainer}
              contentContainerStyle={
                {
                  // paddingHorizontal: 10,
                }
              }
              onScroll={handleScrollend}
              scrollEventThrottle={10}>
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

                  <TouchableOpacity
                    style={styles.likesContainer}
                    onPress={likes}>
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
                <Text style={styles.title}>
                  {route.params.video.sellerName}
                </Text>
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

              <View style={{paddingHorizontal: 15}}>
                <TouchableOpacity
                  style={styles.firstCommentContainer}
                  onPress={() => setCommentIndex(true)}>
                  {comment ? (
                    comment.length === 0 ? (
                      <Text style={styles.commentFirstContents}>
                        아직 댓글이 없습니다. 댓글을 등록해주세요.
                      </Text>
                    ) : (
                      <>
                        <Text style={styles.commentTitle}>댓글</Text>
                        <View style={styles.firstCommentBox}>
                          <Text style={styles.commentUserName}>
                            {comment[0].nickname}
                          </Text>
                          <Text style={styles.commentContents}>
                            {comment[0].content}
                          </Text>
                        </View>
                      </>
                    )
                  ) : (
                    <Text>wait hi</Text>
                  )}
                </TouchableOpacity>
                {openAd && (
                  <View style={styles.bottomContainer}>
                    <Ad adContents={openAd} logoUri={videoData.sellerLogo} />
                  </View>
                )}
                {recommendedVideos.length > 0 &&
                  recommendedVideos.map((e, i) => {
                    return (
                      <TouchableOpacity
                        style={styles.videoThumbnailContainer}
                        key={i}
                        onPress={() => navigation.push('Play', {video: e})}>
                        <VideoThumbnail
                          key={i}
                          video={e}
                          navigation={navigation}
                        />
                      </TouchableOpacity>
                    );
                  })}
              </View>
            </ScrollView>
          )}

          <View
            style={[
              styles.commentContainer,
              !commentIndex ? {paddingHorizontal: 15} : {},
            ]}>
            {commentIndex && (
              <View style={styles.allCommentsContainer}>
                <View style={styles.allCommentsTopMenu}>
                  <Text style={styles.commentTitle}>댓글</Text>
                  <TouchableOpacity onPress={() => setCommentIndex(false)}>
                    <Icon
                      style={styles.icon}
                      name="close"
                      size={23}
                      color={'black'}
                    />
                  </TouchableOpacity>
                </View>

                {comment ? (
                  comment.length === 0 ? (
                    <>
                      <Text style={styles.commentContents}>
                        아직 댓글이 없습니다.
                      </Text>
                      <TextInput
                        style={styles.commentInput}
                        placeholder="댓글 입력"
                        placeholderTextColor={'#c9c9c9'}
                        onChangeText={text => setCommentInput(text)}
                        value={commentInput}
                        onSubmitEditing={() => {
                          submitComment();
                        }}
                      />
                    </>
                  ) : (
                    <>
                      <TextInput
                        style={styles.commentInput}
                        placeholder="댓글 입력"
                        placeholderTextColor={'#c9c9c9'}
                        onChangeText={text => setCommentInput(text)}
                        value={commentInput}
                        onSubmitEditing={() => {
                          submitComment();
                        }}
                      />
                      <ScrollView
                        style={styles.scrollContainer}
                        contentContainerStyle={{alignItems: 'center'}}>
                        <View>
                          {comment.map((e, i) => {
                            return (
                              <TouchableOpacity
                                style={styles.allCommentsBox}
                                key={i}>
                                <View style={styles.commentsInfo}>
                                  <Text style={styles.commentUserNameAll}>
                                    {e.nickname}
                                  </Text>
                                  <Text style={styles.commentDate}>
                                    {e.createdAt.split('T')[0]}
                                  </Text>
                                </View>
                                <View
                                  style={styles.commentContentsAllContainer}>
                                  <Text style={styles.commentContentsAll}>
                                    {e.content}
                                  </Text>
                                  {e.userId === userId && (
                                    <View
                                      style={styles.commentsButtonContainer}>
                                      <TouchableOpacity
                                        onPress={() => {
                                          if (modifyIndex === i) {
                                            setModifyIndex(null);
                                            setModifying('');
                                          } else {
                                            setModifyIndex(i);
                                          }
                                        }}>
                                        <Text>
                                          {modifyIndex === i ? '취소' : '수정'}
                                        </Text>
                                      </TouchableOpacity>
                                      <TouchableOpacity
                                        onPress={() => deleteComment(e)}>
                                        {/* <Icon2
                                          name="trash-outline"
                                          size={30}
                                          color={'black'}
                                        /> */}
                                        <Text>삭제</Text>
                                      </TouchableOpacity>
                                    </View>
                                  )}
                                </View>
                                {modifyIndex === i && (
                                  <View style={styles.modifyCommentContainer}>
                                    <Icon
                                      name="arrow-up-left"
                                      size={25}
                                      color={'black'}
                                    />
                                    <TextInput
                                      style={styles.commentModifyInput}
                                      placeholder="댓글 수정"
                                      placeholderTextColor={'#c9c9c9'}
                                      onChangeText={text => setModifying(text)}
                                      value={modifying}
                                      onSubmitEditing={() => {
                                        modifyComment(e);
                                        setModifyIndex(null);
                                        setModifying('');
                                      }}
                                    />
                                  </View>
                                )}
                              </TouchableOpacity>
                            );
                          })}
                        </View>
                      </ScrollView>
                    </>
                  )
                ) : (
                  <Text>wait hi</Text>
                )}
              </View>
            )}
          </View>
        </View>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'space-between',
    backgroundColor: '#F2F2F2',
  },
  fullScreenContainer: {
    flex: 1,
    justifyContent: 'center',
    backgroundColor: 'black',
  },
  videoContainer: {
    height: 235,
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

  commentScrollContainer: {
    width: '100%',
    backgroundColor: 'white',
    marginTop: 20,
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
  commentContainer: {
    width: '100%',
  },
  firstCommentContainer: {
    backgroundColor: '#E0E0E0',
    paddingHorizontal: 20,
    borderRadius: 10,
    shadowColor: '#000',
    paddingVertical: 12,
  },
  commentTitle: {
    fontWeight: 'bold',
    fontSize: 16,
    color: 'black',
  },
  firstCommentBox: {
    flexDirection: 'row',
    backgroundColor: 'white',
    alignItems: 'center',
    borderRadius: 10,
    paddingVertical: 5,
    gap: 10,
    marginTop: 5,
    paddingHorizontal: 17,
    shadowOffset: {
      width: 0,
      height: 1,
    },
    shadowOpacity: 0.25,
    shadowRadius: 10,
    elevation: 0.5,
  },
  commentUserName: {
    color: 'black',
    fontSize: 17,
    fontWeight: 'bold',
  },
  commentContents: {
    fontSize: 17,
    fontWeight: '600',
    paddingHorizontal: 20,
    lineHeight: 25,
    paddingVertical: 5,
  },
  commentFirstContents: {
    fontSize: 17,
    fontWeight: '600',
    paddingHorizontal: 12,
    lineHeight: 25,
    backgroundColor: 'white',
    borderRadius: 10,
    paddingVertical: 5,
  },
  allCommentsContainer: {
    backgroundColor: '#F0F0F0',
    shadowOffset: {
      width: 0,
      height: 1,
    },
    shadowOpacity: 0.25,
    shadowRadius: 10,
    elevation: 2,
    marginBottom: 20,
    height: '100%',
    paddingVertical: 15,
  },
  allCommentsTopMenu: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingLeft: 5,
    marginHorizontal: 15,
  },
  commentInput: {
    backgroundColor: 'white',
    borderRadius: 10,
    marginTop: 10,
    marginHorizontal: 15,
    shadowOffset: {
      width: 0,
      height: 1,
    },
    shadowOpacity: 0.25,
    shadowRadius: 10,
    elevation: 1,
    paddingHorizontal: 15,
  },
  allCommentsBox: {
    paddingVertical: 10,
    width: 350,
    gap: 5,
    paddingHorizontal: 10,
    borderBottomWidth: 0.6,
  },
  commentsInfo: {
    flexDirection: 'row',
    gap: 10,
    alignItems: 'flex-end',
  },
  commentUserNameAll: {
    fontSize: 19,
    color: 'black',
    fontWeight: 'bold',
  },
  commentDate: {fontSize: 11},
  commentContentsAll: {fontSize: 16},
  commentContentsAllContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  modifyCommentContainer: {
    flexDirection: 'row',
    width: '100%',
    justifyContent: 'center',
    alignItems: 'center',
    height: 40,
    gap: 10,
    marginTop: 5,
  },
  commentsButtonContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 10,
  },
  commentModifyInput: {
    backgroundColor: 'white',
    borderRadius: 10,
    borderWidth: 0.5,
    paddingHorizontal: 15,
    width: '85%',
  },
  videoThumbnailContainer: {
    backgroundColor: '#DEDEDE',
    alignItems: 'center',
    paddingTop: 17,
    paddingBottom: 10,
    marginVertical: 7,

    width: '100%',
  },
  scrollViewContainer: {},
  scrollVerticalContainer: {
    width: '100%',
  },
});
