import { useNavigate, useParams } from "react-router-dom";
import Header from "../components/Fragments/Header";
import Nav from "../components/Fragments/Nav";
import { Body, GridWrapper } from "../components/Home/HomeStyle";
import { useContext, useEffect, useState } from "react";
import { Context } from "../Context/Context";
import ResNav from "../components/Fragments/ResNav";
import {
  LeftBox,
  LeftMiddleBox,
  MiddelBox,
  RightBox,
  VideoListBox,
  VideoListInfo,
  VideoListThumbnail,
  VideoListWrapper,
} from "../components/Home/ManageStyle";
import {
  AdUploadButton,
  FullIcon,
  SpanTitle,
  TitleBetweenBox,
  TitleLeftBox,
} from "../components/Home/UploadStyle";
import { ColorButton } from "../components/Sign/SignStyle";
import PlusButton from "../assets/images/plus-button.svg";
import axios from "axios";

const Manage = () => {
  // Constant----------------------------------------------------
  const navigate = useNavigate();
  const { userId } = useParams();

  // State-------------------------------------------------------
  const { setPage } = useContext(Context);
  const [videoList, setVideoList] = useState([]);

  // Function----------------------------------------------------
  const fetchData = async () => {
    try {
      const videoData = await axios.get(
        `http://13.125.69.94:8011/content-slave-service/videos/${userId}/sort?q=recent&page=0&size=10`
      );
      setVideoList(videoData.data.payload.videos);
    } catch (err) {
      console.log(err);
    }
  };
  console.log(videoList);
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
        <LeftMiddleBox>
          <LeftBox>
            <TitleBetweenBox>
              <SpanTitle>영상목록</SpanTitle>
            </TitleBetweenBox>
            <VideoListWrapper>
              {videoList.map((v) => (
                <VideoListBox key={v.videoId}>
                  <VideoListThumbnail src={v.thumbnailUrl} alt="video-thumbnail" loading="lazy" />
                  <VideoListInfo>{v.videoName}</VideoListInfo>
                </VideoListBox>
              ))}
            </VideoListWrapper>
          </LeftBox>
          <MiddelBox>
            <TitleBetweenBox>
              <SpanTitle>영상 수정</SpanTitle>
              <ColorButton width="70px" style={{ height: "35px" }}>
                수정
              </ColorButton>
            </TitleBetweenBox>
          </MiddelBox>
        </LeftMiddleBox>
        <RightBox>
          <TitleLeftBox>
            <SpanTitle>광고수정</SpanTitle>
            <AdUploadButton>
              <FullIcon src={PlusButton} alt="plus-button" loading="lazy" />
            </AdUploadButton>
          </TitleLeftBox>
        </RightBox>
      </Body>
    </GridWrapper>
  );
};

export default Manage;
