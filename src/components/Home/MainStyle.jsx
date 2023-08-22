import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { styled } from "styled-components";

export const MainSegment = styled.section`
  width: 750px;
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

export const StatisticBox = styled(MainSegment)`
  width: 100%;
  /* max-height: 773px; */
  border-radius: 24px;
  background-color: ${(props) => props.theme.bgColor};
  padding: 20px;
`;

export const MainTitle = styled.h1`
  width: 100%;
  position: relative;
  text-align: left;
  font-size: 20px;
  font-weight: bold;
  color: ${(props) => props.theme.secondBlack};
`;

export const SmallRightSpan = styled.span`
  display: ${(props) => (props.res === "right" ? "block" : "none")};
  position: absolute;
  top: 50%;
  right: 0;
  transform: translateY(-50%);
  font-size: 12px;
  font-weight: 100;
  color: ${(props) => props.theme.secondBlack};
  @media all and (max-width: 1480px) {
    display: ${(props) => (props.res === "left" ? "block" : "none")};
  }
`;

export const RefreshIcon = styled(FontAwesomeIcon)`
  font-size: 12px;
  color: ${(props) => props.theme.secondBlack};
  cursor: pointer;
  transition: all 0.3s;
  &:hover {
    transform: rotate(90deg);
  }
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
`;

export const StatisticEmptyBox = styled.section`
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
`;
