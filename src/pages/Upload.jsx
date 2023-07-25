import { useContext, useEffect, useState } from "react";
import Header from "../components/Fragments/Header";
import Nav from "../components/Fragments/Nav";
import { Body, GridWrapper } from "../components/Home/HomeStyle";
import { useNavigate, useParams } from "react-router-dom";
import { Context } from "../Context/Context";
import ResNav from "../components/Fragments/ResNav";
import {
  AdInputSection,
  AdUploadButton,
  AdUploadGridBox,
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
  VideoThumbnailUploadInput,
  VideoUploadButton,
  VideoUploadSection,
} from "../components/Home/UploadStyle";
import { ColorButton } from "../components/Sign/SignStyle";
import Plus from "../assets/images/plus.svg";
import PlusButton from "../assets/images/plus-button.svg";
import axios from "axios";

// Upload EC2
// 13.125.69.94:8021
// Content-Slave
// 13.125.69.94:8011

const Upload = () => {
  // Constant----------------------------------------------------
  const navigate = useNavigate();
  const { userId } = useParams();

  // State-------------------------------------------------------
  const { setPage } = useContext(Context);
  const [videoFile, setVideoFile] = useState({});
  const [preview, setPreview] = useState(null);

  // Function----------------------------------------------------
  const handleVideoChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setPreview(reader.result);
      };
      reader.readAsDataURL(file);
    } else {
      setPreview(null);
      alert("파일을 등록하는데 실패했습니다.");
    }
  };

  const handleVideoUpload = async () => {
    if (preview) {
      try {
        const formData = new FormData();
        formData.append("video", preview);

        const response = await axios.post(
          `http://13.125.69.94:8021/upload-service/videos/${userId}`,
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          }
        );
        console.log(response);
      } catch (err) {
        console.log(err);
      }
    }
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
            <ColorButton
              width="65px"
              style={{ height: "35px" }}
              onClick={handleVideoUpload}
            >
              등록
            </ColorButton>
          </TitleBetweenBox>
          <VideoInput
            type="file"
            accept="video/*"
            id="video-input"
            onChange={handleVideoChange}
          />
          <VideoInputSection>
            <VideoUploadButton htmlFor="video-input" preview={preview}>
              <FullIcon src={Plus} alt="plus-icon" loading="lazy" />
            </VideoUploadButton>
            {preview && (
              <video controls width="100%">
                <source src={preview} type="video/mp4" />
              </video>
            )}
          </VideoInputSection>
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
                  <VideoThumbnailUploadInput type="file" id="thumbnail-input" />
                  <VideoThumbnailUploadButton htmlFor="thumbnail-input">
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
          <AdUploadGridBox>
            <AdInputSection></AdInputSection>
            <AdInputSection></AdInputSection>
            <AdInputSection></AdInputSection>
          </AdUploadGridBox>
        </AdUploadSection>
      </Body>
    </GridWrapper>
  );
};

export default Upload;
