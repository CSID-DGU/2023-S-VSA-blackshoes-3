import {
  CheckIcon,
  FullIcon,
  SpinnerBox,
  UploadedState,
  VideoInput,
  VideoInputSection,
  VideoPreview,
  VideoUploadButton,
} from "../Home/UploadStyle";
import HashLoader from "react-spinners/HashLoader";
import { faSquareCheck } from "@fortawesome/free-solid-svg-icons";
import Plus from "../../assets/images/plus.svg";
import PropTypes from "prop-types";
import { useState } from "react";

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
}) => {
  // Constant----------------------------------------------------
  const baseUrl = preview2;
  const qualities = ["1080p", "720p", "480p"];

  // State-------------------------------------------------------
  const [selectedQuality, setSelectedQuality] = useState(qualities[0]);

  // Function----------------------------------------------------
  const handleQualityChange = (e) => {
    setSelectedQuality(e.target.value);
  };

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
  console.log(`${baseUrl}/${selectedQuality}.m3u8`);
  return (
    <>
      <VideoInput type="file" accept="video/*" id="video-input" onChange={handleVideoChange} />
      <VideoInputSection videofile={videoFile}>
        <VideoUploadButton htmlFor="video-input" videofile={videoFile}>
          <FullIcon src={Plus} alt="plus-icon" loading="lazy" />
        </VideoUploadButton>
        {preview && !preview2 ? (
          <VideoPreview controls>
            <source src={preview} type="video/mp4" style={{ position: "relative" }} />
          </VideoPreview>
        ) : preview2 ? (
          <>
            <select id="qualitySelect" value={selectedQuality} onChange={handleQualityChange}>
              {qualities.map((quality) => (
                <option key={quality} value={quality}>
                  {quality}
                </option>
              ))}
            </select>
            <VideoPreview controls>
              <source
                src={`${baseUrl}/${selectedQuality}.m3u8`}
                type="application/x-mpegURL"
                style={{ position: "relative" }}
              />
            </VideoPreview>
          </>
        ) : (
          ""
        )}
        {loading && (
          <>
            <UploadedState>
              {percentage === 100
                ? "잠시만 기다려주세요"
                : percentage === 0
                ? "영상이 업로드 중입니다"
                : percentage > 0 && `영상이 인코딩 중입니다, ${percentage}% 완료`}
            </UploadedState>
            <SpinnerBox>
              <HashLoader color="#1DAE86" />
            </SpinnerBox>
          </>
        )}
        {step.second && (
          <>
            <UploadedState>영상 업로드 완료</UploadedState>
            <SpinnerBox>
              <CheckIcon icon={faSquareCheck} />
            </SpinnerBox>
          </>
        )}
      </VideoInputSection>
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
};
