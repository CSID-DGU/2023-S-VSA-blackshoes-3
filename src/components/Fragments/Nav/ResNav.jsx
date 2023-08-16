import { useNavigate, useParams } from "react-router-dom";
import { useContext } from "react";
import { GlobalContext } from "../../../context/GlobalContext";
import * as H from "../../Home/HomeStyle";

const ResNav = () => {
  const navigate = useNavigate();
  const { page } = useContext(GlobalContext);
  const { userId } = useParams();

  return (
    <H.ResNavSection>
      <H.ResNavItem0 onClick={() => navigate(`/home/${userId}`)} $page={page}>
        메인
      </H.ResNavItem0>
      <H.ResNavItem1 onClick={() => navigate(`/home/${userId}/upload`)} $page={page}>
        영상 업로드
      </H.ResNavItem1>
      <H.ResNavItem2 onClick={() => navigate(`/home/${userId}/manage`)} $page={page}>
        영상 관리
      </H.ResNavItem2>
    </H.ResNavSection>
  );
};

export default ResNav;
