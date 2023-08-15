import { HeaderInfoBox, HeaderRSection, HeaderSection, LogoCircleBox } from "../../Home/HomeStyle";
import { ColorButton, Logo } from "../../Sign/SignStyle";
import logo from "../../../assets/images/logo.svg";
import { useNavigate } from "react-router-dom";
import { getCookie, removeCookie } from "../../../Cookie";
import axios from "axios";

const Header = () => {
  // Constant--------------------------------------------------
  const navigate = useNavigate();
  const refreshToken = getCookie("refreshToken");

  // Function--------------------------------------------------
  const submitLogout = async () => {
    if (window.confirm("로그아웃 하시겠습니까?")) {
      await axios
        .post(`http://13.125.69.94:8001/user-service/logout`, {
          refreshToken,
        })
        .then((res) => {
          console.log(res);
          localStorage.removeItem("accessToken");
          removeCookie("refreshToken");
          navigate(`/`, { replace: true });
        })
        .catch((err) => {
          console.log(err);
        });
    }
  };

  return (
    <HeaderSection>
      <Logo src={logo} alt="logo" loading="lazy" />
      <HeaderRSection>
        <HeaderInfoBox>
          <LogoCircleBox>LOGO</LogoCircleBox>
          회사명
        </HeaderInfoBox>
        <ColorButton width="130px" onClick={submitLogout}>
          로그아웃
        </ColorButton>
      </HeaderRSection>
    </HeaderSection>
  );
};

export default Header;
