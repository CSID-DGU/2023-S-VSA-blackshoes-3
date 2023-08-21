import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getCookie, removeCookie } from "../../../Cookie";
import * as H from "../../Home/HomeStyle";
import * as S from "../../Sign/SignStyle";
import logo from "../../../assets/images/logo.svg";
import axios from "axios";
import { BASE_URL, Instance } from "../../../api/axios";
import Proptypes from "prop-types";

const Header = ({ isRefresh }) => {
  // Constant--------------------------------------------------
  const navigate = useNavigate();
  const { userId } = useParams();

  // State-----------------------------------------------------
  const [sellerLogo, setSellerLogo] = useState(null);
  const [sellerName, setSellerName] = useState("");

  // Function--------------------------------------------------
  const removeAll = () => {
    localStorage.removeItem("accessToken");
    removeCookie("refreshToken", { path: "/" });
    console.clear();
    navigate(`/`, { replace: true });
  };

  const submitLogout = async () => {
    if (window.confirm("로그아웃 하시겠습니까?")) {
      const refreshToken = getCookie("refreshToken");
      await axios
        .post(`${BASE_URL}user-service/logout`, {
          refreshToken,
        })
        .then(() => {
          removeAll();
        })
        .catch((err) => {
          console.log(err);
          if (
            err.response.data.error ===
              "리프레시 토큰은 비어 있거나 null일 수 없습니다." ||
            err.response.data.error ===
              "로그아웃 오류: 리프레시 토큰이 일치하지 않습니다."
          ) {
            alert(err.response.data.error);
            // removeAll();
          }
        });
    }
  };

  // ComponentDidMount-----------------------------------------
  const fetchData = async () => {
    const accessToken = localStorage.getItem("accessToken");
    const refreshToken = getCookie("refreshToken");
    if (refreshToken && accessToken) {
      try {
        await Instance.get(`user-service/sellers/${userId}`, {
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
    } else {
      alert("로그인이 필요한 서비스입니다.");
      removeAll();
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
            <S.FullImage
              // data: URL 스키마를 사용하여 base64 데이터를 직접 src에 할당
              src={`data:image/;base64,${sellerLogo}`}
              alt="sellorLogo"
              loading="lazy"
              style={{ borderRadius: "10px" }}
            />
          </H.LogoCircleBox>
          {sellerName}
        </H.HeaderInfoBox>
        <S.ColorButton width="130px" onClick={submitLogout}>
          로그아웃
        </S.ColorButton>
      </H.HeaderRSection>
    </H.HeaderSection>
  );
};

export default Header;

Header.propTypes = {
  isRefresh: Proptypes.bool,
};
