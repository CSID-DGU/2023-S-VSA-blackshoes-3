import { useState } from "react";
import * as A from "../Upload/UploadStyle";
import Minus from "../../../assets/images/minus.svg";
import { LocalizationProvider, TimeField } from "@mui/x-date-pickers";
import { DemoContainer } from "@mui/x-date-pickers/internals/demo";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import PropTypes from "prop-types";
import dayjs from "dayjs";
import { Instance } from "../../../api/axios";

const AdInput = ({
  adId,
  userId,
  videoId,
  adInputs,
  setAdInputs,
  adStartTime,
  setStartTime,
  adEndTime,
  setEndTime,
  adContent,
  setAdContent,
  adUrl,
  setAdUrl,
}) => {
  // Constant----------------------------------------------------

  // State-------------------------------------------------------
  const [newStartTime, setNewStartTime] = useState(adStartTime);
  const [newEndTime, setNewEndTime] = useState(adEndTime);
  const [newAdContent, setNewAdContent] = useState(adContent);
  const [newAdUrl, setNewAdUrl] = useState(adUrl);

  // Function----------------------------------------------------
  const removeInput = (id, event) => {
    if (adInputs.length > 1) {
      const updatedInputs = adInputs.filter((i) => i.id !== id);
      setAdInputs(updatedInputs);
    } else {
      event.preventDefault();
      alert("광고를 하나 이상 등록해야 합니다.");
    }
  };

  const handleTime = (savedTime, setHandler, setNewHandler, event) => {
    const receivedTime = new Date(event.$d);
    const hours = receivedTime.getHours();
    const minutes = receivedTime.getMinutes();
    const seconds = receivedTime.getSeconds();
    const totalMilliseconds = (hours * 3600 + minutes * 60 + seconds) * 1000;
    if (savedTime) {
      setNewHandler(totalMilliseconds.toString());
    } else {
      setHandler(totalMilliseconds.toString());
    }
  };

  const handleAdContent = (savedAdContent, setHandler, setNewHandler, event) => {
    if (savedAdContent) {
      setNewHandler(event.target.value);
    } else {
      setHandler(event.target.value);
    }
  };

  const handleAdUrl = (savedAdUrl, setHandler, setNewHandler, event) => {
    if (savedAdUrl) {
      setNewHandler(event.target.value);
    } else {
      setHandler(event.target.value);
    }
  };

  const submitVideoAds = async (e) => {
    e.preventDefault();
    const adModifyRequests = [];
    try {
      if (window.confirm("광고를 수정하시겠습니까?")) {
        await Instance.put(`upload/service/videos/${userId}/${videoId}/ads`, adModifyRequests).then(
          (res) => {
            console.log(res);
            alert("광고가 수정되었습니다.");
          }
        );
      }
    } catch (err) {
      console.log(err);
      alert("광고 수정에 실패했습니다.");
    }
  };

  return (
    <A.AdInputSection key={adId}>
      <A.TimeBox>
        <LocalizationProvider dateAdapter={AdapterDayjs}>
          <DemoContainer components={["TimeField"]}>
            <TimeField
              label="시작 시간"
              onChange={(e) => {
                handleTime(adStartTime, setStartTime, setNewStartTime, e);
              }}
              format="HH:mm:ss"
              color="success"
              defaultValue={adStartTime && dayjs.unix(parseInt(adStartTime) / 1000 - 32400)}
            />
            <TimeField
              label="종료 시간"
              onChange={(e) => {
                handleTime(adEndTime, setEndTime, setNewEndTime, e);
              }}
              format="HH:mm:ss"
              color="success"
              defaultValue={adEndTime && dayjs.unix(parseInt(adEndTime) / 1000 - 32400)}
            />
          </DemoContainer>
        </LocalizationProvider>
      </A.TimeBox>
      <A.ContentBox>
        <A.NormalSpan>내용</A.NormalSpan>
        <A.AdInput
          type="text"
          placeholder="광고로 등록할 내용을 입력해주세요."
          defaultValue={adContent && adContent}
          width="85%"
          height="100px"
          onChange={(e) => handleAdContent(adContent, setAdContent, setNewAdContent, e)}
        />
      </A.ContentBox>
      <A.LinkBox>
        <A.NormalSpan>링크</A.NormalSpan>
        <A.AdInput
          type="text"
          placeholder="광고 링크를 첨부해주세요."
          defaultValue={adUrl && adUrl}
          width="85%"
          height="35px"
          onChange={(e) => handleAdUrl(adUrl, setAdUrl, setNewAdUrl, e)}
        />
      </A.LinkBox>
      <A.RemoveButton onClick={() => removeInput(adId, event)}>
        <A.SmallImage src={Minus} alt="minus" />
      </A.RemoveButton>
    </A.AdInputSection>
  );
};

export default AdInput;

AdInput.propTypes = {
  adId: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  userId: PropTypes.string,
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
};
