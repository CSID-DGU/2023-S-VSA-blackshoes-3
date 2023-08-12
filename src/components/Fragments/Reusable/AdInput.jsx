import * as A from "../Upload/UploadStyle";
import Minus from "../../../assets/images/minus.svg";
import { LocalizationProvider, TimeField } from "@mui/x-date-pickers";
import { DemoContainer } from "@mui/x-date-pickers/internals/demo";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import PropTypes from "prop-types";

const AdInput = ({
  params,
  adInputs,
  setAdInputs,
  setStartTime,
  setEndTime,
  setAdContent,
  setAdUrl,
}) => {
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

  return (
    <A.AdInputSection key={params.id}>
      <A.TimeBox>
        <LocalizationProvider dateAdapter={AdapterDayjs}>
          <DemoContainer components={["TimeField"]}>
            <TimeField
              label="시작 시간"
              onChange={(e) => {
                const receivedTime = new Date(e.$d);
                const hours = receivedTime.getHours();
                const minutes = receivedTime.getMinutes();
                const seconds = receivedTime.getSeconds();
                const totalMilliseconds = (hours * 3600 + minutes * 60 + seconds) * 1000;
                setStartTime(totalMilliseconds.toString());
              }}
              format="HH:mm:ss"
              color="success"
            />
            <TimeField
              label="종료 시간"
              onChange={(e) => {
                const receivedTime = new Date(e.$d);
                const hours = receivedTime.getHours();
                const minutes = receivedTime.getMinutes();
                const seconds = receivedTime.getSeconds();
                const totalMilliseconds = (hours * 3600 + minutes * 60 + seconds) * 1000;
                setEndTime(totalMilliseconds.toString());
              }}
              format="HH:mm:ss"
              color="success"
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
          onChange={(e) => setAdContent(e.target.value)}
        />
      </A.ContentBox>
      <A.LinkBox>
        <A.NormalSpan>링크</A.NormalSpan>
        <A.AdInput
          type="text"
          placeholder="광고 링크를 첨부해주세요."
          width="85%"
          height="35px"
          onChange={(e) => setAdUrl(e.target.value)}
        />
      </A.LinkBox>
      <A.RemoveButton onClick={() => removeInput(params.id, event)}>
        <A.SmallImage src={Minus} alt="minus" />
      </A.RemoveButton>
    </A.AdInputSection>
  );
};

export default AdInput;

AdInput.propTypes = {
  params: PropTypes.object,
  adInputs: PropTypes.array,
  setAdInputs: PropTypes.func,
  setStartTime: PropTypes.func,
  setEndTime: PropTypes.func,
  setAdContent: PropTypes.func,
  setAdUrl: PropTypes.func,
};
