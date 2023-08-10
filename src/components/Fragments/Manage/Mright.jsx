import * as M from "./ManageStyle";
import * as U from "../Upload/UploadStyle";
import { LocalizationProvider, TimeField } from "@mui/x-date-pickers";
import { DemoContainer } from "@mui/x-date-pickers/internals/demo";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import PlusButton from "../../../assets/images/plus-button.svg";
import Minus from "../../../assets/images/minus.svg";

const Mright = () => {
  return (
    <M.RightBox>
      <U.TitleLeftBox>
        <U.SpanTitle>광고수정</U.SpanTitle>
        <U.AdUploadButton>
          <U.FullIcon src={PlusButton} alt="plus-button" loading="lazy" />
        </U.AdUploadButton>
      </U.TitleLeftBox>
      <M.AdModifySection>
        <U.AdInputSection>
          <U.TimeBox>
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
                    // setStartTime(totalMilliseconds.toString());
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
                    // setEndTime(totalMilliseconds.toString());
                  }}
                  format="HH:mm:ss"
                  color="success"
                />
              </DemoContainer>
            </LocalizationProvider>
          </U.TimeBox>
          <U.ContentBox>
            <U.NormalSpan>내용</U.NormalSpan>
            <U.AdInput
              type="text"
              placeholder="수정할 광고 내용을 입력해주세요."
              width="85%"
              height="100px"
              // onChange={(e) => setAdContent(e.target.value)}
            />
          </U.ContentBox>
          <U.LinkBox>
            <U.NormalSpan>링크</U.NormalSpan>
            <U.AdInput
              type="text"
              placeholder="수정할 광고 링크를 첨부해주세요."
              width="85%"
              height="35px"
              // onChange={(e) => setAdUrl(e.target.value)}
            />
          </U.LinkBox>
          <U.RemoveButton>
            <U.SmallImage src={Minus} alt="minus" />
          </U.RemoveButton>
        </U.AdInputSection>
      </M.AdModifySection>
      <U.TitleLeftBox>
        <U.SpanTitle>댓글조회</U.SpanTitle>
      </U.TitleLeftBox>
      <M.AdCommentEmptySection>
        <M.BoldSpan>영상 선택 후 댓글을 조회할 수 있습니다.</M.BoldSpan>
      </M.AdCommentEmptySection>
    </M.RightBox>
  );
};

export default Mright;
