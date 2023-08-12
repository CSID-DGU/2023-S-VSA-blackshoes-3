import { useNavigate, useParams } from "react-router-dom";
import {
  BorderButton,
  HoverButton0,
  HoverButton1,
  HoverButton2,
  NavBox,
  NavSection,
} from "../../Home/HomeStyle";
import { useContext } from "react";
import { GlobalContext } from "../../../context/GlobalContext";

const Nav = () => {
  const navigate = useNavigate();
  const { page } = useContext(GlobalContext);
  const { userId } = useParams();

  return (
    <NavSection>
      <NavBox>
        <HoverButton0 width="270px" onClick={() => navigate(`/home/${userId}`)} $page={page}>
          메인
        </HoverButton0>
        <HoverButton1 width="270px" onClick={() => navigate(`/home/${userId}/upload`)} $page={page}>
          영상 업로드
        </HoverButton1>
        <HoverButton2 width="270px" onClick={() => navigate(`/home/${userId}/manage`)} $page={page}>
          영상 관리
        </HoverButton2>
      </NavBox>
      <BorderButton width="270px">탈퇴하기</BorderButton>
    </NavSection>
  );
};

export default Nav;
