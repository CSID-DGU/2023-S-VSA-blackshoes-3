import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { keyframes, styled } from "styled-components";

export const wiggle = keyframes`
  0% {
    transform: translateX(0);
  }
  20% {
    transform: translateX(-5px) rotateZ(-5deg);
  }
  40% {
    transform: translateX(5px) rotateZ(5deg);
  }
  60% {
    transform: translateX(-5px) rotateZ(-5deg);
  }
  80% {
    transform: translateX(5px) rotateZ(5deg);
  }
  100% {
    transform: translateX(0);
  }
`;

export const LeftMiddleBox = styled.div`
  min-width: 70%;
  height: 100%;
  display: flex;
  gap: 20px;
  transition: all 0.3s;
  @media all and (max-width: 1480px) {
    width: 100%;
    /* min-height: 100%; */
  }
  @media all and (max-width: 1280px) {
    flex-direction: column;
  }
  @media all and (max-width: 900px) {
  }
`;

export const LeftBox = styled.div`
  width: 40%;
  min-width: 350px;
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: ${(props) => props.theme.bgColor};
  border-radius: 24px;
  padding: 20px;
  transition: all 0.3s;
  @media all and (max-width: 1280px) {
    width: 100%;
  }
  @media all and (max-width: 900px) {
  }
`;

export const MiddelBox = styled.div`
  width: 60%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  padding: 20px;
  transition: all 0.3s;
  @media all and (max-width: 1280px) {
    width: 100%;
  }
  @media all and (max-width: 900px) {
  }
`;

export const RightBox = styled.div`
  width: 30%;
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: 15px;
  background-color: ${(props) => props.theme.bgColor};
  border-radius: 24px;
  padding: 20px;
  overflow-y: auto;
  @media all and (max-width: 1480px) {
    width: 100%;
    min-height: 50%;
    overflow-y: visible;
  }
  @media all and (max-width: 1280px) {
  }
  @media all and (max-width: 900px) {
  }
`;

// Video List-----------------------------------------------
export const VideoListWrapper = styled.section`
  width: 100%;
  height: calc(100% - 50px);
  max-height: calc(100% - 50px);
  display: flex;
  flex-direction: column;
  gap: 10px;
  overflow-y: auto;
  padding-right: 10px;
  @media all and (max-width: 1280px) {
    align-items: center;
  }
`;

export const VideoListBox = styled.section`
  width: 100%;
  height: 275px;
  display: flex;
  flex-direction: column;
  padding: 10px;
  border: ${(props) =>
    props.videoid === props.clickedid ? `3px solid ${props.theme.secondBlack}` : ``};
  @media all and (max-width: 1280px) {
    max-width: 600px;
  }
`;

export const VideoListThumbnail = styled.img`
  width: 100%;
  height: 180px;
  object-fit: cover;
  border: 1px solid ${(props) => props.theme.middleGray};
`;

export const VideoListInfo = styled.section`
  width: 100%;
  height: 75px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: ${(props) => props.theme.bgColor};
  border: 1px solid ${(props) => props.theme.middleGray};
  border-top: none;
  padding-left: 5px;
`;

export const VideoEmptySection = styled.section`
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 20px;
`;

export const LargeFont = styled(FontAwesomeIcon)`
  font-size: 75px;
  color: ${(props) => props.theme.middleGray};
  animation: ${wiggle} 3s ease infinite alternate;
`;

export const BoldSpan = styled.span`
  font-weight: 900;
  font-size: 14px;
  color: ${(props) => props.theme.secondBlack};
`;

export const InfoRightWrapper = styled.section`
  width: calc(100% - 50px);
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
`;

export const InfoRightBox = styled.section`
  width: 100%;
  height: 22.5px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 5px;
`;

export const InfoSpan = styled.span`
  font-size: 14px;
`;

export const GreenSpan = styled(FontAwesomeIcon)`
  color: ${(props) => props.theme.primaryColor};
`;

export const LogoImage = styled.img`
  width: 100%;
  height: 100%;
`;

export const Select = styled.select`
  width: 100px;
  height: 30px;
  border: 1px solid ${(props) => props.theme.middleGray};
  border-radius: 5px;
  color: ${(props) => props.theme.secondBlack};
  padding: 0 10px;
`;

// Video Modify-----------------------------------------------
export const VideoModify = styled.video`
  width: 100%;
  min-width: 350px;
  height: 45%;
  max-height: 300px;
  object-fit: cover;
  position: "relative";
`;

export const VideoModifyWrapper = styled.section`
  width: 100%;
  min-width: 350px;
  height: 45%;
  max-height: 300px;
  display: ${(props) => (props.videourl ? "none" : "flex")};
  justify-content: center;
  align-items: center;
  font-size: 14px;
  font-weight: 900;
  color: ${(props) => props.theme.secondBlack};
  background-color: ${(props) => props.theme.bgColor};
  border-radius: 24px;
`;

export const VideoSource = styled.source`
  display: ${(props) => (props.videourl ? "block" : "none")};
`;

// Info Modify-----------------------------------------------
export const InfoModify = styled.section`
  width: 100%;
  height: 55%;
  min-height: 300px;
  display: flex;
  flex-direction: column;
  gap: 20px;
  background-color: ${(props) => props.theme.bgColor};
  border: none;
  border-radius: 24px;
  padding: 20px;
`;

export const InfoFlexBox = styled.section`
  width: 100%;
  height: 80px;
  display: flex;
  justify-content: space-between;
  gap: 10px;
`;

export const InfoVerticalBox = styled.section`
  width: 50%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
  gap: 10px;
`;

export const CenterBox = styled.section`
  width: 100%;
  height: 40px;
  display: flex;
  align-items: center;
`;

export const FileInput = styled.input`
  display: none;
`;

export const FileTextInput = styled.input`
  width: calc(100% - 85px);
  height: 40px;
  border: ${(props) => `1px solid ${props.theme.lightGray}`};
  border-radius: 16px 0 0 16px;
  background-color: #f6f7f9;
  padding: 15px;
`;

export const ExchangeButton = styled.label`
  width: 60px;
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: ${(props) => props.theme.primaryColor};
  color: ${(props) => props.theme.bgColor};
  border: none;
  border-radius: 0 16px 16px 0;
  font-size: 14px;
  font-weight: 900;
  cursor: pointer;
`;

export const TagSection = styled.section`
  width: 100%;
  height: calc(100% - 90px);
  display: flex;
`;

export const SecondBlackP = styled.p`
  width: 100%;
  color: ${(props) => props.theme.secondBlack};
  font-size: 14px;
`;
