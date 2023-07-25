import { useContext, useEffect, useState } from "react";
import Header from "../components/Fragments/Header";
import Nav from "../components/Fragments/Nav";
import { Body, GridWrapper } from "../components/Home/HomeStyle";
import { useNavigate, useParams } from "react-router-dom";
import { GlobalContext } from "../context/GlobalContext";
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
  Shadow,
  SmallTitle,
  SpanTitle,
  SpinnerBox,
  TagCheckSection,
  TagWrapper,
  TitleBetweenBox,
  TitleInput,
  TitleLeftBox,
  TitleThumbnailWrapper,
  TitleWrapper,
  VideoInput,
  VideoInputSection,
  VideoPreview,
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
import HashLoader from "react-spinners/HashLoader";

// Upload EC2
// 13.125.69.94:8021
// Content-Slave
// 13.125.69.94:8011

const Upload = () => {
  // Constant----------------------------------------------------
  const navigate = useNavigate();
  const { userId } = useParams();

  // State-------------------------------------------------------
  const { setPage } = useContext(GlobalContext);
  const [loading, setLoading] = useState(false);
  const [videoFile, setVideoFile] = useState(null);
  const [preview, setPreview] = useState(null);

  const [step, setStep] = useState({
    first: true,
    second: false,
    third: false,
  });

  //
  const [thumbnailfile, setThumbnailfile] = useState(null);
  const [regionTag, setRegionTag] = useState([]);
  const [themeTag, setThemeTag] = useState([]);

  // Function----------------------------------------------------
  const handleVideoChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setPreview(reader.result);
      };
      reader.readAsDataURL(file);
      setVideoFile(file);
    } else {
      setVideoFile(null);
      alert("파일을 등록하는데 실패했습니다.");
    }
  };

  const handleVideoUpload = async () => {
    if (videoFile) {
      try {
        setLoading(true);
        const formData = new FormData();
        formData.append("video", videoFile);
        await axios
          .post(`http://13.125.69.94:8021/upload-service/videos/${userId}`, formData, {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          })
          .then((res) => {
            console.log(res);
            setLoading(false);
            alert("영상이 업로드되었습니다.");
            setStep({
              ...step,
              second: true,
            });
          });
      } catch (err) {
        console.log(err);
        setLoading(false);
        alert("업로드에 실패했습니다.");
      }
    }
  };

  // ComponentDidMount-------------------------------------------
  useEffect(() => {
    setPage(1);
    // await axios.get(`http://13.125.69.94:8021/upload-service/tags/${type}`);
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
            <ColorButton width="65px" style={{ height: "35px" }} onClick={handleVideoUpload}>
              다음
            </ColorButton>
          </TitleBetweenBox>
          <VideoInput type="file" accept="video/*" id="video-input" onChange={handleVideoChange} />
          <VideoInputSection>
            <VideoUploadButton htmlFor="video-input" videofile={videoFile}>
              <FullIcon src={Plus} alt="plus-icon" loading="lazy" />
            </VideoUploadButton>
            {loading && (
              <SpinnerBox>
                <HashLoader color="#1DAE86" />
              </SpinnerBox>
            )}
            {preview && (
              <VideoPreview controls>
                <source src={preview} type="video/mp4" />
              </VideoPreview>
            )}
          </VideoInputSection>
          <br />
          <InfoInputSection>
            {!step.second && <Shadow>STEP 2</Shadow>}
            <InfoTitleBox>
              <TitleWrapper>
                <SmallTitle>제목</SmallTitle>
                <TitleInput type="text" placeholder="제목을 입력해주세요." />
              </TitleWrapper>
              <TitleThumbnailWrapper>
                <SmallTitle>썸네일</SmallTitle>
                <VideoThumbnailSection>
                  <VideoThumbnailUploadInput type="file" id="thumbnail-input" />
                  <VideoThumbnailUploadButton
                    htmlFor="thumbnail-input"
                    thumbnailfile={thumbnailfile}
                  >
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
          {!step.third && <Shadow>STEP 3</Shadow>}
          <TitleLeftBox>
            <SpanTitle>광고등록</SpanTitle>
            <AdUploadButton>
              <FullIcon src={PlusButton} alt="plus-button" loading="lazy" />
            </AdUploadButton>
          </TitleLeftBox>
          <AdUploadGridBox>
            <AdInputSection></AdInputSection>
          </AdUploadGridBox>
        </AdUploadSection>
      </Body>
    </GridWrapper>
  );
};

export default Upload;
