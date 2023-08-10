import { HeaderInfoBox, HeaderRSection, HeaderSection, LogoCircleBox } from "../../Home/HomeStyle";
import { ColorButton, Logo } from "../../Sign/SignStyle";
import logo from "../../../assets/images/logo.svg";
import { useNavigate } from "react-router-dom";

const Header = () => {
  const navigate = useNavigate();
  return (
    <HeaderSection>
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
    </HeaderSection>
  );
};

export default Header;
