import { styled } from "styled-components";

export const Container = styled.div`
  width: 100vw;
  height: 100vh;
  @media all and (max-width: 900px) {
    height: auto;
    min-height: 100vh;
  }
`;

export const Wrapper = styled.div`
  width: 100%;
  height: 100%;
  display: flex;
`;

export const GridWrapper = styled.div`
  height: 100%;
  display: grid;
  grid-template-columns: 336px calc(100% - 336px);
  grid-template-rows: 100px calc(100% - 100px);
  overflow: hidden;
  @media all and (max-width: 900px) {
    grid-template-columns: 1fr;
  }
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
  z-index: 1;
  @media all and (max-width: 900px) {
    position: sticky;
    top: 0;
  }
`;

export const HeaderRSection = styled.section`
  min-width: 300px;
  height: 100px;
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

export const HeaderInfoBox = styled.section`
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
  height: calc(100vh - 100px);
  grid-column: 1 / 2;
  grid-row: 2 / 3;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  background-color: ${(props) => props.theme.bgColor};
  padding: 50px 0;
  transition: all 0.3s;
  @media all and (max-width: 900px) {
    grid-column: 0;
    grid-row: 0;
    transform: translate(-336px, 0);
  }
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
  background-color: ${(props) =>
    props.$page === 0 ? props.theme.primaryColor : "transparent"};
  color: ${(props) =>
    props.$page === 0 ? props.theme.bgColor : props.theme.textColor};
  font-size: 14px;
  cursor: pointer;
`;

export const HoverButton1 = styled(HoverButton0)`
  background-color: ${(props) =>
    props.$page === 1 ? props.theme.primaryColor : "transparent"};
  color: ${(props) =>
    props.$page === 1 ? props.theme.bgColor : props.theme.textColor};
`;

export const HoverButton2 = styled(HoverButton0)`
  background-color: ${(props) =>
    props.$page === 2 ? props.theme.primaryColor : "transparent"};
  color: ${(props) =>
    props.$page === 2 ? props.theme.bgColor : props.theme.textColor};
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

export const ResNavSection = styled.section`
  width: 100%;
  height: 50px;
  display: none;
  justify-content: space-between;
  align-items: center;
  @media all and (max-width: 900px) {
    display: flex;
  }
`;

export const ResNavItem0 = styled.section`
  width: 25%;
  min-width: 100px;
  height: 40px;
  display: none;
  justify-content: center;
  align-items: center;
  background-color: transparent;
  color: ${(props) => props.theme.textColor};
  border: none;
  border-bottom: ${(props) =>
    props.$page === 0 ? `2px solid ${props.theme.primaryColor}` : "none"};
  cursor: pointer;
  @media all and (max-width: 900px) {
    display: flex;
  }
`;

export const ResNavItem1 = styled(ResNavItem0)`
  border-bottom: ${(props) =>
    props.$page === 1 ? `2px solid ${props.theme.primaryColor}` : "none"};
`;

export const ResNavItem2 = styled(ResNavItem0)`
  border-bottom: ${(props) =>
    props.$page === 2 ? `2px solid ${props.theme.primaryColor}` : "none"};
`;

// Body-------------------------------------------------------------
export const Body = styled.div`
  width: calc(100vw - 336px);
  max-height: calc(100vh - 100px);
  grid-column: 2 / 3;
  grid-row: 2 / 3;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  gap: 20px;
  background-color: #f6f7f9;
  padding: 20px;
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

// Modal-------------------------------------------------------------
export const ModalSection = styled.section`
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 25px;
`;

export const ModalTitle = styled.h2`
  width: 100%;
  text-align: left;
  font-weight: 900;
  color: ${(props) => props.theme.secondBlack};
`;

export const ModalInputSection = styled.section`
  width: 100%;
  height: 50px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 10px;
`;

export const ModalBetweenBox = styled.section`
  width: ${(props) => props.width};
  height: 100%;
  display: flex;
  gap: 10px;
`;

export const ModalInput = styled.input`
  width: 70%;
  height: 100%;
  border: none;
  border-radius: 16px;
  background-color: ${(props) => props.theme.lightGray};
  padding: 0 20px;
  &:focus {
    outline: none;
  }
`;

export const ModalFileInput = styled(ModalInput)`
  display: none;
`;

export const ModalFileInputLabel = styled.label`
  width: calc(100% - 80px);
  height: 100%;
  border: none;
  border-radius: 16px;
  background-color: ${(props) => props.theme.primaryColor};
  padding: 0 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  color: ${(props) => props.theme.bgColor};
  font-size: 14px;
  cursor: pointer;
`;
