import * as U from "./UploadStyle";
import HashLoader from "react-spinners/HashLoader";
import { faSquareCheck } from "@fortawesome/free-solid-svg-icons";
import Plus from "../../../assets/images/plus.svg";
import PropTypes from "prop-types";
import { useEffect, useRef, useState } from "react";
import videojs from "video.js";
import "video.js/dist/video-js.css";

const Vupload = ({
  userId,
  step,
  setStep,
  loading,
  percentage,
  videoFile,
  setVideoFile,
  preview,
  setPreview,
  preview2,
  timer,
  iconTimer,
}) => {
  // Constant----------------------------------------------------
  const baseUrl = preview2;
  const qualities = ["1080p", "720p", "480p"];

  // State-------------------------------------------------------
  const [selectedQuality, setSelectedQuality] = useState(qualities[0]);
  const videoRef = useRef(null);

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

  // ComponentDidMount-------------------------------------------
  useEffect(() => {
    if (preview2 !== null) {
      iconTimer();
    }
    // video.js
    if (videoRef.current !== null) {
      // video.js 옵션 설정
      const options = {
        techOrder: ["html5"],
        controls: true,
        autoplay: true,
        sources: [
          {
            src: `${baseUrl}/${selectedQuality}.m3u8`,
            type: "application/x-mpegURL",
          },
        ],
      };

      // video.js 생성 및 초기화
      const player = videojs(videoRef.current, options);
      // video.js 소멸
      return () => {
        if (player) {
          player.dispose();
        }
      };
    }
  }, [baseUrl]);

  return (
    <>
      <U.VideoInput type="file" accept="video/*" id="video-input" onChange={handleVideoChange} />
      <U.VideoInputSection $video_file={videoFile}>
        <U.VideoUploadButton htmlFor="video-input" $video_file={videoFile}>
          <U.FullIcon src={Plus} alt="plus-icon" loading="lazy" />
        </U.VideoUploadButton>
        {preview !== null && preview2 === null ? (
          <U.VideoPreview controls autoPlay>
            <source src={preview} type="video/mp4" />
          </U.VideoPreview>
        ) : preview2 !== null ? (
          <U.VideoPreview controls ref={videoRef} className="video-js vjs-default-skin">
            <source src={`${baseUrl}/${selectedQuality}.m3u8`} type="application/x-mpegURL" />
          </U.VideoPreview>
        ) : (
          ""
        )}
        {loading && (
          <>
            <U.UploadedState>
              {percentage === 100
                ? "잠시만 기다려주세요"
                : percentage === 0
                ? "영상이 업로드 중입니다"
                : percentage > 0 && `영상이 인코딩 중입니다, ${percentage}% 완료`}
            </U.UploadedState>
            <U.SpinnerBox>
              <HashLoader color="#1DAE86" />
            </U.SpinnerBox>
          </>
        )}
        {step.second && (
          <>
            <U.UploadedState $timer={timer}>영상 업로드 완료</U.UploadedState>
            <U.SpinnerBox $timer={timer}>
              <U.CheckIcon icon={faSquareCheck} />
            </U.SpinnerBox>
          </>
        )}
      </U.VideoInputSection>
    </>
  );
};

export default Vupload;

Vupload.propTypes = {
  userId: PropTypes.string,
  step: PropTypes.object,
  setStep: PropTypes.func,
  loading: PropTypes.bool,
  percentage: PropTypes.number,
  videoFile: PropTypes.object,
  setVideoFile: PropTypes.func,
  preview: PropTypes.string,
  setPreview: PropTypes.func,
  preview2: PropTypes.string,
  timer: PropTypes.bool,
  iconTimer: PropTypes.func,
};
