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
  display: ${(props) => (props.preview === null ? "flex" : "none")};
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
  height: 300px;
  display: flex;
  gap: 20px;
  background-color: ${(props) => props.theme.bgColor};
  border-radius: 24px;
  padding: 30px;
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
  justify-content: space-between;
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
  gap: 10px;
`;

export const SmallTitle = styled.h2`
  font-size: 16px;
  font-weight: bold;
  color: ${(props) => props.theme.secondBlack};
`;

export const TitleInput = styled.input`
  min-width: 100%;
  height: 40px;
  border: ${(props) => `1px solid ${props.theme.lightGray}`};
  border-radius: 16px;
  background-color: #f6f7f9;
  padding: 15px;
`;

export const TitleThumbnailWrapper = styled(TitleWrapper)``;

export const VideoThumbnailSection = styled(VideoInputSection)`
  height: 100px;
  border: ${(props) => `1px solid ${props.theme.lightGray}`};
  @media all and (max-width: 900px) {
    height: 120px;
  }
`;

export const VideoThumbnailUploadInput = styled(VideoInput)``;

export const VideoThumbnailUploadButton = styled(VideoUploadButton)`
  width: 50px;
  height: 50px;
  border: none;
`;

export const TagWrapper = styled.section`
  width: 100%;
  height: 100%;
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
  border: ${(props) => `1px solid ${props.theme.lightGray}`};
  border-radius: 8px;
`;

// Ad Upload---------------------------------------------
export const AdUploadSection = styled(VideoUploadSection)`
  width: 30%;
  background-color: ${(props) => props.theme.bgColor};
  border-radius: 24px;
  padding: 30px;
  overflow-y: auto;
  @media all and (max-width: 1480px) {
    width: 100%;
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
  align-items: center;
  gap: 10px;
  @media all and (max-width: 1480px) {
    grid-template-columns: repeat(2, 1fr);
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
`;

export const TitleLeftBox = styled(TitleBetweenBox)`
  justify-content: flex-start;
  gap: 15px;
  border-bottom: 1px solid #c4c4c4;
`;

export const AdInputSection = styled.section`
  width: 100%;
  height: 300px;
  border: 1px solid green;

  @media all and (max-width: 1030px) {
    width: 100%;
  }
  @media all and (max-width: 900px) {
    width: 100%;
  }
`;
