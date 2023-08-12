import { useState } from "react";
import * as I from "../Upload/UploadStyle";
import Plus from "../../../assets/images/plus.svg";
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
    <I.InfoInputSection>
      {/* {step.second && <Shadow>STEP 2</Shadow>} */}
      <I.InfoTitleBox>
        <I.TitleWrapper>
          <I.SmallTitle>제목</I.SmallTitle>
          <I.TitleInput
            type="text"
            placeholder="제목을 입력해주세요."
            onChange={(e) => setVideoName(e.target.value)}
          />
        </I.TitleWrapper>
        <I.TitleThumbnailWrapper>
          <I.SmallTitle>썸네일</I.SmallTitle>
          <I.VideoThumbnailSection>
            <I.VideoThumbnailUploadInput
              type="file"
              accept="image/*"
              id="thumbnail-input"
              onChange={handleThumbnailFile}
            />
            <I.VideoThumbnailUploadButton htmlFor="thumbnail-input" $thumbnail_file={thumbnailFile}>
              <I.FullIcon src={Plus} alt="plus-icon" loading="lazy" />
            </I.VideoThumbnailUploadButton>
            {thumbnailPreview && (
              <I.ThumbnailImage src={thumbnailPreview} alt="thumbnail-image" loading="lazy" />
            )}
          </I.VideoThumbnailSection>
        </I.TitleThumbnailWrapper>
      </I.InfoTitleBox>
      <I.InfoTagBox>
        <I.SmallTitle>태그</I.SmallTitle>
        <I.TagWrapper>
          <I.TagCheckSection>
            <I.TagTitle>지역 태그</I.TagTitle>
            <I.TagScrollBox>
              {regionTag.map((region) => (
                <I.TagItemBox key={region.tagId}>
                  <I.NormalSpan>{region.content}</I.NormalSpan>
                  <I.CheckBoxInput
                    type="checkbox"
                    id="checkbox"
                    onChange={() => {
                      setTagIdList([...tagIdList, region.tagId]);
                    }}
                  />
                </I.TagItemBox>
              ))}
            </I.TagScrollBox>
          </I.TagCheckSection>
          <I.TagCheckSection>
            <I.TagTitle>테마 태그</I.TagTitle>
            <I.TagScrollBox>
              {themeTag.map((theme) => (
                <I.TagItemBox key={theme.tagId}>
                  <I.NormalSpan>{theme.content}</I.NormalSpan>
                  <I.CheckBoxInput
                    type="checkbox"
                    id="checkbox"
                    onChange={() => {
                      setTagIdList([...tagIdList, theme.tagId]);
                    }}
                  />
                </I.TagItemBox>
              ))}
            </I.TagScrollBox>
          </I.TagCheckSection>
        </I.TagWrapper>
      </I.InfoTagBox>
    </I.InfoInputSection>
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
