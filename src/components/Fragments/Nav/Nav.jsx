import { useNavigate, useParams } from "react-router-dom";
import * as H from "../../Home/HomeStyle";
import { useContext, useState } from "react";
import { GlobalContext } from "../../../context/GlobalContext";
import Modal from "react-modal";

const customStyles = {
  content: {
    backgroundColor: "white",
    border: "2px solid #1DAE86",
    borderRadius: "4px",
    outline: "none",
    padding: "20px",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    maxWidth: "1000px",
    width: "80%",
  },
};

Modal.setAppElement("#root");

const Nav = () => {
  // constant----------------------------------------------
  const navigate = useNavigate();
  const { page } = useContext(GlobalContext);
  const { userId } = useParams();

  // State-------------------------------------------------
  const [modal, setModal] = useState(false);

  // function----------------------------------------------
  const openModal = () => {
    setModal(true);
  };

  const closeModal = () => {
    setModal(false);
  };

  return (
    <H.NavSection>
      <H.NavBox>
        <H.HoverButton0 width="270px" onClick={() => navigate(`/home/${userId}`)} $page={page}>
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
        <H.HoverButton0 width="270px" onClick={openModal}>
          설정
        </H.HoverButton0>
        <Modal isOpen={modal} onRequestClose={closeModal} style={customStyles}>
          <H.ModalSection>
            <H.ModalInputSection></H.ModalInputSection>
            <H.ModalInputSection></H.ModalInputSection>
          </H.ModalSection>
        </Modal>
      </H.NavBox>
      <H.BorderButton width="270px">탈퇴하기</H.BorderButton>
    </H.NavSection>
  );
};

export default Nav;
