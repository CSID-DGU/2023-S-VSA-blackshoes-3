import { styled } from "styled-components";

export const Container = styled.div`
  width: 100vw;
  height: 100vh;
`;

export const Wrapper = styled.div`
  width: 100%;
  height: 100%;
  display: flex;
`;

export const GridWrapper = styled.div`
  width: 100%;
  height: 100%;
  display: grid;
  grid-template-columns: 336px calc(100% - 336px);
  grid-template-rows: 100px calc(100% - 100px);
`;

// Header-------------------------------------------------------------
export const HeaderSection = styled.div`
  width: 100%;
  height: 100px;
  grid-column: 1 / 3;
  grid-row: 1 / 2;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: ${(props) => props.theme.bgColor};
  padding: 0 30px;
`;

export const HeaderRSection = styled.section`
  width: 300px;
  height: 100px;
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

export const HeaderInfoBox = styled.section`
  width: 130px;
  height: 50px;
  display: flex;
  align-items: center;
  gap: 10px;
  border: ${(props) => `1px solid ${props.theme.lightGray}`};
  border-radius: 16px;
  background-color: transparent;
  padding: 3px;
`;

export const LogoCircleBox = styled.section`
  width: 45px;
  height: 45px;
  display: flex;
  justify-content: center;
  align-items: center;
  border: none;
  border-radius: 30%;
  background-color: ${(props) => props.theme.lightGray};
  font-size: 12px;
`;
// Nav-------------------------------------------------------------
export const NavSection = styled.div`
  width: 336px;
  height: 100%;
  grid-column: 1 / 2;
  grid-row: 2 / 3;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  background-color: ${(props) => props.theme.bgColor};
  padding: 50px 0;
`;

export const NavBox = styled.div`
  width: 336px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
`;

export const HoverButton0 = styled.button`
  width: ${(props) => props.width};
  height: 50px;
  border: none;
  border-radius: 16px;
  background-color: ${(props) => (props.page === 0 ? props.theme.primaryColor : "transparent")};
  color: ${(props) => (props.page === 0 ? props.theme.bgColor : props.theme.textColor)};
  font-size: 14px;
  cursor: pointer;
  &:hover {
    text-decoration: underline;
  }
`;

export const HoverButton1 = styled(HoverButton0)`
  background-color: ${(props) => (props.page === 1 ? props.theme.primaryColor : "transparent")};
  color: ${(props) => (props.page === 1 ? props.theme.bgColor : props.theme.textColor)};
`;

export const HoverButton2 = styled(HoverButton0)`
  background-color: ${(props) => (props.page === 2 ? props.theme.primaryColor : "transparent")};
  color: ${(props) => (props.page === 2 ? props.theme.bgColor : props.theme.textColor)};
`;

export const BorderButton = styled.button`
  width: ${(props) => props.width};
  height: 50px;
  border: ${(props) => `1px solid ${props.theme.primaryColor}`};
  border-radius: 16px;
  background-color: transparent;
  color: ${(props) => props.theme.textColor};
  font-size: 14px;
  cursor: pointer;
  &:hover {
    background-color: ${(props) => props.theme.primaryColor};
    color: ${(props) => props.theme.bgColor};
  }
`;

// Body-------------------------------------------------------------
export const Body = styled.div`
  width: calc(100vw - 336px);
  height: 100%;
  grid-column: 2 / 3;
  grid-row: 2 / 3;
  background-color: #f6f7f9;
`;
