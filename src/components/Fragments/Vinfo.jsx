import { useState } from "react";
import {
  CheckBoxInput,
  FullIcon,
  InfoInputSection,
  InfoTagBox,
  InfoTitleBox,
  NormalSpan,
  Shadow,
  SmallTitle,
  TagCheckSection,
  TagItemBox,
  TagScrollBox,
  TagTitle,
  TagWrapper,
  ThumbnailImage,
  TitleInput,
  TitleThumbnailWrapper,
  TitleWrapper,
  VideoThumbnailSection,
  VideoThumbnailUploadButton,
  VideoThumbnailUploadInput,
} from "../Home/UploadStyle";
import Plus from "../../assets/images/plus.svg";
import PropTypes from "prop-types";

const Vinfo = ({
  step,
  regionTag,
  themeTag,
  setVideoName,
  thumbnailFile,
  setThumbnailFile,
  tagIdList,
  setTagIdList,
}) => {
  // State---------------------------------------------------------
  const [thumbnailPreview, setThumbnailPreview] = useState(null);

  // Function------------------------------------------------------
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

  return (
    <InfoInputSection>
      {step.second && <Shadow>STEP 2</Shadow>}
      <InfoTitleBox>
        <TitleWrapper>
          <SmallTitle>제목</SmallTitle>
          <TitleInput
            type="text"
            placeholder="제목을 입력해주세요."
            onChange={(e) => setVideoName(e.target.value)}
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
            <VideoThumbnailUploadButton htmlFor="thumbnail-input" thumbnailfile={thumbnailFile}>
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
              {regionTag.map((region) => (
                <TagItemBox key={region.tagId}>
                  <NormalSpan>{region.content}</NormalSpan>
                  <CheckBoxInput
                    type="checkbox"
                    id="checkbox"
                    onChange={() => {
                      setTagIdList([...tagIdList, region.tagId]);
                    }}
                  />
                </TagItemBox>
              ))}
            </TagScrollBox>
          </TagCheckSection>
          <TagCheckSection>
            <TagTitle>테마 태그</TagTitle>
            <TagScrollBox>
              {themeTag.map((theme) => (
                <TagItemBox key={theme.tagId}>
                  <NormalSpan>{theme.content}</NormalSpan>
                  <CheckBoxInput
                    type="checkbox"
                    id="checkbox"
                    onChange={() => {
                      setTagIdList([...tagIdList, theme.tagId]);
                    }}
                  />
                </TagItemBox>
              ))}
            </TagScrollBox>
          </TagCheckSection>
        </TagWrapper>
      </InfoTagBox>
    </InfoInputSection>
  );
};

export default Vinfo;

Vinfo.propTypes = {
  step: PropTypes.object,
  regionTag: PropTypes.array,
  themeTag: PropTypes.array,
  setVideoName: PropTypes.func,
  thumbnailFile: PropTypes.object,
  setThumbnailFile: PropTypes.func,
  tagIdList: PropTypes.array,
  setTagIdList: PropTypes.func,
};
