import { styled } from "styled-components";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { TimeField, TimePicker } from "@mui/x-date-pickers";

// Video Upload--------------------------------------------
export const VideoForm = styled.form`
  width: calc(100vw - 336px);
  height: 100%;
  grid-column: 2 / 3;
  grid-row: 2 / 3;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  gap: 30px;
  background-color: #f6f7f9;
  padding: 30px;
  overflow-y: auto;
  transition: all 0.3s;
  @media all and (max-width: 1480px) {
    flex-direction: column;
    justify-content: flex-start;
    align-items: center;
  }
  @media all and (max-width: 900px) {
    grid-column: 1 / 3;
    width: 100%;
    max-width: 100%;
  }
`;

export const VideoUploadSection = styled.div`
  width: 70%;
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: 15px;
  @media all and (max-width: 1480px) {
    width: 100%;
  }
  @media all and (max-width: 900px) {
  }
`;

export const TitleBetweenBox = styled.section`
  width: 100%;
  height: 50px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
`;

export const MiddleSpan = styled.span`
  display: ${(props) => (props.$preview2 === null ? "none" : "block")};
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 12px;
  font-weight: bold;
  color: ${(props) => props.theme.secondBlack};
`;

export const ExtendSpan = styled.span`
  font-size: 12px;
  font-weight: bold;
  color: ${(props) => props.theme.bgColor};
  background-color: ${(props) => props.theme.primaryColor};
  padding: 5px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
`;

export const SpanTitle = styled.h1`
  height: 100%;
  display: flex;
  align-items: center;
  font-size: 20px;
  font-weight: bold;
  color: ${(props) => props.theme.secondBlack};
`;

export const VideoInput = styled.input`
  display: none;
`;

export const VideoInputSection = styled.section`
  width: 100%;
  height: ${(props) => (props.$video_file === null ? "400px" : "auto")};
  max-height: ${(props) => (props.$video_file === null ? "400px" : "auto")};
  position: relative;
  background-color: ${(props) => props.theme.bgColor};
`;

export const Shadow = styled.div`
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  border-radius: 24px;
  background-color: ${(props) => props.theme.lightGray};
  color: ${(props) => props.theme.primaryColor};
  font-size: 36px;
  font-weight: 900;
  opacity: 0.7;
  z-index: 2;
`;

export const VideoUploadButton = styled.label`
  width: 80px;
  height: 80px;
  display: ${(props) => (props.$video_file === null ? "flex" : "none")};
  justify-content: center;
  align-items: center;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  border: ${(props) => `4px solid ${props.theme.lightGray}`};
  border-radius: 16px;
  background-color: transparent;
  cursor: pointer;
`;

export const SpinnerBox = styled.section`
  width: 80px;
  height: 80px;
  display: flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 1;
`;

export const VideoPreview = styled.video`
  width: 100%;
  height: 400px;
  object-fit: cover;
  position: relative;
`;

export const UploadedState = styled.p`
  width: 100%;
  position: absolute;
  top: 5%;
  font-size: 16px;
  font-weight: 900;
  text-align: center;
  color: ${(props) => props.theme.bgColor};
  z-index: 2;
`;

export const CheckIcon = styled(FontAwesomeIcon)`
  font-size: 62px;
  color: ${(props) => props.theme.primaryColor};
`;

export const FullIcon = styled.img`
  width: 100%;
  height: 100%;
`;

// Info Upload--------------------------------------------
export const InfoInputSection = styled.section`
  width: 100%;
  height: 350px;
  display: flex;
  gap: 20px;
  position: relative;
  background-color: ${(props) => props.theme.bgColor};
  border-radius: 24px;
  padding: 20px;
  @media all and (max-width: 1480px) {
  }
  @media all and (max-width: 900px) {
  }
`;

export const InfoTitleBox = styled.section`
  width: 33%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  gap: 15px;
`;

export const InfoTagBox = styled.section`
  width: 66%;
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: 10px;
`;

export const TitleWrapper = styled.section`
  min-width: 100%;
  display: flex;
  flex-direction: column;
  gap: 5px;
`;

export const SmallTitle = styled.h2`
  height: 30px;
  font-size: 16px;
  font-weight: bold;
  color: ${(props) => props.theme.secondBlack};
`;

export const TitleInput = styled.input`
  min-width: 100%;
  height: 30px;
  border: ${(props) => `1px solid ${props.theme.lightGray}`};
  border-radius: 16px;
  background-color: #f6f7f9;
  padding: 15px;
`;

export const TitleThumbnailWrapper = styled(TitleWrapper)``;

export const VideoThumbnailSection = styled(VideoInputSection)`
  height: 160px;
  min-height: auto;
  border: ${(props) => `1px solid ${props.theme.lightGray}`};
  @media all and (max-width: 900px) {
  }
`;

export const VideoThumbnailUploadInput = styled(VideoInput)``;

export const VideoThumbnailUploadButton = styled(VideoUploadButton)`
  display: ${(props) => (props.$thumbnail_file === null ? "flex" : "none")};
  width: 50px;
  height: 50px;
  border: none;
  z-index: 1;
`;

export const ThumbnailImage = styled.img`
  width: 100%;
  max-height: 100%;
  object-fit: cover;
`;

export const TagWrapper = styled.section`
  width: 100%;
  height: calc(100% - 30px);
  display: flex;
  gap: 10px;

  @media all and (max-width: 1480px) {
  }
  @media all and (max-width: 900px) {
  }
`;

export const TagCheckSection = styled.section`
  width: 50%;
  height: 100%;
  display: flex;
  flex-direction: column;
  border: ${(props) => `1px solid ${props.theme.lightGray}`};
  border-radius: 8px;
`;

export const TagTitle = styled.h2`
  width: 100%;
  min-height: 30px;
  display: flex;
  justify-content: center;
  align-items: center;
  color: ${(props) => props.theme.secondBlack};
  border-bottom: ${(props) => `1px solid ${props.theme.lightGray}`};
`;

export const TagScrollBox = styled.section`
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
`;

export const TagItemBox = styled.section`
  width: 100%;
  min-height: 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: ${(props) => `1px solid ${props.theme.lightGray}`};
  padding: 0 15px;
`;

export const NormalSpan = styled.span`
  color: ${(props) => props.theme.secondBlack};
`;

export const CheckBoxInput = styled.input`
  width: 20px;
  height: 20px;
  border: ${(props) => `1.5px solid ${props.theme.lightGray}`};
  user-select: none;
  cursor: pointer;
  &:checked {
    background-color: ${(props) => props.theme.primaryColor};
    color: white;
  }
`;

export const CheckBoxLabel = styled.label`
  width: 20px;
  height: 20px;
  border: ${(props) => `1.5px solid ${props.theme.lightGray}`};
  user-select: none;
  cursor: pointer;
  &:checked {
    background-color: ${(props) => props.theme.primaryColor};
    color: white;
  }
`;

// Ad Upload---------------------------------------------
export const AdUploadSection = styled(VideoUploadSection)`
  width: 30%;
  position: relative;
  background-color: ${(props) => props.theme.bgColor};
  border-radius: 24px;
  padding: 20px;
  overflow-y: auto;
  @media all and (max-width: 1480px) {
    width: 100%;
    min-height: 400px;
    overflow-y: visible;
  }
  @media all and (max-width: 900px) {
  }
`;

export const AdUploadGridBox = styled.section`
  width: 100%;
  height: 100%;
  max-height: 100%;
  display: grid;
  grid-template-columns: repeat(1, 1fr);
  grid-template-rows: repeat(auto-fit, 1fr);
  justify-items: center;
  gap: 40px;
  @media all and (max-width: 1480px) {
    grid-template-columns: ${(props) =>
      props.$adInputs.length === 0 ? `repeat(1, 1fr)` : `repeat(2, 1fr)`};
  }
  @media all and (max-width: 900px) {
    grid-template-columns: repeat(1, 1fr);
  }
`;

export const AdUploadButton = styled.button`
  width: 35px;
  height: 35px;
  border: none;
  background-color: transparent;
  cursor: pointer;
`;

export const TitleLeftBox = styled(TitleBetweenBox)`
  justify-content: flex-start;
  gap: 5px;
  border-bottom: 1px solid #c4c4c4;
`;

export const AdInputSection = styled.section`
  width: 100%;
  max-height: 350px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
  border-bottom: ${(props) => `1px solid ${props.theme.lightGray}`};
`;

export const TimeBox = styled.section`
  width: 100%;
  height: 70px;
  display: flex;
  align-items: center;
  gap: 15px;
`;

export const ContentBox = styled.section`
  width: 100%;
  height: 100px;
  display: flex;
  gap: 15px;
`;

export const LinkBox = styled(TimeBox)``;

export const RemoveButton = styled.button`
  width: 100%;
  height: 40px;
  border: ${(props) => `1px solid ${props.theme.lightGray}`};
  border-radius: 8px;
  background-color: ${(props) => props.theme.bgColor};
  color: ${(props) => props.theme.primaryColor};
  font-size: 20px;
  cursor: pointer;
`;

export const TimeInput = styled.input`
  width: 160px;
  height: 35px;
  text-align: center;
  letter-spacing: 2px;
  border: ${(props) => `1px solid ${props.theme.lightGray}`};
  border-radius: 16px;
  background-color: #f6f7f9;
  padding: 5px;
`;

export const AdInput = styled.textarea`
  width: ${(props) => props.width};
  height: ${(props) => props.height};
  display: flex;
  border: ${(props) => `1px solid ${props.theme.lightGray}`};
  border-radius: 16px;
  overflow-y: auto;
  padding: 8px;
  resize: none;
`;

export const SmallImage = styled.img`
  width: 32px;
  height: 32px;
`;
