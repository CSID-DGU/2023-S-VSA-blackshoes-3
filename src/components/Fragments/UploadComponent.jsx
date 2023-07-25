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
  VideoUploadButton,
} from "../Home/UploadStyle";
import { ColorButton } from "../Sign/SignStyle";
import HashLoader from "react-spinners/HashLoader";
import { faSquareCheck } from "@fortawesome/free-solid-svg-icons";
import Plus from "../../assets/images/plus.svg";

const UploadComponent = ({ userId, step, setStep }) => {
  // State-------------------------------------------------------
  const [loading, setLoading] = useState(false);
  const [videoFile, setVideoFile] = useState(null);
  const [preview, setPreview] = useState(null);

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

  const handleNextStep = async () => {
    if (!videoFile) {
      alert("동영상을 첨부해주세요");
    } else if (!step.second) {
      if (window.confirm("동영상을 등록하고 다음 단계로 넘어가시겠습니까?")) {
        if (videoFile) {
          try {
            setLoading(true);
            const formData = new FormData();
            formData.append("video", videoFile);
            await axios
              .post(`http://13.125.69.94:8021/upload-service/videos/${userId}`, formData, {
                headers: {
                  "Content-Type": "multipart/form-data",
                },
              })
              .then((res) => {
                console.log(res);
                setLoading(false);
                alert("영상이 업로드되었습니다.");
                setPreview(res.data.payload.videoCloudfrontUrl);
                setStep({
                  ...step,
                  second: true,
                });
              });
          } catch (err) {
            console.log(err);
            setLoading(false);
            alert("업로드에 실패했습니다.");
          }
        }
      }
    } else {
      if (window.confirm("동영상 최종 업로드를 시작합니다.")) {
        console.log("Final started");
      }
    }
  };
  return (
    <>
      <TitleBetweenBox>
        <SpanTitle>영상 등록</SpanTitle>
        <ColorButton width="65px" style={{ height: "35px" }} onClick={handleNextStep}>
          {step.first && step.second ? "등록" : "다음"}
        </ColorButton>
      </TitleBetweenBox>
      <VideoInput type="file" accept="video/*" id="video-input" onChange={handleVideoChange} />
      <VideoInputSection>
        <VideoUploadButton htmlFor="video-input" videofile={videoFile}>
          <FullIcon src={Plus} alt="plus-icon" loading="lazy" />
        </VideoUploadButton>
        {preview && (
          <VideoPreview controls>
            <source src={preview} type="video/mp4" style={{ position: "relative" }} />
          </VideoPreview>
        )}
        {loading && (
          <>
            <UploadedState>영상이 업로드 중입니다, 잠시만 기다려주세요...</UploadedState>
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
