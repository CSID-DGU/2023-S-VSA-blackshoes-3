import { useEffect, useState } from "react";
import AdInput from "../Reusable/AdInput";
import * as M from "./ManageStyle";
import * as U from "../Upload/UploadStyle";
import PlusButton from "../../../assets/images/plus-button.svg";
import PropTypes from "prop-types";
import { ColorButton } from "../../Sign/SignStyle";

const Mright = ({ videoAds, userId, videoId }) => {
  // Constant----------------------------------------------------

  // State-------------------------------------------------------

  // Function----------------------------------------------------

  // ComponentDidMount--------------------------------------------
  useEffect(() => {
    //
  }, []);
  console.log(videoAds);
  return (
    <M.RightBox>
      <U.TitleBetweenBox>
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
        {videoAds.length === 0
          ? "추가된 광고가 없습니다."
          : videoAds.map((params) => (
              <AdInput
                key={params.adId}
                adId={params.adId}
                userId={userId}
                videoId={videoId}
                adContent={params.adContent}
                adUrl={params.adUrl}
                adStartTime={params.startTime}
                adEndTime={params.endTime}
              />
            ))}
      </M.AdModifySection>
      <U.TitleLeftBox>
        <U.SpanTitle>댓글조회</U.SpanTitle>
      </U.TitleLeftBox>
      <M.AdCommentEmptySection>
        <M.BoldSpan>영상 선택 후 댓글을 조회할 수 있습니다.</M.BoldSpan>
      </M.AdCommentEmptySection>
    </M.RightBox>
  );
};

export default Mright;

Mright.propTypes = {
  videoAds: PropTypes.array,
  userId: PropTypes.string,
  videoId: PropTypes.string,
};
