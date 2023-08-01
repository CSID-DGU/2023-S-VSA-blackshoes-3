import axios from "axios";
import { useState } from "react";
import {
  CheckIcon,
  FullIcon,
  SpanTitle,
  SpinnerBox,
  TitleBetweenBox,
  UploadedState,
  VideoInput,
  VideoInputSection,
  VideoPreview,
  VideoUploadButton
} from "../Home/UploadStyle";
import { ColorButton } from "../Sign/SignStyle";
import HashLoader from "react-spinners/HashLoader";
import { faSquareCheck } from "@fortawesome/free-solid-svg-icons";
import Plus from "../../assets/images/plus.svg";

const UploadComponent = ({
  userId,
  step,
  setStep,
  loading,
  percentage,
  videoFile,
  setVideoFile,
  preview,
  setPreview
}) => {
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

  return (
    <>
      <VideoInput
        type="file"
        accept="video/*"
        id="video-input"
        onChange={handleVideoChange}
      />
      <VideoInputSection>
        <VideoUploadButton htmlFor="video-input" videofile={videoFile}>
          <FullIcon src={Plus} alt="plus-icon" loading="lazy" />
        </VideoUploadButton>
        {preview && (
          <VideoPreview controls>
            <source
              src={preview}
              type="video/m3u8"
              style={{ position: "relative" }}
            />
          </VideoPreview>
        )}
        {loading && (
          <>
            <UploadedState>
              {percentage === 100
                ? "잠시만 기다려주세요"
                : percentage === 0
                ? "영상이 업로드 중입니다"
                : percentage > 0 &&
                  `영상이 인코딩 중입니다, ${percentage}% 완료`}
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

export default UploadComponent;
