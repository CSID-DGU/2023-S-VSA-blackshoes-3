import * as M from "../../components/Home/ManageStyle";
import * as U from "../../components/Home/UploadStyle";
import { LogoCircleBox } from "../../components/Home/HomeStyle";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBoxOpen, faEye, faHeart } from "@fortawesome/free-solid-svg-icons";
import PropTypes from "prop-types";

const Mleft = ({ videoList, videoId, setVideoId, setSortOption }) => {
  // Constant----------------------------------------------------

  // State-------------------------------------------------------

  // Function----------------------------------------------------
  const handleSortChange = (e) => {
    const selectedOption = e.target.value;
    if (selectedOption === "최신순") {
      setSortOption("최신순");
    } else if (selectedOption === "조회수순") {
      setSortOption("조회수순");
    } else if (selectedOption === "좋아요순") {
      setSortOption("좋아요순");
    }
  };

  const handleVideoModifyFile = (videoId) => {
    setVideoId(videoId);
  };

  return (
    <M.LeftBox>
      <U.TitleBetweenBox>
        <U.SpanTitle>영상목록</U.SpanTitle>
        <M.Select onChange={handleSortChange}>
          <option value="최신순">최신순</option>
          <option value="조회수순">조회수순</option>
          <option value="좋아요순">좋아요순</option>
        </M.Select>
      </U.TitleBetweenBox>
      <M.VideoListWrapper>
        {videoList.length === 0 ? (
          <M.VideoEmptySection>
            <M.LargeFont icon={faBoxOpen} />
            <M.BoldSpan>업로드된 영상이 없습니다.</M.BoldSpan>
          </M.VideoEmptySection>
        ) : (
          videoList.map((v) => (
            <M.VideoListBox
              key={v.videoId}
              onClick={() => handleVideoModifyFile(v.videoId)}
              clickedid={videoId}
              videoid={v.videoId}
            >
              <M.VideoListThumbnail src={v.thumbnailUrl} alt="video-thumbnail" loading="lazy" />
              <M.VideoListInfo>
                <LogoCircleBox>
                  {/* <M.LogoImage src={v.sellorLogo} alt="Sellor-Logo" loading="lazy" /> */}
                  LOGO
                </LogoCircleBox>
                <M.InfoRightWrapper>
                  <M.InfoRightBox>
                    <M.InfoSpan>{v.videoName}</M.InfoSpan>
                    <M.InfoSpan>
                      <FontAwesomeIcon icon={faEye} /> {v.views}
                    </M.InfoSpan>
                  </M.InfoRightBox>
                  <M.InfoRightBox>
                    <M.InfoSpan>{v.createdAt?.slice(0, 10)}</M.InfoSpan>
                    <M.InfoSpan>
                      <M.GreenSpan icon={faHeart} /> {v.likes}
                    </M.InfoSpan>
                  </M.InfoRightBox>
                </M.InfoRightWrapper>
              </M.VideoListInfo>
            </M.VideoListBox>
          ))
        )}
      </M.VideoListWrapper>
    </M.LeftBox>
  );
};

export default Mleft;

Mleft.propTypes = {
  videoList: PropTypes.array,
  videoId: PropTypes.string,
  setVideoId: PropTypes.func,
  setSortOption: PropTypes.func,
};
