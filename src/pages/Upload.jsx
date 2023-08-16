import { useContext, useEffect, useState } from "react";
import Header from "../components/Fragments/Nav/Header";
import Nav from "../components/Fragments/Nav/Nav";
import ResNav from "../components/Fragments/Nav/ResNav";
import { GridWrapper } from "../components/Home/HomeStyle";
import { useNavigate, useParams } from "react-router-dom";
import { GlobalContext } from "../context/GlobalContext";
import * as V from "../components/Fragments/Upload/UploadStyle";
import Vupload from "../components/Fragments/Upload/Vupload";
import Vinfo from "../components/Fragments/Upload/Vinfo";
import Vad from "../components/Fragments/Upload/Vad";
import { ColorButton } from "../components/Sign/SignStyle";
import SockJS from "sockjs-client/dist/sockjs.min.js";
import Stomp from "stompjs";
import { Instance } from "../api/axios";

// Upload EC2
// 210.94.179.19:9127
// Content-Slave
// 13.125.69.94:8011

const Upload = () => {
  // Constant----------------------------------------------------
  const navigate = useNavigate();
  const { userId } = useParams();

  // State-------------------------------------------------------
  const { setPage } = useContext(GlobalContext);
  const [step, setStep] = useState({
    first: true,
    second: false,
  });
  const [loading, setLoading] = useState(false);
  const [videoFile, setVideoFile] = useState(null);
  const [videoId, setVideoId] = useState(null);
  const [preview, setPreview] = useState(null);
  const [preview2, setPreview2] = useState(null);
  const [videoExpireState, setVideoExpireState] = useState("");
  //
  const [videoName, setVideoName] = useState("");
  const [thumbnailFile, setThumbnailFile] = useState(null);
  const [tagIdList, setTagIdList] = useState([]);
  const [regionTag, setRegionTag] = useState([]);
  const [themeTag, setThemeTag] = useState([]);
  const [adList, setAdList] = useState([]);
  const [adUrl, setAdUrl] = useState("");
  const [adContent, setAdContent] = useState("");
  const [startTime, setStartTime] = useState("");
  const [endTime, setEndTime] = useState("");
  //
  const [isSocketOpen, setIsSocketOpen] = useState(false);
  const [percentage, setPercentage] = useState(0);
  const [timer, setTimer] = useState(true);

  // Function----------------------------------------------------
  const iconTimer = () => {
    setTimeout(() => {
      setTimer(false);
    }, 5000);
  };

  const fetchData = async () => {
    try {
      await Instance.get(`/upload-service/videos/temporary/${userId}`).then(async (res) => {
        if (res.status === 200) {
          if (window.confirm(`이전에 작성하던 영상이 있습니다. 이어서 작성하시겠습니까?`)) {
            const expiredAt = new Date(res.data.payload.expiredAt);
            setVideoExpireState(
              `${expiredAt.getHours()}시 ${expiredAt.getMinutes()}분에 영상이 만료됩니다 --- `
            );
            setVideoId(res.data.payload.videoId);
            setPreview2(res.data.payload.videoCloudfrontUrl);
            setStep({
              first: false,
              second: true,
            });
          } else {
            await Instance.delete(
              `/upload-service/videos/temporary/${userId}/${res.data.payload.videoId}`
            )
              .then(() => {
                alert("이전에 작성하던 영상을 삭제했습니다.");
              })
              .catch(() => {
                alert("이전에 작성하던 영상을 삭제하는데 실패했습니다.");
              });
          }
        }
      });
    } catch (err) {
      if (err.status === 404) {
        alert("이전에 작성하던 영상이 없습니다.");
        return;
      } else if (err.status === 500) {
        alert("서버에 문제가 발생했습니다. 잠시 후 다시 시도해주세요.");
        navigate("/", { replace: true });
      }
    }
    const regionData = await Instance.get(`/upload-service/tags/region`);
    const themeData = await Instance.get(`/upload-service/tags/theme`);
    setRegionTag(regionData.data.payload.tags);
    setThemeTag(themeData.data.payload.tags);
  };

  const handleNextStep = async (e) => {
    e.preventDefault();
    if (!videoFile && !preview2) {
      alert("동영상을 첨부해주세요");
    } else if (!step.second) {
      if (window.confirm("동영상을 등록하고 다음 단계로 넘어가시겠습니까?")) {
        if (videoFile) {
          try {
            setLoading(true);
            setIsSocketOpen(true);
            const formData = new FormData();
            formData.append("video", videoFile);
            await Instance.post(`/upload-service/videos/${userId}`, formData, {
              headers: {
                "Content-Type": "multipart/form-data",
              },
            }).then((res) => {
              setLoading(false);
              setIsSocketOpen(true);
              iconTimer();
              alert("영상이 업로드되었습니다.");
              const expiredAt = new Date(res.data.payload.expiredAt);
              setVideoExpireState(
                `${expiredAt.getHours()}시 ${expiredAt.getMinutes()}분에 영상이 만료됩니다 --- `
              );
              setPreview2(res.data.payload.videoCloudfrontUrl);
              setVideoId(res.data.payload.videoId);
              setStep({
                ...step,
                second: true,
              });
            });
          } catch (err) {
            setLoading(false);
            setIsSocketOpen(true);
            alert("업로드에 실패했습니다.");
          }
        }
      }
    } else {
      if (window.confirm("동영상 최종 업로드를 시작합니다.")) {
        const requestData = {
          thumbnail: thumbnailFile,
          requestUpload: {
            videoName: videoName,
            tagIdList: tagIdList,
            adList: [
              {
                adUrl: adUrl,
                adContent: adContent,
                startTime: startTime,
                endTime: endTime,
              },
            ],
          },
        };
        const jsonData = JSON.stringify(requestData.requestUpload);
        const blob = new Blob([jsonData], { type: "application/json" });
        const formData = new FormData();
        formData.append("thumbnail", thumbnailFile);
        formData.append("requestUpload", blob);
        try {
          await Instance.post(`/upload-service/videos/${userId}/${videoId}`, formData, {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          }).then((res) => {
            console.log(res);
            alert("동영상 최종 업로드가 완료되었습니다.");
            navigate(`/home/${userId}/manage`);
          });
        } catch (err) {
          console.log(err);
          alert(`${err.response.data.error}`);
          return;
        }
      }
    }
  };

  const handleVideoExtend = async () => {
    if (window.confirm("영상이 지금으로부터 30분 후 만료됩니다.")) {
      try {
        await Instance.put(`/upload-service/videos/temporary/${userId}/${videoId}`).then((res) => {
          const expiredAt = new Date(res.data.payload.expiredAt);
          setVideoExpireState(
            `${expiredAt.getHours()}시 ${expiredAt.getMinutes()}분에 영상이 만료됩니다 --- `
          );
        });
      } catch (err) {
        console.log(err);
        alert("연장에 실패했습니다.");
      }
    } else {
      return;
    }
  };

  // ComponentDidMount-------------------------------------------
  useEffect(() => {
    setPage(1);
    fetchData();
  }, []);

  useEffect(() => {
    let stompClient;

    const openSocket = () => {
      const socket = new SockJS("http://210.94.179.19:9127/ws");
      stompClient = Stomp.over(socket);
      stompClient.connect({}, () => {
        stompClient.subscribe(`/topic/encoding/${userId}`, (message) => {
          const messageBody = JSON.parse(message.body);
          setPercentage(Math.floor(messageBody.encodedPercentage));
        });
      });
    };

    const closeSocket = () => {
      if (stompClient) {
        stompClient.disconnect(() => {});
      }
    };

    if (isSocketOpen) {
      openSocket();
    } else {
      closeSocket();
    }

    return () => {
      closeSocket();
    };
  }, [isSocketOpen]);

  return (
    <GridWrapper>
      <Header />
      <Nav />
      <V.VideoForm encType="multipart/form-data">
        <ResNav userId={userId} />
        <V.VideoUploadSection>
          <V.TitleBetweenBox>
            <V.SpanTitle>영상 등록</V.SpanTitle>
            <V.MiddleSpan $preview2={preview2}>
              {videoExpireState}{" "}
              <V.ExtendSpan onClick={handleVideoExtend}>만료 시간 연장</V.ExtendSpan>
            </V.MiddleSpan>
            <ColorButton width="65px" style={{ height: "35px" }} onClick={handleNextStep}>
              {step.first && step.second ? "등록" : "다음"}
            </ColorButton>
          </V.TitleBetweenBox>
          {/* 영상 업로드 컴포넌트 조각 */}
          <Vupload
            userId={userId}
            step={step}
            setStep={setStep}
            loading={loading}
            percentage={percentage}
            videoFile={videoFile}
            setVideoFile={setVideoFile}
            preview={preview}
            setPreview={setPreview}
            preview2={preview2}
            timer={timer}
            iconTimer={iconTimer}
          />
          <br />
          {/* 영상 정보 등록 컴포넌트 조각 */}
          <Vinfo
            step={step}
            regionTag={regionTag}
            themeTag={themeTag}
            setVideoName={setVideoName}
            thumbnailFile={thumbnailFile}
            setThumbnailFile={setThumbnailFile}
            tagIdList={tagIdList}
            setTagIdList={setTagIdList}
          />
        </V.VideoUploadSection>
        {/* 영상 광고 등록 컴포넌트 조각 */}
        <Vad
          step={step}
          setStartTime={setStartTime}
          setEndTime={setEndTime}
          setAdContent={setAdContent}
          setAdUrl={setAdUrl}
        />
      </V.VideoForm>
    </GridWrapper>
  );
};

export default Upload;
