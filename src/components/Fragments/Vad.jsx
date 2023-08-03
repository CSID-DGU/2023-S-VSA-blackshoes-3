import { LocalizationProvider, TimeField } from "@mui/x-date-pickers";
import {
  AdInput,
  AdInputSection,
  AdUploadButton,
  AdUploadGridBox,
  AdUploadSection,
  ContentBox,
  FullIcon,
  LinkBox,
  NormalSpan,
  RemoveButton,
  Shadow,
  SmallImage,
  SpanTitle,
  TimeBox,
  TitleLeftBox,
} from "../Home/UploadStyle";
import { DemoContainer } from "@mui/x-date-pickers/internals/demo";
import Minus from "../../assets/images/minus.svg";
import PlusButton from "../../assets/images/plus-button.svg";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import PropTypes from "prop-types";

const Vad = ({ step, setStartTime, setEndTime, setAdContent, setAdUrl }) => {
  return (
    <AdUploadSection>
      {/* {step.second && <Shadow>STEP 2</Shadow>} */}
      <TitleLeftBox>
        <SpanTitle>광고등록</SpanTitle>
        <AdUploadButton>
          <FullIcon src={PlusButton} alt="plus-button" loading="lazy" />
        </AdUploadButton>
      </TitleLeftBox>
      <AdUploadGridBox>
        <AdInputSection>
          <TimeBox>
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <DemoContainer components={["TimeField"]}>
                <TimeField
                  label="시작 시간"
                  onChange={(e) => {
                    const receivedTime = new Date(e.$d);
                    const hours = receivedTime.getHours();
                    const minutes = receivedTime.getMinutes();
                    const seconds = receivedTime.getSeconds();
                    setStartTime(`${hours}:${minutes}:${seconds}`);
                  }}
                  format="HH:mm:ss"
                  color="success"
                />
              </DemoContainer>
            </LocalizationProvider>
          </TimeBox>
          <TimeBox>
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <DemoContainer components={["TimeField"]}>
                <TimeField
                  label="종료 시간"
                  onChange={(e) => {
                    const receivedTime = new Date(e.$d);
                    const hours = receivedTime.getHours();
                    const minutes = receivedTime.getMinutes();
                    const seconds = receivedTime.getSeconds();
                    setEndTime(`${hours}:${minutes}:${seconds}`);
                  }}
                  format="HH:mm:ss"
                  color="success"
                />
              </DemoContainer>
            </LocalizationProvider>
          </TimeBox>
          <ContentBox>
            <NormalSpan>내용</NormalSpan>
            <AdInput
              type="text"
              placeholder="광고로 등록할 내용을 입력해주세요."
              width="250px"
              height="100px"
              onChange={(e) => setAdContent(e.target.value)}
            />
          </ContentBox>
          <LinkBox>
            <NormalSpan>링크</NormalSpan>
            <AdInput
              type="text"
              placeholder="광고 링크를 첨부해주세요."
              width="250px"
              height="35px"
              onChange={(e) => setAdUrl(e.target.value)}
            />
          </LinkBox>
          <RemoveButton>
            <SmallImage src={Minus} alt="minus" />
          </RemoveButton>
        </AdInputSection>
      </AdUploadGridBox>
    </AdUploadSection>
  );
};

export default Vad;

Vad.propTypes = {
  step: PropTypes.object,
  setStartTime: PropTypes.func,
  setEndTime: PropTypes.func,
  setAdContent: PropTypes.func,
  setAdUrl: PropTypes.func,
};
