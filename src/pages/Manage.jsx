import { useNavigate, useParams } from "react-router-dom";
import Header from "../components/Fragments/Header";
import Nav from "../components/Fragments/Nav";
import { Body, GridWrapper, LogoCircleBox } from "../components/Home/HomeStyle";
import { useContext, useEffect, useState } from "react";
import { GlobalContext } from "../context/GlobalContext";
import ResNav from "../components/Fragments/ResNav";
import * as M from "../components/Home/ManageStyle";
import * as U from "../components/Home/UploadStyle";
import { ColorButton } from "../components/Sign/SignStyle";
import PlusButton from "../assets/images/plus-button.svg";
import axios from "axios";
import { faEye, faHeart } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

const Manage = () => {
  // Constant----------------------------------------------------
  const navigate = useNavigate();
  const { userId } = useParams();

  // State-------------------------------------------------------
  const { setPage } = useContext(GlobalContext);
  const [videoList, setVideoList] = useState([]);
  const [sortOption, setSortOption] = useState("최신순");

  // Function----------------------------------------------------
  const fetchData = async () => {
    try {
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

  // ComponentDidMount-------------------------------------------
  useEffect(() => {
    setPage(2);
    fetchData();
  }, [sortOption]);

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
              {videoList.map((v) => (
                <M.VideoListBox key={v.videoId}>
                  <M.VideoListThumbnail src={v.thumbnailUrl} alt="video-thumbnail" loading="lazy" />
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
              ))}
            </M.VideoListWrapper>
          </M.LeftBox>
          <M.MiddelBox>
            <U.TitleBetweenBox>
              <U.SpanTitle>영상 수정</U.SpanTitle>
              <ColorButton width="70px" style={{ height: "35px" }}>
                수정
              </ColorButton>
            </U.TitleBetweenBox>
          </M.MiddelBox>
        </M.LeftMiddleBox>
        <M.RightBox>
          <U.TitleLeftBox>
            <U.SpanTitle>광고수정</U.SpanTitle>
            <U.AdUploadButton>
              <U.FullIcon src={PlusButton} alt="plus-button" loading="lazy" />
            </U.AdUploadButton>
          </U.TitleLeftBox>
        </M.RightBox>
      </Body>
    </GridWrapper>
  );
};

export default Manage;
