import { useNavigate, useParams } from "react-router-dom";
import Header from "../components/Fragments/Nav/Header";
import Nav from "../components/Fragments/Nav/Nav";
import ResNav from "../components/Fragments/Nav/ResNav";
import { Body, GridWrapper } from "../components/Home/HomeStyle";
import { useContext, useEffect, useRef, useState } from "react";
import { GlobalContext } from "../context/GlobalContext";
import * as M from "../components/Fragments/Manage/ManageStyle";
import videojs from "video.js";
import "video.js/dist/video-js.css";
import { SlaveInstance, UploadInstance } from "../api/axios";
import Mleft from "../components/Fragments/Manage/Mleft";
import Mmiddle from "../components/Fragments/Manage/Mmiddle";
import Mright from "../components/Fragments/Manage/Mright";

const Manage = () => {
  // Constant----------------------------------------------------
  const navigate = useNavigate();
  const { userId } = useParams();
  const qualities = ["1080p", "720p", "480p"];

  // State-------------------------------------------------------
  const { setPage } = useContext(GlobalContext);
  const [videoList, setVideoList] = useState([]);
  const [sortOption, setSortOption] = useState("최신순");
  const [selectedQuality, setSelectedQuality] = useState(qualities[0]);
  const videoRef = useRef(null);
  const [initialized, setInitialized] = useState(false);
  const [videoId, setVideoId] = useState("");
  const [videoUrl, setVideoUrl] = useState("");
  const [videoName, setVideoName] = useState("");
  const [videoThumbnail, setVideoThumbnail] = useState("");
  const [videoTags, setVideoTags] = useState([]);
  const [videoAds, setVideoAds] = useState([]);
  const [videoComments, setVideoComments] = useState([]);
  const [regionTag, setRegionTag] = useState([]);
  const [themeTag, setThemeTag] = useState([]);

  // Function----------------------------------------------------
  const fetchData = async () => {
    // 기본 태그 가져오기
    const regionData = await UploadInstance.get(`/upload-service/tags/region`);
    const themeData = await UploadInstance.get(`/upload-service/tags/theme`);
    setRegionTag(regionData.data.payload.tags);
    setThemeTag(themeData.data.payload.tags);
    try {
      // 비디오 리스트 가져오기
      if (sortOption === "최신순") {
        const videoRecentData = await SlaveInstance.get(
          `/content-slave-service/videos/${userId}/sort?s=recent&page=0&size=10`
        );
        setVideoList(videoRecentData.data.payload.videos);
      } else if (sortOption === "조회수순") {
        const videoViewData = await SlaveInstance.get(
          `/content-slave-service/videos/${userId}/sort?s=views&page=0&size=10`
        );
        setVideoList(videoViewData.data.payload.videos);
      } else if (sortOption === "좋아요순") {
        const videoLikeData = await SlaveInstance.get(
          `/content-slave-service/videos/${userId}/sort?s=likes&page=0&size=10`
        );
        setVideoList(videoLikeData.data.payload.videos);
      } else if (sortOption === "광고클릭순") {
        const videoAdsData = await SlaveInstance.get(
          `/content-slave-service/videos/${userId}/sort?s=adClicks&page=0&size=10`
        );
        setVideoList(videoAdsData.data.payload.videos);
      }
    } catch (err) {
      console.log(err);
    }
  };

  const fetchModifyData = async () => {
    if (videoId) {
      // 수정 영상 가져오기
      await SlaveInstance.get(`/content-slave-service/videos/video?type=videoId&q=${videoId}`).then(
        (res) => {
          setVideoUrl(res.data.payload.video.videoUrl);
          setVideoName(res.data.payload.video.videoName);
          setVideoThumbnail(res.data.payload.video.thumbnailUrl);
          setVideoTags(res.data.payload.video.videoTags);
          setVideoAds(res.data.payload.video.videoAds);
        }
      );
    } else {
      return;
    }
  };

  // ComponentDidMount-------------------------------------------
  useEffect(() => {
    setPage(2);
    fetchData();
  }, [sortOption]);

  useEffect(() => {
    fetchModifyData();
  }, [videoId]);

  useEffect(() => {
    if (!initialized && videoRef.current !== null) {
      // video.js 옵션 설정
      const options = {
        techOrder: ["html5"],
        controls: true,
        autoplay: true,
        sources: [
          {
            src: `${videoUrl}/${selectedQuality}.m3u8`,
            type: "application/x-mpegURL",
          },
        ],
      };
      // video.js 생성 및 초기화
      const player = videojs(videoRef.current, options);
      setInitialized(true);
      // video.js 소멸
      return () => {
        if (player) {
          player.dispose();
        }
      };
    }
  }, [videoUrl, selectedQuality]);

  return (
    <GridWrapper>
      <Header />
      <Nav />
      <Body>
        <ResNav userId={userId} />
        <M.LeftMiddleBox>
          <Mleft
            videoList={videoList}
            videoId={videoId}
            setVideoId={setVideoId}
            setSortOption={setSortOption}
          />
          <Mmiddle
            userId={userId}
            videoId={videoId}
            videoName={videoName}
            setVideoName={setVideoName}
            videoUrl={videoUrl}
            videoRef={videoRef}
            videoThumbnail={videoThumbnail}
            setVideoThumbnail={setVideoThumbnail}
            videoTags={videoTags}
            selectedQuality={selectedQuality}
            regionTag={regionTag}
            themeTag={themeTag}
          />
        </M.LeftMiddleBox>
        <Mright videoAds={videoAds} userId={userId} videoId={videoId} />
      </Body>
    </GridWrapper>
  );
};

export default Manage;
