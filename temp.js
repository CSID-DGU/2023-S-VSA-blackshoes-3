<View style={isFullScreen ? styles.fullScreenContainer : styles.container}>
  <View
    style={
      isFullScreen ? styles.fullScreenVideoContainer : styles.videoContainer
    }>
    {videoData.videoUrl && (
      <VideoPlayer
        ref={videoRef}
        source={{uri: videoData.videoUrl}}
        fullscreen={isFullScreen}
        onEnterFullscreen={() => setFullScreen(true)}
        onExitFullscreen={() => setFullScreen(false)}
        controlTimeout={3000}
        style={isFullScreen ? styles.fullScreenVideo : {width: '100%'}}
        resizeMode="contain"
        disableBack={true}
        disableVolume={true}
      />
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
        <View style={styles.scrollContainer}>
          <ScrollView
            horizontal
            onScroll={handleScroll}
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
          <View style={styles.sellerInfoContainer}>
            <Image
              style={styles.logo}
              source={{uri: `data:image/png;base64,${videoData.sellerLogo}`}}
            />
            <Text style={styles.title}>{route.params.video.sellerName}</Text>
          </View>
        </ScrollView>

        {/* {openAd && (
          <Ad adContents={openAd} logoUri={videoData.sellerLogo} />

          // <View style={styles.adContainer}>
          //   <Image
          //     style={styles.logo}
          //     source={{ uri: `data:image/png;base64,${videoData.sellerLogo}` }}
          //   />
          //   <Text>{openAd}</Text>
          // </View>
        )} */}
      </View>
      <View style={styles.toolbarContainer}>
        <Toolbar route={route} />
      </View>
    </>
  )}
</View>;
