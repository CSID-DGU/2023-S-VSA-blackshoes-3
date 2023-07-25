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
  TagCheckSection,
  TagItemBox,
  TagScrollBox,
  TagTitle,
  TagWrapper,
  ThumbnailImage,
  TitleInput,
  TitleLeftBox,
  TitleThumbnailWrapper,
  TitleWrapper,
  VideoThumbnailSection,
  VideoThumbnailUploadButton,
  VideoThumbnailUploadInput,
  VideoUploadSection,
} from "../components/Home/UploadStyle";
import Plus from "../assets/images/plus.svg";
import PlusButton from "../assets/images/plus-button.svg";
import axios from "axios";
import UploadComponent from "../components/Fragments/UploadComponent";

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
  const [step, setStep] = useState({
    first: true,
    second: false,
  });

  //
  const [videoTitle, setVideoTitle] = useState("");
  const [thumbnailFile, setThumbnailFile] = useState(null);
  const [thumbnailPreview, setThumbnailPreview] = useState(null);
  const [regionTag, setRegionTag] = useState([]);
  const [themeTag, setThemeTag] = useState([]);

  const handleVideoTitle = (e) => setVideoTitle(e.target.value);
  const handleThumbnailFile = (e) => {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setThumbnailPreview(reader.result);
      };
      reader.readAsDataURL(file);
      setThumbnailFile(file);
    } else {
      setThumbnailPreview(null);
    }
  };

  // Function----------------------------------------------------
  const fetchData = async () => {
    const regionData = await axios.get(`http://13.125.69.94:8021/upload-service/tags/region`);
    const themeData = await axios.get(`http://13.125.69.94:8021/upload-service/tags/theme`);
    setRegionTag(regionData.data.payload.tags);
    setThemeTag(themeData.data.payload.tags);
  };

  const handleCheckBox = (e) => {};

  // ComponentDidMount-------------------------------------------
  useEffect(() => {
    setPage(1);
    fetchData();
  }, []);

  return (
    <GridWrapper>
      <Header />
      <Nav />
      <Body>
        <ResNav userId={userId} />
        <VideoUploadSection>
          {/* 영상 업로드 컴포넌트 조각 */}
          <UploadComponent userId={userId} step={step} setStep={setStep} />
          <br />
          {/* 영상 정보 등록 컴포넌트 조각 */}
          <InfoInputSection>
            {!step.second && <Shadow>STEP 2</Shadow>}
            <InfoTitleBox>
              <TitleWrapper>
                <SmallTitle>제목</SmallTitle>
                <TitleInput
                  type="text"
                  placeholder="제목을 입력해주세요."
                  onChange={handleVideoTitle}
                />
              </TitleWrapper>
              <TitleThumbnailWrapper>
                <SmallTitle>썸네일</SmallTitle>
                <VideoThumbnailSection>
                  <VideoThumbnailUploadInput
                    type="file"
                    accept="image/*"
                    id="thumbnail-input"
                    onChange={handleThumbnailFile}
                  />
                  <VideoThumbnailUploadButton
                    htmlFor="thumbnail-input"
                    thumbnailfile={thumbnailFile}
                  >
                    <FullIcon src={Plus} alt="plus-icon" loading="lazy" />
                  </VideoThumbnailUploadButton>
                  {thumbnailPreview && (
                    <ThumbnailImage src={thumbnailPreview} alt="thumbnail-image" loading="lazy" />
                  )}
                </VideoThumbnailSection>
              </TitleThumbnailWrapper>
            </InfoTitleBox>
            <InfoTagBox>
              <SmallTitle>태그</SmallTitle>
              <TagWrapper>
                <TagCheckSection>
                  <TagTitle>지역 태그</TagTitle>
                  <TagScrollBox>
                    <TagItemBox>지역 태그1</TagItemBox>
                  </TagScrollBox>
                </TagCheckSection>
                <TagCheckSection>
                  <TagTitle>테마 태그</TagTitle>
                  <TagScrollBox>
                    <TagItemBox>테마 태그1</TagItemBox>
                  </TagScrollBox>
                </TagCheckSection>
              </TagWrapper>
            </InfoTagBox>
          </InfoInputSection>
        </VideoUploadSection>
        <AdUploadSection>
          {!step.second && <Shadow>STEP 2</Shadow>}
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
