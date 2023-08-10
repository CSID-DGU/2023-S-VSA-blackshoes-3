import { useNavigate } from "react-router-dom";
import { useContext } from "react";
import { GlobalContext } from "../../../context/GlobalContext";
import { ResNavItem0, ResNavItem1, ResNavItem2, ResNavSection } from "../../Home/HomeStyle";

const ResNav = ({ userId }) => {
  const navigate = useNavigate();
  const { page } = useContext(GlobalContext);

  return (
    <ResNavSection>
      <ResNavItem0 onClick={() => navigate(`/home/${userId}`)} page={page}>
        메인
      </ResNavItem0>
      <ResNavItem1 onClick={() => navigate(`/home/${userId}/upload`)} page={page}>
        영상 업로드
      </ResNavItem1>
      <ResNavItem2 onClick={() => navigate(`/home/${userId}/manage`)} page={page}>
        영상 관리
      </ResNavItem2>
    </ResNavSection>
  );
};

export default ResNav;
