import { styled } from "styled-components";

export const LeftMiddleBox = styled.div`
  min-width: 70%;
  height: 100%;
  display: flex;
  @media all and (max-width: 1480px) {
    width: 100%;
    /* min-height: 100%; */
  }
  @media all and (max-width: 900px) {
  }
`;

export const LeftBox = styled.div`
  width: 40%;
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: ${(props) => props.theme.bgColor};
  border-radius: 24px;
  padding: 30px;
`;

export const MiddelBox = styled.div`
  width: 60%;
  height: 100%;
  padding: 30px;
  border: 1px solid green;
`;

export const RightBox = styled.div`
  width: 30%;
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: 15px;
  background-color: ${(props) => props.theme.bgColor};
  border-radius: 24px;
  padding: 30px;
  overflow-y: auto;
  @media all and (max-width: 1480px) {
    width: 100%;
    min-height: 50%;
    overflow-y: visible;
  }
  @media all and (max-width: 900px) {
  }
`;

// Video Modifiy-----------------------------------------------
export const VideoListWrapper = styled.section`
  width: 100%;
  height: calc(100% - 50px);
  max-height: calc(100% - 50px);
  display: flex;
  flex-direction: column;
  gap: 10px;
  overflow-y: auto;
  border: 1px solid green;
`;

export const VideoListBox = styled.section`
  width: 100%;
  height: 275px;
  display: flex;
  flex-direction: column;
  background-color: #d9d9d9;
  padding: 10px;
`;

export const VideoListThumbnail = styled.img`
  width: 100%;
  height: 180px;
  object-fit: contain;
  border: 1px solid green;
`;

export const VideoListInfo = styled.section`
  width: 100%;
  height: 75px;
  border: 1px solid green;
`;
