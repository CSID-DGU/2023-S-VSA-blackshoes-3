import { useNavigate, useParams } from "react-router-dom";
import Header from "../components/Fragments/Header";
import Nav from "../components/Fragments/Nav";
import { Body, GridWrapper } from "../components/Home/HomeStyle";
import { useContext, useEffect, useState } from "react";
import { GlobalContext } from "../context/GlobalContext";
import ResNav from "../components/Fragments/ResNav";
import * as M from "../components/Home/ManageStyle";
import * as U from "../components/Home/UploadStyle";
import { ColorButton } from "../components/Sign/SignStyle";
import PlusButton from "../assets/images/plus-button.svg";
import axios from "axios";

const Manage = () => {
  // Constant----------------------------------------------------
  const navigate = useNavigate();
  const { userId } = useParams();

  // State-------------------------------------------------------
  const { setPage } = useContext(GlobalContext);
  const [videoList, setVideoList] = useState([]);

  // Function----------------------------------------------------
  const fetchData = async () => {
    try {
      const videoData = await axios.get(
        `http://13.125.69.94:8011/content-slave-service/videos/${userId}/sort?s=recent&page=0&size=10`
      );
      setVideoList(videoData.data.payload.videos);
    } catch (err) {
      console.log(err);
    }
  };

  // ComponentDidMount-------------------------------------------
  useEffect(() => {
    setPage(2);
    fetchData();
  }, []);

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
            </U.TitleBetweenBox>
            <M.VideoListWrapper>
              {videoList.map((v) => (
                <M.VideoListBox key={v.videoId}>
                  <M.VideoListThumbnail src={v.thumbnailUrl} alt="video-thumbnail" loading="lazy" />
                  <M.VideoListInfo>{v.videoName}</M.VideoListInfo>
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
