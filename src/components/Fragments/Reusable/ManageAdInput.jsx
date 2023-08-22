import { useState, useEffect } from "react";
import * as A from "../Upload/UploadStyle";
import Minus from "../../../assets/images/minus.svg";
import { LocalizationProvider, TimeField } from "@mui/x-date-pickers";
import { DemoContainer } from "@mui/x-date-pickers/internals/demo";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import PropTypes from "prop-types";
import dayjs from "dayjs";
import { Instance } from "../../../api/axios";

const ManageAdInput = ({
  userId,
  adId,
  videoId,
  adInputs,
  setAdInputs,
  adStartTime,
  adEndTime,
  adContent,
  adUrl,
}) => {
  // Constant----------------------------------------------------

  // State-------------------------------------------------------
  const [newStartTime, setNewStartTime] = useState(
    adStartTime ? adStartTime : ""
  );
  const [newEndTime, setNewEndTime] = useState(adEndTime ? adEndTime : "");
  const [newAdContent, setNewAdContent] = useState(adContent ? adContent : "");
  const [newAdUrl, setNewAdUrl] = useState(adUrl ? adUrl : "");

  // Function----------------------------------------------------
  const deleteAd = (adId, event) => {
    if (adId !== undefined) {
      const updatedInputs = adInputs.filter((i) => i.adId !== adId);
      const adModifyRequest = {
        adId: adId,
        modifyType: "delete",
      };
      submitModifyVideoAds(adModifyRequest);
      setAdInputs(updatedInputs);
    } else {
      window.alert("새로고침이 필요합니다.");
    }
  };

  const createAd = async () => {
    const adModifyRequest = {
      adId: adId,
      modifyType: "create",
      startTime: newStartTime,
      endTime: newEndTime,
      adContent: newAdContent,
      adUrl: newAdUrl,
    };
    submitModifyVideoAds(adModifyRequest);
  };

  const updateAd = async () => {
    const adModifyRequest = {
      adId: adId,
      modifyType: "update",
      startTime: newStartTime,
      endTime: newEndTime,
      adContent: newAdContent,
      adUrl: newAdUrl,
    };
    submitModifyVideoAds(adModifyRequest);
  };

  const handleTime = (setHandler, event) => {
    const receivedTime = new Date(event.$d);
    const hours = receivedTime.getHours();
    const minutes = receivedTime.getMinutes();
    const seconds = receivedTime.getSeconds();
    const totalMilliseconds = (hours * 3600 + minutes * 60 + seconds) * 1000;
    setHandler(totalMilliseconds.toString());
  };

  const handleStartTime = (event) => {
    setNewStartTime(event);
    console.log(event);
  };

  const handleEndTime = (event) => {
    setNewEndTime(event);
    console.log(event);
  };

  const handleAdContent = (event) => {
    setNewAdContent(event.target.value);
    console.log(event.target.value);
  };

  const handleAdUrl = (event) => {
    setNewAdUrl(event.target.value);
    console.log(event.target.value);
  };

  const submitModifyVideoAds = async (adModifyRequest) => {
    const adModifyRequests = [adModifyRequest];
    try {
      if (
        adModifyRequest.modifyType === "create"
          ? window.confirm("광고를 추가하시겠습니까?")
          : adModifyRequest.modifyType === "delete"
          ? window.confirm("광고를 삭제하시겠습니까?")
          : window.confirm("광고를 수정하시겠습니까?")
      ) {
        await Instance.put(`upload-service/videos/${userId}/${videoId}/ads`, {
          adModifyRequests: adModifyRequests,
        }).then((res) => {
          console.log(res);
          alert("광고가 수정되었습니다.");
        });
      }
    } catch (err) {
      console.log(err);
      alert("광고 수정에 실패했습니다.");
    }
  };

  return (
    <A.AdInputSection key={adId}>
      <A.TimeBox>
        <A.NormalSpan>시작</A.NormalSpan>
        <LocalizationProvider dateAdapter={AdapterDayjs}>
          <DemoContainer components={["TimeField"]}>
            <TimeField
              label="시작 시간"
              onChange={(e) => handleTime(handleStartTime, e)}
              format="HH:mm:ss"
              color="success"
              defaultValue={
                adEndTime && dayjs.unix(parseInt(adEndTime) / 1000 - 32400)
              }
            />
          </DemoContainer>
        </LocalizationProvider>
      </A.TimeBox>
      <A.TimeBox>
        <A.NormalSpan>종료</A.NormalSpan>
        <LocalizationProvider dateAdapter={AdapterDayjs}>
          <DemoContainer components={["TimeField"]}>
            <TimeField
              label="종료 시간"
              onChange={(e) => handleTime(handleEndTime, e)}
              format="HH:mm:ss"
              color="success"
              defaultValue={
                adEndTime && dayjs.unix(parseInt(adEndTime) / 1000 - 32400)
              }
            />
          </DemoContainer>
        </LocalizationProvider>
      </A.TimeBox>
      <A.ContentBox>
        <A.NormalSpan>내용</A.NormalSpan>
        <A.AdInput
          type="text"
          placeholder="광고로 등록할 내용을 입력해주세요."
          width="85%"
          height="100px"
          onChange={(e) => handleAdContent(e)}
          defaultValue={adContent && adContent}
        />
      </A.ContentBox>
      <A.LinkBox>
        <A.NormalSpan>링크</A.NormalSpan>
        <A.AdInput
          type="text"
          placeholder="광고 링크를 첨부해주세요."
          width="85%"
          height="35px"
          onChange={(e) => handleAdUrl(e)}
          defaultValue={adUrl && adUrl}
        />
      </A.LinkBox>
      {adId !== undefined ? (
        <A.RemoveButton onClick={() => updateAd()}>광고 수정</A.RemoveButton>
      ) : (
        <A.RemoveButton onClick={() => createAd()}>광고 추가</A.RemoveButton>
      )}
      <A.RemoveButton onClick={() => deleteAd(adId, event)}>
        광고 삭제
      </A.RemoveButton>
    </A.AdInputSection>
  );
};

export default ManageAdInput;

ManageAdInput.propTypes = {
  adIdx: PropTypes.number,
  userId: PropTypes.string,
  adId: PropTypes.string,
  videoId: PropTypes.string,
  adInputs: PropTypes.array,
  setAdInputs: PropTypes.func,
  adStartTime: PropTypes.string,
  setStartTime: PropTypes.func,
  adEndTime: PropTypes.string,
  setEndTime: PropTypes.func,
  adContent: PropTypes.string,
  setAdContent: PropTypes.func,
  adUrl: PropTypes.string,
  setAdUrl: PropTypes.func,
  handleAdList: PropTypes.func,
};
