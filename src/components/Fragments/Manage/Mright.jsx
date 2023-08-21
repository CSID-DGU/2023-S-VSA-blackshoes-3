import { useEffect, useState } from "react";
import * as M from "./ManageStyle";
import * as U from "../Upload/UploadStyle";
import PlusButton from "../../../assets/images/plus-button.svg";
import PropTypes from "prop-types";
import { ColorButton } from "../../Sign/SignStyle";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTrash } from "@fortawesome/free-solid-svg-icons";
import { Instance } from "../../../api/axios";
import ManageAdInput from "../Reusable/ManageAdInput";

const Mright = ({ videoAds, userId, videoId, videoComments }) => {
  // Constant----------------------------------------------------

  // State-------------------------------------------------------
  const [adInputs, setAdInputs] = useState([]);

  // Function----------------------------------------------------
  const removeComment = async (commentId) => {
    if (window.confirm("댓글을 삭제하시겠습니까?")) {
      try {
        await Instance.delete(
          `comment-service/comments/${userId}/${commentId}`
        ).then((res) => {
          alert("댓글을 삭제했습니다.");
        });
      } catch (err) {
        alert("댓글 삭제에 실패했습니다.");
      }
    }
  };

  // ComponentDidMount--------------------------------------------
  useEffect(() => {
    setAdInputs(videoAds);
  }, [videoAds]);

  return (
    <M.RightBox>
      <U.TitleBetweenBox style={{ borderBottom: `1px solid #c4c4c4` }}>
        <U.SpanTitle>
          광고수정{" "}
          <U.AdUploadButton>
            <U.FullIcon src={PlusButton} alt="plus-button" loading="lazy" />
          </U.AdUploadButton>
        </U.SpanTitle>
        <ColorButton width="70px" style={{ height: "35px" }}>
          변경
        </ColorButton>
      </U.TitleBetweenBox>
      <M.AdModifySection>
        {videoAds.length === 0 ? (
          <M.EmptyCenterBox>
            <M.BoldSpan>영상 선택 후 광고를 수정할 수 있습니다.</M.BoldSpan>
          </M.EmptyCenterBox>
        ) : (
          videoAds.map((params) => (
            <ManageAdInput
              key={params.adId}
              adId={params.adId}
              userId={userId}
              videoId={videoId}
              adContent={params.adContent}
              adUrl={params.adUrl}
              adStartTime={params.startTime}
              adEndTime={params.endTime}
              adInputs={adInputs}
              setAdInputs={setAdInputs}
            />
          ))
        )}
      </M.AdModifySection>
      <U.TitleLeftBox>
        <U.SpanTitle>댓글조회</U.SpanTitle>
      </U.TitleLeftBox>
      {videoComments.length === 0 ? (
        <M.AdCommentEmptySection>
          <M.BoldSpan>영상 선택 후 댓글을 조회할 수 있습니다.</M.BoldSpan>
        </M.AdCommentEmptySection>
      ) : (
        <M.AdCommentSection>
          {videoComments.map((params) => (
            <M.CommentBox key={params.commentId}>
              <M.CommentLeftBox>
                <M.CommentContent>
                  {params.nickname} {params.createdAt.slice(0, 10)}
                </M.CommentContent>
                <M.CommentContent>{params.content}</M.CommentContent>
              </M.CommentLeftBox>
              <M.CommentRightBox
                onClick={() => removeComment(params.commentId)}
              >
                <FontAwesomeIcon icon={faTrash} />
              </M.CommentRightBox>
            </M.CommentBox>
          ))}
        </M.AdCommentSection>
      )}
    </M.RightBox>
  );
};

export default Mright;

Mright.propTypes = {
  videoAds: PropTypes.array,
  userId: PropTypes.string,
  videoId: PropTypes.string,
  videoComments: PropTypes.array,
};
