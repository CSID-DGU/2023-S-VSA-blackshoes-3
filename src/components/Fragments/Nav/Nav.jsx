import { useNavigate, useParams } from "react-router-dom";
import * as H from "../../Home/HomeStyle";
import { useContext, useState } from "react";
import { GlobalContext } from "../../../context/GlobalContext";
import { MemoizedSetModal } from "../Reusable/SetModal";

const Nav = () => {
  // constant----------------------------------------------
  const navigate = useNavigate();
  const { page } = useContext(GlobalContext);
  const { userId } = useParams();

  // State-------------------------------------------------
  const [modal, setModal] = useState(false);

  return (
    <H.NavSection>
      <H.NavBox>
        <H.HoverButton0
          width="270px"
          onClick={() => navigate(`/home/${userId}`)}
          $page={page}
        >
          메인
        </H.HoverButton0>
        <H.HoverButton1
          width="270px"
          onClick={() => navigate(`/home/${userId}/upload`)}
          $page={page}
        >
          영상 업로드
        </H.HoverButton1>
        <H.HoverButton2
          width="270px"
          onClick={() => navigate(`/home/${userId}/manage`)}
          $page={page}
        >
          영상 관리
        </H.HoverButton2>
      </H.NavBox>
      <H.BorderButton width="270px" onClick={() => setModal(true)}>
        설정
      </H.BorderButton>
      <MemoizedSetModal modal={modal} setModal={setModal} />
    </H.NavSection>
  );
};

export default Nav;
