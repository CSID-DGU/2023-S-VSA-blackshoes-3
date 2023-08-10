import { useNavigate, useParams } from "react-router-dom";
import Header from "../components/Fragments/Header";
import Nav from "../components/Fragments/Nav";
import { Body, GridWrapper } from "../components/Home/HomeStyle";
import { useContext, useEffect, useRef, useState } from "react";
import { GlobalContext } from "../context/GlobalContext";
import ResNav from "../components/Fragments/ResNav";
import * as M from "../components/Home/ManageStyle";
import * as U from "../components/Home/UploadStyle";
import PlusButton from "../assets/images/plus-button.svg";
import Minus from "../assets/images/minus.svg";
import videojs from "video.js";
import "video.js/dist/video-js.css";
import { LocalizationProvider, TimeField } from "@mui/x-date-pickers";
import { DemoContainer } from "@mui/x-date-pickers/internals/demo";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { SlaveInstance, UploadInstance } from "../api/axios";
import Mleft from "../components/Fragments/Mleft";
import Mmiddle from "../components/Fragments/Mmiddle";

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

  const handleVideoDelete = async () => {
    if (window.confirm("정말 삭제하시겠습니까?")) {
      await UploadInstance.delete(`/upload-service/videos/${userId}/${videoId}`)
        .then((res) => {
          console.log(res);
          window.location.reload();
        })
        .catch((err) => {
          console.log(err);
        });
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
    if (videoRef.current !== null) {
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
      // video.js 소멸
      return () => {
        if (player) {
          player.dispose();
        }
      };
    }
  }, [videoUrl]);

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
            videoUrl={videoUrl}
            videoRef={videoRef}
            selectedQuality={selectedQuality}
            regionTag={regionTag}
            themeTag={themeTag}
          />
        </M.LeftMiddleBox>
        <M.RightBox>
          <U.TitleLeftBox>
            <U.SpanTitle>광고수정</U.SpanTitle>
            <U.AdUploadButton>
              <U.FullIcon src={PlusButton} alt="plus-button" loading="lazy" />
            </U.AdUploadButton>
          </U.TitleLeftBox>
          <M.AdModifySection>
            <U.AdInputSection>
              <U.TimeBox>
                <LocalizationProvider dateAdapter={AdapterDayjs}>
                  <DemoContainer components={["TimeField"]}>
                    <TimeField
                      label="시작 시간"
                      onChange={(e) => {
                        const receivedTime = new Date(e.$d);
                        const hours = receivedTime.getHours();
                        const minutes = receivedTime.getMinutes();
                        const seconds = receivedTime.getSeconds();
                        const totalMilliseconds = (hours * 3600 + minutes * 60 + seconds) * 1000;
                        // setStartTime(totalMilliseconds.toString());
                      }}
                      format="HH:mm:ss"
                      color="success"
                    />
                    <TimeField
                      label="종료 시간"
                      onChange={(e) => {
                        const receivedTime = new Date(e.$d);
                        const hours = receivedTime.getHours();
                        const minutes = receivedTime.getMinutes();
                        const seconds = receivedTime.getSeconds();
                        const totalMilliseconds = (hours * 3600 + minutes * 60 + seconds) * 1000;
                        // setEndTime(totalMilliseconds.toString());
                      }}
                      format="HH:mm:ss"
                      color="success"
                    />
                  </DemoContainer>
                </LocalizationProvider>
              </U.TimeBox>
              <U.ContentBox>
                <U.NormalSpan>내용</U.NormalSpan>
                <U.AdInput
                  type="text"
                  placeholder="수정할 광고 내용을 입력해주세요."
                  width="85%"
                  height="100px"
                  // onChange={(e) => setAdContent(e.target.value)}
                />
              </U.ContentBox>
              <U.LinkBox>
                <U.NormalSpan>링크</U.NormalSpan>
                <U.AdInput
                  type="text"
                  placeholder="수정할 광고 링크를 첨부해주세요."
                  width="85%"
                  height="35px"
                  // onChange={(e) => setAdUrl(e.target.value)}
                />
              </U.LinkBox>
              <U.RemoveButton>
                <U.SmallImage src={Minus} alt="minus" />
              </U.RemoveButton>
            </U.AdInputSection>
          </M.AdModifySection>
          <U.TitleLeftBox>
            <U.SpanTitle>댓글조회</U.SpanTitle>
          </U.TitleLeftBox>
          <M.AdCommentEmptySection>
            <M.BoldSpan>영상 선택 후 댓글을 조회할 수 있습니다.</M.BoldSpan>
          </M.AdCommentEmptySection>
        </M.RightBox>
      </Body>
    </GridWrapper>
  );
};

export default Manage;
