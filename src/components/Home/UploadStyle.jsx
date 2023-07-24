import { styled } from "styled-components";

// Video Upload--------------------------------------------
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

export const AdUploadSection = styled(VideoUploadSection)`
  width: 30%;
  background-color: ${(props) => props.theme.bgColor};
  border-radius: 24px;
  padding: 30px;
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
`;

export const SpanTitle = styled.h1`
  font-size: 20px;
  font-weight: bold;
  color: ${(props) => props.theme.secondBlack};
`;

export const SmallTitle = styled.h2`
  font-size: 16px;
  font-weight: bold;
  color: ${(props) => props.theme.secondBlack};
`;

export const VideoInput = styled.input`
  display: none;
`;

export const VideoInputSection = styled.section`
  width: 100%;
  height: 500px;
  position: relative;
  background-color: ${(props) => props.theme.bgColor};
`;

export const VideoUploadButton = styled.label`
  width: 80px;
  height: 80px;
  display: flex;
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

export const FullIcon = styled.img`
  width: 100%;
  height: auto;
`;

// Info Upload--------------------------------------------
export const InfoInputSection = styled.section`
  width: 100%;
  height: 400px;
  display: flex;
  background-color: ${(props) => props.theme.bgColor};
  border-radius: 24px;
  /* padding: 30px; */
  @media all and (max-width: 1480px) {
  }
  @media all and (max-width: 900px) {
  }
`;

export const InfoTitleBox = styled.section``;

// Ad Upload---------------------------------------------
export const AdUploadButton = styled.button`
  width: 35px;
  height: 35px;
  border: none;
  background-color: transparent;
`;

export const TitleLeftBox = styled(TitleBetweenBox)`
  justify-content: flex-start;
  gap: 15px;
  border-bottom: 1px solid #c4c4c4;
`;
