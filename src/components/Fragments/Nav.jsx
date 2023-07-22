import { useNavigate } from "react-router-dom";
import {
  BorderButton,
  HoverButton0,
  HoverButton1,
  HoverButton2,
  NavBox,
  NavSection,
} from "../Home/HomeStyle";
import { useContext } from "react";
import { Context } from "../../Context/Context";

const Nav = () => {
  const navigate = useNavigate();
  const { page } = useContext(Context);

  return (
    <NavSection>
      <NavBox>
        <HoverButton0 width="270px" onClick={() => navigate("/home/1")} page={page}>
          메인
        </HoverButton0>
        <HoverButton1 width="270px" onClick={() => navigate("/home/1/upload")} page={page}>
          영상 업로드
        </HoverButton1>
        <HoverButton2 width="270px" onClick={() => navigate("/home/1/manage")} page={page}>
          영상 관리
        </HoverButton2>
      </NavBox>
      <BorderButton width="270px">탈퇴하기</BorderButton>
    </NavSection>
  );
};

export default Nav;
