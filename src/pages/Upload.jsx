import { useContext, useEffect, useState } from "react";
import Header from "../components/Fragments/Header";
import Nav from "../components/Fragments/Nav";
import { Body, GridWrapper } from "../components/Home/HomeStyle";
import { useNavigate, useParams } from "react-router-dom";
import { Context } from "../Context/Context";
import ResNav from "../components/Fragments/ResNav";
import {
  AdUploadButton,
  AdUploadSection,
  FullIcon,
  InfoInputSection,
  InfoTagBox,
  InfoTitleBox,
  SmallTitle,
  SpanTitle,
  TagCheckSection,
  TagWrapper,
  TitleBetweenBox,
  TitleInput,
  TitleLeftBox,
  TitleThumbnailWrapper,
  TitleWrapper,
  VideoInput,
  VideoInputSection,
  VideoThumbnailSection,
  VideoThumbnailUploadButton,
  VideoUploadButton,
  VideoUploadSection,
} from "../components/Home/UploadStyle";
import { ColorButton } from "../components/Sign/SignStyle";
import Plus from "../assets/images/plus.svg";
import PlusButton from "../assets/images/plus-button.svg";

const Upload = () => {
  // Constant----------------------------------------------------
  const navigate = useNavigate();
  const { userId } = useParams();

  // State-------------------------------------------------------
  const { setPage } = useContext(Context);
  const [videoFile, setVideoFile] = useState({});

  // Function----------------------------------------------------
  const videoUpload = (e) => {
    const videoType = e.target.files[0].type.includes("video");

    setVideoFile({
      url: URL.createObjectURL(e.target.files[0]),
      video: videoType,
    });
    console.log(videoType);
  };

  // ComponentDidMount-------------------------------------------
  useEffect(() => {
    setPage(1);
  }, []);

  return (
    <GridWrapper>
      <Header />
      <Nav />
      <Body>
        <ResNav userId={userId} />
        <VideoUploadSection>
          <TitleBetweenBox>
            <SpanTitle>영상 등록</SpanTitle>
            <ColorButton width="65px" style={{ height: "35px" }} onClick={videoUpload}>
              등록
            </ColorButton>
          </TitleBetweenBox>
          <VideoInput type="file" id="video-input" />
          <VideoInputSection>
            <VideoUploadButton htmlFor="video-input">
              <FullIcon src={Plus} alt="plus-icon" loading="lazy" />
            </VideoUploadButton>
          </VideoInputSection>
          {videoFile.video && <video src={videoFile.url} controls width="100%" />}
          <br />
          <InfoInputSection>
            <InfoTitleBox>
              <TitleWrapper>
                <SmallTitle>제목</SmallTitle>
                <TitleInput type="text" placeholder="제목을 입력해주세요." />
              </TitleWrapper>
              <TitleThumbnailWrapper>
                <SmallTitle>썸네일</SmallTitle>
                <VideoThumbnailSection>
                  <VideoThumbnailUploadButton>
                    <FullIcon src={Plus} alt="plus-icon" loading="lazy" />
                  </VideoThumbnailUploadButton>
                </VideoThumbnailSection>
              </TitleThumbnailWrapper>
            </InfoTitleBox>
            <InfoTagBox>
              <SmallTitle>태그</SmallTitle>
              <TagWrapper>
                <TagCheckSection></TagCheckSection>
                <TagCheckSection></TagCheckSection>
              </TagWrapper>
            </InfoTagBox>
          </InfoInputSection>
        </VideoUploadSection>
        <AdUploadSection>
          <TitleLeftBox>
            <SpanTitle>광고등록</SpanTitle>
            <AdUploadButton>
              <FullIcon src={PlusButton} alt="plus-button" loading="lazy" />
            </AdUploadButton>
          </TitleLeftBox>
        </AdUploadSection>
      </Body>
    </GridWrapper>
  );
};

export default Upload;
