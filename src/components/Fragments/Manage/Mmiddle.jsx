import { useEffect, useState } from "react";
import * as M from "./ManageStyle";
import * as U from "../Upload/UploadStyle";
import { ColorButton } from "../../Sign/SignStyle";
import { UploadInstance } from "../../../api/axios";
import PropTypes from "prop-types";

const Mmiddle = ({
  userId,
  videoId,
  videoName,
  setVideoName,
  videoUrl,
  videoRef,
  videoThumbnail,
  setVideoThumbnail,
  videoTags,
  selectedQuality,
  regionTag,
  themeTag,
}) => {
  // Constant----------------------------------------------------

  // State-------------------------------------------------------
  const [newTagIdList, setNewTagIdList] = useState([]);
  const [thumbnailPreview, setThumbnailPreview] = useState(null);
  const [thumbnailModifiedFile, setThumbnailModifiedFile] = useState(null);

  // Function----------------------------------------------------
  const handleVideoDelete = async () => {
    if (window.confirm("정말 삭제하시겠습니까?")) {
      await UploadInstance.delete(`/upload-service/videos/${userId}/${videoId}`)
        .then((res) => {
          console.log(res);
          window.location.reload();
        })
        .catch((err) => {
          console.log(err);
        });
    } else {
      return;
    }
  };

  const handleVideoName = (e) => {
    setVideoName(e.target.value);
  };

  const submitVideoName = async () => {
    try {
      await UploadInstance.put(`/upload-service/videos/${userId}/${videoId}/title`, {
        videoName,
      }).then(() => {
        alert("제목이 수정되었습니다.");
      });
    } catch (err) {
      console.log(err);
    }
  };

  const previewTimer = () => {
    setTimeout(() => {
      setThumbnailPreview(null);
    }, 3000);
  };

  const handleVideoThumbnail = (e) => {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setThumbnailPreview(reader.result);
      };
      reader.readAsDataURL(file);
      setThumbnailModifiedFile(file);
      if (typeof file === "object") {
        setVideoThumbnail(file.name);
      } else {
        setVideoThumbnail(file);
      }
      previewTimer();
    } else {
      setVideoThumbnail(null);
      alert("파일을 등록하는데 실패했습니다.");
    }
  };

  const submitVideoThumbnail = async () => {
    if (window.confirm("썸네일을 수정하시겠습니까?")) {
      try {
        const formData = new FormData();
        formData.append("thumbnail", thumbnailModifiedFile);
        await UploadInstance.put(
          `/upload-service/videos/${userId}/${videoId}/thumbnail`,
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          }
        ).then((res) => {
          console.log(res);
          alert("썸네일이 수정되었습니다.");
        });
      } catch (err) {
        console.log(err);
      }
    } else {
      return;
    }
  };

  const handleVideoTag = (tagId) => {
    if (newTagIdList.includes(tagId)) {
      setNewTagIdList(newTagIdList.filter((n) => n !== tagId));
    } else {
      setNewTagIdList([...newTagIdList, tagId]);
    }
  };

  const submitVideoTag = async () => {
    if (window.confirm("태그를 수정하시겠습니까?")) {
      try {
        await UploadInstance.put(`/upload-service/videos/${userId}/${videoId}/tags`, {
          tagIds: newTagIdList,
        }).then((res) => {
          console.log(res);
          alert("태그가 수정되었습니다.");
        });
      } catch (err) {
        console.log(err);
      }
    } else {
      return;
    }
  };

  // ComponentDidMount--------------------------------------------
  useEffect(() => {
    setNewTagIdList(videoTags.map((v) => v.tagId));
  }, [videoTags]);

  return (
    <M.MiddleBox>
      <M.PreviewSection $thumbnail_preview={thumbnailPreview}>
        <M.ThumbnailPreview src={thumbnailPreview} alt="thumbnail_preview" loading="lazy" />
      </M.PreviewSection>
      <U.TitleBetweenBox>
        <U.SpanTitle>영상 정보 수정</U.SpanTitle>
        <ColorButton width="70px" style={{ height: "35px" }} onClick={handleVideoDelete}>
          삭제
        </ColorButton>
      </U.TitleBetweenBox>
      <M.VideoModifyWrapper $videourl={videoUrl}>
        수정을 원하는 영상을 클릭해주세요.
      </M.VideoModifyWrapper>
      {videoUrl && (
        <M.VideoModify controls ref={videoRef} className="video-js vjs-default-skin">
          <M.VideoSource src={`${videoUrl}/${selectedQuality}.m3u8`} type="application/x-mpegURL" />
        </M.VideoModify>
      )}
      <M.InfoModify>
        <M.InfoFlexBox>
          <M.InfoVerticalBox>
            <M.SecondBlackP>제목</M.SecondBlackP>
            <M.CenterBox>
              <M.FileTextInput type="text" defaultValue={videoName} onChange={handleVideoName} />
              <M.ExchangeButton onClick={submitVideoName}>변경</M.ExchangeButton>
            </M.CenterBox>
          </M.InfoVerticalBox>
          <M.InfoVerticalBox>
            <M.SecondBlackP>
              썸네일
              <M.InputButton htmlFor="file-input">File</M.InputButton>
            </M.SecondBlackP>
            <M.CenterBox>
              <M.FileTextInput type="text" defaultValue={videoThumbnail} />
              <M.FileInput
                type="file"
                id="file-input"
                accept="image/*"
                onChange={handleVideoThumbnail}
              />
              <M.ExchangeButton onClick={submitVideoThumbnail}>변경</M.ExchangeButton>
            </M.CenterBox>
          </M.InfoVerticalBox>
        </M.InfoFlexBox>
        <M.SecondBlackP>
          태그 <M.InputButton onClick={submitVideoTag}>변경</M.InputButton>
        </M.SecondBlackP>
        <M.TagSection>
          <U.TagCheckSection>
            <U.TagTitle>지역 태그</U.TagTitle>
            <U.TagScrollBox>
              {regionTag.map((region) => (
                <U.TagItemBox key={region.tagId}>
                  <U.NormalSpan>{region.content}</U.NormalSpan>
                  {videoTags.length !== 0 ? (
                    <U.CheckBoxInput
                      type="checkbox"
                      checked={newTagIdList.includes(region.tagId)}
                      onChange={() => handleVideoTag(region.tagId)}
                    />
                  ) : (
                    <U.CheckBoxInput
                      type="checkbox"
                      checked={false}
                      onChange={(e) => e.preventDefault()}
                      disabled
                    />
                  )}
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
                  {videoTags.length !== 0 ? (
                    <U.CheckBoxInput
                      type="checkbox"
                      id="checkbox"
                      checked={newTagIdList.includes(theme.tagId)}
                      onChange={() => handleVideoTag(theme.tagId)}
                    />
                  ) : (
                    <U.CheckBoxInput
                      type="checkbox"
                      checked={false}
                      onChange={(e) => e.preventDefault()}
                      disabled
                    />
                  )}
                </U.TagItemBox>
              ))}
            </U.TagScrollBox>
          </U.TagCheckSection>
        </M.TagSection>
      </M.InfoModify>
    </M.MiddleBox>
  );
};

export default Mmiddle;

Mmiddle.propTypes = {
  userId: PropTypes.string,
  videoId: PropTypes.string,
  videoName: PropTypes.string,
  setVideoName: PropTypes.func,
  videoUrl: PropTypes.string,
  videoRef: PropTypes.object,
  videoThumbnail: PropTypes.string,
  setVideoThumbnail: PropTypes.func,
  videoTags: PropTypes.array,
  selectedQuality: PropTypes.string,
  regionTag: PropTypes.array,
  themeTag: PropTypes.array,
};
