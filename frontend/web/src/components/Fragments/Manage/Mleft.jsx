import * as M from "./ManageStyle";
import * as U from "../Upload/UploadStyle";
import { LogoCircleBox } from "../../Home/HomeStyle";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBoxOpen, faEye, faHeart } from "@fortawesome/free-solid-svg-icons";
import PropTypes from "prop-types";
import { Instance } from "../../../api/axios";
import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";

const Mleft = ({
  videoListData,
  videoId,
  setVideoId,
  sortOption,
  setSortOption,
}) => {
  // Constant----------------------------------------------------
  const { userId } = useParams();

  // State-------------------------------------------------------
  const [newVideoList, setNewVideoList] = useState([videoListData.videos]);

  // 비디오 리스트 정렬--------------------------------------------
  const handleSortChange = (e) => {
    const selectedOption = e.target.value;
    if (selectedOption === "최신순") {
      setSortOption("최신순");
    } else if (selectedOption === "조회수순") {
      setSortOption("조회수순");
    } else if (selectedOption === "좋아요순") {
      setSortOption("좋아요순");
    } else if (selectedOption === "광고클릭순") {
      setSortOption("광고클릭순");
    }
  };

  // 비디오 클릭 시 해당 비디오 정보 가져오기------------------------
  const handleVideoModifyFile = (videoId) => {
    setVideoId(videoId);
  };

  // 비디오 인피니트 스크롤-----------------------------------------
  const handleVideoListAdd = async () => {
    if (sortOption === "최신순") {
      const videoRecentData = await Instance.get(
        `content-slave-service/videos/${userId}/sort?s=recent&page=0&size=2`
      );
      setNewVideoList([
        ...videoListData.videos,
        ...videoRecentData.data.payload.videos,
      ]);
    } else if (sortOption === "조회수순") {
      const videoViewData = await Instance.get(
        `content-slave-service/videos/${userId}/sort?s=views&page=0&size=2`
      );
      setNewVideoList(videoViewData.data.payload.videos);
    } else if (sortOption === "좋아요순") {
      const videoLikeData = await Instance.get(
        `content-slave-service/videos/${userId}/sort?s=likes&page=0&size=2`
      );
      setNewVideoList(videoLikeData.data.payload.videos);
    } else if (sortOption === "광고클릭순") {
      const videoAdsData = await Instance.get(
        `content-slave-service/videos/${userId}/sort?s=ads&page=0&size=2`
      );
      setNewVideoList(videoAdsData.data.payload.videos);
    }
  };

  // ComponentDidMount---------------------------------------------
  useEffect(() => {
    // 더보기 최소 한 번 이상 클릭 이후
    if (newVideoList.length !== 0) {
      //
    }
  }, [newVideoList]);

  return (
    <M.LeftBox>
      <U.TitleBetweenBox>
        <U.SpanTitle>영상목록</U.SpanTitle>
        <M.Select onChange={handleSortChange}>
          <option value="최신순">최신순</option>
          <option value="조회수순">조회수순</option>
          <option value="좋아요순">좋아요순</option>
          <option value="광고클릭순">광고클릭순</option>
        </M.Select>
      </U.TitleBetweenBox>
      <M.VideoListWrapper>
        {videoListData.videos?.length === 0 ? (
          <M.VideoEmptySection>
            <M.LargeFont icon={faBoxOpen} />
            <M.BoldSpan>업로드된 영상이 없습니다.</M.BoldSpan>
          </M.VideoEmptySection>
        ) : (
          <>
            {videoListData.videos?.map((v) => (
              <M.VideoListBox
                key={v.videoId}
                onClick={() => handleVideoModifyFile(v.videoId)}
                $clicked_id={videoId}
                $video_id={v.videoId}
              >
                <M.VideoListThumbnail
                  src={v.thumbnailUrl}
                  alt="video-thumbnail"
                  loading="lazy"
                />
                <M.VideoListInfo>
                  <LogoCircleBox>
                    <M.LogoImage
                      // data: URL 스키마를 사용하여 base64 데이터를 직접 src에 할당
                      src={`data:image/;base64,${v.sellerLogo}`}
                      alt="Sellor-Logo"
                      loading="lazy"
                    />
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
            ))}
            {videoListData.videos?.length === 0 ? (
              ""
            ) : (
              <M.VideoListAddText>더보기...</M.VideoListAddText>
            )}
          </>
        )}
      </M.VideoListWrapper>
    </M.LeftBox>
  );
};

export default Mleft;

Mleft.propTypes = {
  videoListData: PropTypes.object,
  videoId: PropTypes.string,
  setVideoId: PropTypes.func,
  sortOption: PropTypes.string,
  setSortOption: PropTypes.func,
};
