import { useState } from "react";
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
  videoTags,
  videoAds,
  selectedQuality,
  regionTag,
  themeTag,
}) => {
  // Constant----------------------------------------------------

  // State-------------------------------------------------------
  const [newTagIdList, setNewTagIdList] = useState([]);

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
      }).then((res) => {
        console.log(res);
        alert("제목이 수정되었습니다.");
      });
    } catch (err) {
      console.log(err);
    }
  };

  return (
    <M.MiddelBox>
      <U.TitleBetweenBox>
        <U.SpanTitle>영상 정보 수정</U.SpanTitle>
        <ColorButton width="70px" style={{ height: "35px" }} onClick={handleVideoDelete}>
          삭제
        </ColorButton>
      </U.TitleBetweenBox>
      <M.VideoModifyWrapper videourl={videoUrl}>
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
            <M.SecondBlackP>썸네일</M.SecondBlackP>
            <M.CenterBox>
              <M.FileInput type="file" id="file-input" />
              <M.FileTextInput type="text" defaultValue={videoThumbnail} />
              <M.ExchangeButton htmlFor="file-input">변경</M.ExchangeButton>
            </M.CenterBox>
          </M.InfoVerticalBox>
        </M.InfoFlexBox>
        <M.SecondBlackP>태그</M.SecondBlackP>
        <M.TagSection>
          <U.TagCheckSection>
            <U.TagTitle>지역 태그</U.TagTitle>
            <U.TagScrollBox>
              {regionTag.map((region) => (
                <U.TagItemBox key={region.tagId}>
                  <U.NormalSpan>{region.content}</U.NormalSpan>
                  <U.CheckBoxInput
                    type="checkbox"
                    id="checkbox"
                    defaultChecked={videoTags.includes(region.tagId)}
                    onChange={() => {
                      setNewTagIdList([...newTagIdList, region.tagId]);
                    }}
                  />
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
                  <U.CheckBoxInput
                    type="checkbox"
                    id="checkbox"
                    onChange={() => {
                      setNewTagIdList([...newTagIdList, theme.tagId]);
                    }}
                  />
                </U.TagItemBox>
              ))}
            </U.TagScrollBox>
          </U.TagCheckSection>
        </M.TagSection>
      </M.InfoModify>
    </M.MiddelBox>
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
  videoTags: PropTypes.array,
  videoAds: PropTypes.array,
  selectedQuality: PropTypes.string,
  regionTag: PropTypes.array,
  themeTag: PropTypes.array,
};
