import Modal from "react-modal";
import * as H from "../../Home/HomeStyle";
import * as S from "../../Sign/SignStyle";
import PropTypes from "prop-types";

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
    width: "30%",
    minWidth: "350px",
  },
};

Modal.setAppElement("#root");

const SetModal = ({ modal, setModal }) => {
  // function----------------------------------------------
  const closeModal = () => {
    setModal(false);
  };

  return (
    <Modal isOpen={modal} onRequestClose={closeModal} style={customStyles}>
      <H.ModalSection>
        <H.ModalInputSection>
          <H.ModalInput
            type="text"
            placeholder="수정할 유저 이름을 작성하세요"
          />
          <S.ColorButton width="15%">수정</S.ColorButton>
        </H.ModalInputSection>
        <H.ModalInputSection></H.ModalInputSection>
        <H.ModalInputSection></H.ModalInputSection>
      </H.ModalSection>
    </Modal>
  );
};

export default SetModal;

SetModal.propTypes = {
  modal: PropTypes.bool,
  setModal: PropTypes.func,
};
