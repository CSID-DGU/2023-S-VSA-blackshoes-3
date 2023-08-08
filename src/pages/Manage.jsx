import { useNavigate, useParams } from "react-router-dom";
import Header from "../components/Fragments/Header";
import Nav from "../components/Fragments/Nav";
import { Body, GridWrapper, LogoCircleBox } from "../components/Home/HomeStyle";
import { useContext, useEffect, useRef, useState } from "react";
import { GlobalContext } from "../context/GlobalContext";
import ResNav from "../components/Fragments/ResNav";
import * as M from "../components/Home/ManageStyle";
import * as U from "../components/Home/UploadStyle";
import { ColorButton } from "../components/Sign/SignStyle";
import PlusButton from "../assets/images/plus-button.svg";
import Minus from "../assets/images/minus.svg";
import axios from "axios";
import { faBoxOpen, faEye, faHeart } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import videojs from "video.js";
import "video.js/dist/video-js.css";
import { LocalizationProvider, TimeField } from "@mui/x-date-pickers";
import { DemoContainer } from "@mui/x-date-pickers/internals/demo";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";

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
  const [newTagIdList, setNewTagIdList] = useState([]);

  // Function----------------------------------------------------
  const fetchData = async () => {
    // 기본 태그 가져오기
    const regionData = await axios.get(`http://13.125.69.94:8021/upload-service/tags/region`);
    const themeData = await axios.get(`http://13.125.69.94:8021/upload-service/tags/theme`);
    setRegionTag(regionData.data.payload.tags);
    setThemeTag(themeData.data.payload.tags);
    try {
      // 비디오 리스트 가져오기
      if (sortOption === "최신순") {
        const videoRecentData = await axios.get(
          `http://13.125.69.94:8011/content-slave-service/videos/${userId}/sort?s=recent&page=0&size=10`
        );
        setVideoList(videoRecentData.data.payload.videos);
      } else if (sortOption === "조회수순") {
        const videoViewData = await axios.get(
          `http://13.125.69.94:8011/content-slave-service/videos/${userId}/sort?s=views&page=0&size=10`
        );
        setVideoList(videoViewData.data.payload.videos);
      } else if (sortOption === "좋아요순") {
        const videoLikeData = await axios.get(
          `http://13.125.69.94:8011/content-slave-service/videos/${userId}/sort?s=likes&page=0&size=10`
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
      await axios
        .get(
          `http://13.125.69.94:8011/content-slave-service/videos/video?type=videoId&q=${videoId}`
        )
        .then((res) => {
          setVideoUrl(res.data.payload.video.videoUrl);
          setVideoName(res.data.payload.video.videoName);
          setVideoThumbnail(res.data.payload.video.thumbnailUrl);
          setVideoTags(res.data.payload.video.videoTags);
          setVideoAds(res.data.payload.video.videoAds);
        });
    } else {
      return;
    }
  };

  const handleSortChange = (e) => {
    const selectedOption = e.target.value;
    if (selectedOption === "최신순") {
      setSortOption("최신순");
    } else if (selectedOption === "조회수순") {
      setSortOption("조회수순");
    } else if (selectedOption === "좋아요순") {
      setSortOption("좋아요순");
    }
  };

  const handleVideoModifyFile = (videoId) => {
    setVideoId(videoId);
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
          <M.LeftBox>
            <U.TitleBetweenBox>
              <U.SpanTitle>영상목록</U.SpanTitle>
              <M.Select onChange={handleSortChange}>
                <option value="최신순">최신순</option>
                <option value="조회수순">조회수순</option>
                <option value="좋아요순">좋아요순</option>
              </M.Select>
            </U.TitleBetweenBox>
            <M.VideoListWrapper>
              {videoList.length === 0 ? (
                <M.VideoEmptySection>
                  <M.LargeFont icon={faBoxOpen} />
                  <M.BoldSpan>업로드된 영상이 없습니다.</M.BoldSpan>
                </M.VideoEmptySection>
              ) : (
                videoList.map((v) => (
                  <M.VideoListBox
                    key={v.videoId}
                    onClick={() => handleVideoModifyFile(v.videoId)}
                    clickedid={videoId}
                    videoid={v.videoId}
                  >
                    <M.VideoListThumbnail
                      src={v.thumbnailUrl}
                      alt="video-thumbnail"
                      loading="lazy"
                    />
                    <M.VideoListInfo>
                      <LogoCircleBox>
                        {/* <M.LogoImage src={v.sellorLogo} alt="Sellor-Logo" loading="lazy" /> */}
                        LOGO
                      </LogoCircleBox>
                      <M.InfoRightWrapper>
                        <M.InfoRightBox>
                          <M.InfoSpan>{v.videoName}</M.InfoSpan>
                          <M.InfoSpan>
                            <FontAwesomeIcon icon={faEye} /> {v.views}
                          </M.InfoSpan>
                        </M.InfoRightBox>
                        <M.InfoRightBox>
                          <M.InfoSpan>{v.createdAt?.slice(0, 10)}</M.InfoSpan>
                          <M.InfoSpan>
                            <M.GreenSpan icon={faHeart} /> {v.likes}
                          </M.InfoSpan>
                        </M.InfoRightBox>
                      </M.InfoRightWrapper>
                    </M.VideoListInfo>
                  </M.VideoListBox>
                ))
              )}
            </M.VideoListWrapper>
          </M.LeftBox>
          <M.MiddelBox>
            <U.TitleBetweenBox>
              <U.SpanTitle>영상 정보 수정</U.SpanTitle>
              <ColorButton width="70px" style={{ height: "35px" }}>
                수정
              </ColorButton>
            </U.TitleBetweenBox>
            <M.VideoModifyWrapper videourl={videoUrl}>
              수정을 원하는 영상을 클릭해주세요.
            </M.VideoModifyWrapper>
            {videoUrl && (
              <M.VideoModify controls ref={videoRef} className="video-js vjs-default-skin">
                <M.VideoSource
                  src={`${videoUrl}/${selectedQuality}.m3u8`}
                  type="application/x-mpegURL"
                />
              </M.VideoModify>
            )}
            <M.InfoModify>
              <M.InfoFlexBox>
                <M.InfoVerticalBox>
                  <M.SecondBlackP>제목</M.SecondBlackP>
                  <U.TitleInput
                    type="text"
                    placeholder="수정할 영상 제목을 입력하세요"
                    style={{ height: "40px" }}
                  />
                </M.InfoVerticalBox>
                <M.InfoVerticalBox>
                  <M.SecondBlackP>썸네일</M.SecondBlackP>
                  <M.CenterBox>
                    <M.FileInput type="file" id="file-input" />
                    <M.FileTextInput type="text" />
                    <M.ExchangeButton htmlFor="file-input">변경</M.ExchangeButton>
                  </M.CenterBox>
                </M.InfoVerticalBox>
              </M.InfoFlexBox>
              <M.SecondBlackP>태그</M.SecondBlackP>
              <M.TagSection>
                <U.TagCheckSection>
                  <U.TagTitle>지역 태그</U.TagTitle>
                  <U.TagScrollBox>
                    {regionTag.map((region) => (
                      <U.TagItemBox key={region.tagId}>
                        <U.NormalSpan>{region.content}</U.NormalSpan>
                        <U.CheckBoxInput
                          type="checkbox"
                          id="checkbox"
                          onChange={() => {
                            setNewTagIdList([...newTagIdList, region.tagId]);
                          }}
                        />
                      </U.TagItemBox>
                    ))}
                  </U.TagScrollBox>
                </U.TagCheckSection>
                <U.TagCheckSection>
                  <U.TagTitle>테마 태그</U.TagTitle>
                  <U.TagScrollBox>
                    {themeTag.map((theme) => (
                      <U.TagItemBox key={theme.tagId}>
                        <U.NormalSpan>{theme.content}</U.NormalSpan>
                        <U.CheckBoxInput
                          type="checkbox"
                          id="checkbox"
                          onChange={() => {
                            setNewTagIdList([...newTagIdList, theme.tagId]);
                          }}
                        />
                      </U.TagItemBox>
                    ))}
                  </U.TagScrollBox>
                </U.TagCheckSection>
              </M.TagSection>
            </M.InfoModify>
          </M.MiddelBox>
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
