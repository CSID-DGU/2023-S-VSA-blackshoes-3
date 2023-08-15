import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getCookie, removeCookie } from "../../../Cookie";
import * as H from "../../Home/HomeStyle";
import * as S from "../../Sign/SignStyle";
import logo from "../../../assets/images/logo.svg";
import axios from "axios";
import { UserInstance } from "../../../api/axios";

const Header = () => {
  // Constant--------------------------------------------------
  const navigate = useNavigate();
  const accessToken = localStorage.getItem("accessToken");
  const refreshToken = getCookie("refreshToken");
  const { userId } = useParams();

  // State-----------------------------------------------------
  const [sellerLogo, setSellerLogo] = useState(null);
  const [sellerName, setSellerName] = useState("");

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
      await UserInstance.get(`/user-service/sellers/${userId}`, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      }).then((res) => {
        setSellerLogo(res.data.payload.sellerLogo);
        setSellerName(res.data.payload.sellerName);
      });
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <H.HeaderSection>
      <S.Logo src={logo} alt="logo" loading="lazy" />
      <H.HeaderRSection>
        <H.HeaderInfoBox>
          <H.LogoCircleBox>
            <S.FullImage src={`data:image/;base64,${sellerLogo}`} alt="sellorLogo" loading="lazy" />
          </H.LogoCircleBox>
          {sellerName}
        </H.HeaderInfoBox>
        <S.ColorButton width="130px" onClick={() => submitLogout()}>
          로그아웃
        </S.ColorButton>
      </H.HeaderRSection>
    </H.HeaderSection>
  );
};

export default Header;
