import {
  Body,
  BorderButton,
  GridWrapper,
  Header,
  HeaderInfoBox,
  HeaderRSection,
  HoverButton,
  LogoCircleBox,
  NavBox,
  NavSection,
} from "../components/Home/HomeStyle";
import { ColorButton, Logo } from "../components/Sign/SignStyle";
import logo from "../assets/images/logo.svg";
import { useNavigate } from "react-router-dom";

const Home = () => {
  const navigate = useNavigate();
  return (
    <GridWrapper>
      <Header>
        <Logo src={logo} alt="logo" loading="lazy" />
        <HeaderRSection>
          <HeaderInfoBox>
            <LogoCircleBox>LOGO</LogoCircleBox>
            회사명
          </HeaderInfoBox>
          <ColorButton width="130px" onClick={() => navigate("/")}>
            로그아웃
          </ColorButton>
        </HeaderRSection>
      </Header>
      <NavSection>
        <NavBox>
          <HoverButton width="270px">메인</HoverButton>
          <HoverButton width="270px">영상 업로드</HoverButton>
          <HoverButton width="270px">영상 관리</HoverButton>
        </NavBox>
        <BorderButton width="270px">탈퇴하기</BorderButton>
      </NavSection>
      <Body></Body>
    </GridWrapper>
  );
};

export default Home;
