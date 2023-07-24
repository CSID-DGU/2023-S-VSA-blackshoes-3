import { styled } from "styled-components";

export const MainSegment = styled.section`
  width: 750px;
  min-height: 800px;
  display: flex;
  flex-direction: column;
  align-items: center;
  flex-shrink: 0;
  gap: 20px;
  transition: all 0.3s;
  @media all and (max-width: 1900px) {
    width: 47%;
  }
  @media all and (max-width: 1480px) {
    width: 94%;
  }
`;

export const AdSection = styled(MainSegment)`
  width: 100%;
  align-items: flex-start;
  height: 773px;
  border-radius: 24px;
  background-color: ${(props) => props.theme.bgColor};
  padding: 30px;
`;

export const VideoSection = styled(AdSection)`
  width: 100%;
  height: 773px;
  overflow-y: auto;
`;

export const MainTitle = styled.h1`
  width: 100%;
  text-align: left;
  font-size: 20px;
  font-weight: bold;
  color: ${(props) => props.theme.secondBlack};
`;

export const MainSubTitle = styled(MainTitle)`
  font-size: 16px;
  font-weight: 600;
`;

export const StatisticSection = styled.section`
  width: 100%;
  min-width: 400px;
  min-height: 250px;
  max-height: 250px;
  border: 1px solid green;
`;
