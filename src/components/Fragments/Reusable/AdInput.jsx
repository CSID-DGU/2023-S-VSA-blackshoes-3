import { useEffect, useState } from "react";
import * as A from "../Upload/UploadStyle";
import Minus from "../../../assets/images/minus.svg";
import { LocalizationProvider, TimeField } from "@mui/x-date-pickers";
import { DemoContainer } from "@mui/x-date-pickers/internals/demo";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import PropTypes from "prop-types";

const UploadAdInput = ({ adIdx, adInputs, setAdInputs, handleAdList }) => {
  // State-------------------------------------------------------
  const [newStartTime, setNewStartTime] = useState("");
  const [newEndTime, setNewEndTime] = useState("");
  const [newAdContent, setNewAdContent] = useState("");
  const [newAdUrl, setNewAdUrl] = useState("");

  const ad = {
    startTime: newStartTime,
    endTime: newEndTime,
    adContent: newAdContent,
    adUrl: newAdUrl,
  };

  useEffect(() => {
    handleAdList(adIdx, ad);
  }, [ad]);

  // Function----------------------------------------------------
  const removeInput = (adIdx, event) => {
    const updatedInputs = adInputs.filter((i) => i.id !== adIdx);
    setAdInputs(updatedInputs);
  };

  const handleTime = (savedTime, setHandler, setNewHandler, event) => {
    const receivedTime = new Date(event.$d);
    const hours = receivedTime.getHours();
    const minutes = receivedTime.getMinutes();
    const seconds = receivedTime.getSeconds();
    const totalMilliseconds = (hours * 3600 + minutes * 60 + seconds) * 1000;

    setHandler(totalMilliseconds.toString());
  };

  return (
    <A.AdInputSection key={adIdx}>
      <A.TimeBox>
        <A.NormalSpan>시작</A.NormalSpan>
        <LocalizationProvider dateAdapter={AdapterDayjs}>
          <DemoContainer components={["TimeField"]}>
            <TimeField
              label="시작 시간"
              onChange={(e) => handleTime(setNewStartTime(e), e)}
              format="HH:mm:ss"
              color="success"
              // defaultValue={
              //   adStartTime && dayjs.unix(parseInt(adStartTime) / 1000 - 32400)
              // }
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
              onChange={(e) => handleTime(setNewEndTime(e), e)}
              format="HH:mm:ss"
              color="success"
              // defaultValue={
              //   adEndTime && dayjs.unix(parseInt(adEndTime) / 1000 - 32400)
              // }
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
          onChange={(e) => setNewAdContent(e.target.value)}
        />
      </A.ContentBox>
      <A.LinkBox>
        <A.NormalSpan>링크</A.NormalSpan>
        <A.AdInput
          type="text"
          placeholder="광고 링크를 첨부해주세요."
          width="85%"
          height="35px"
          onChange={(e) => setNewAdUrl(e.target.value)}
        />
      </A.LinkBox>
      <A.RemoveButton onClick={() => removeInput(adIdx, event)}>
        <A.SmallImage src={Minus} alt="minus" />
      </A.RemoveButton>
    </A.AdInputSection>
  );
};

export default UploadAdInput;

UploadAdInput.propTypes = {
  adIdx: PropTypes.number,
  adInputs: PropTypes.array,
  setAdInputs: PropTypes.func,
  handleAdList: PropTypes.func,
};
