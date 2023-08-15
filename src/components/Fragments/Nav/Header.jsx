import { HeaderInfoBox, HeaderRSection, HeaderSection, LogoCircleBox } from "../../Home/HomeStyle";
import { ColorButton, Logo } from "../../Sign/SignStyle";
import logo from "../../../assets/images/logo.svg";
import { useNavigate, useParams } from "react-router-dom";
import { getCookie, removeCookie } from "../../../Cookie";
import axios from "axios";
import { useEffect } from "react";
import { UserInstance } from "../../../api/axios";

const Header = () => {
  // Constant--------------------------------------------------
  const navigate = useNavigate();
  const accessToken = localStorage.getItem("accessToken");
  const refreshToken = getCookie("refreshToken");
  const { userId } = useParams();

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
          if (err.response.data.error === "리프레시 토큰은 비어 있거나 null일 수 없습니다.") {
            alert(err.response.data.error);
            localStorage.removeItem("accessToken");
            removeCookie("refreshToken");
            navigate(`/`, { replace: true });
          }
        });
    }
  };

  // ComponentDidMount-----------------------------------------
  const fetchData = async () => {
    try {
      const userData = await UserInstance.get(`/user-service/sellers/${userId}`, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      });
      console.log(userData);
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <HeaderSection>
      <Logo src={logo} alt="logo" loading="lazy" />
      <HeaderRSection>
        <HeaderInfoBox>
          <LogoCircleBox>LOGO</LogoCircleBox>
          회사명
        </HeaderInfoBox>
        <ColorButton width="130px" onClick={() => submitLogout()}>
          로그아웃
        </ColorButton>
      </HeaderRSection>
    </HeaderSection>
  );
};

export default Header;
