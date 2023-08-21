import Modal from "react-modal";
import * as H from "../../Home/HomeStyle";
import * as S from "../../Sign/SignStyle";
import PropTypes from "prop-types";
import React, { useEffect, useState } from "react";
import { BASE_URL, Instance } from "../../../api/axios";
import { useParams } from "react-router-dom";

const customStyles = {
  content: {
    backgroundColor: "white",
    border: "1px solid #1DAE86",
    borderRadius: "16px",
    outline: "none",
    padding: "20px",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    width: "50%",
    minWidth: "350px",
  },
};

Modal.setAppElement("#root");

const SetModal = ({ modal, setModal }) => {
  // Constant----------------------------------------------
  const { userId } = useParams();
  // State-------------------------------------------------
  const [sellerName, setSellerName] = useState("");
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [sellerLogo, setSellerLogo] = useState(null);

  // function----------------------------------------------
  const submitNewName = async () => {
    try {
      await Instance.put(
        `${BASE_URL}user-service/sellers/${userId}/sellerName`,
        {
          sellerName,
        }
      ).then((res) => {
        alert("이름이 변경되었습니다.");
      });
    } catch (err) {
      console.log(err);
      alert(err.response.data.error);
    }
  };

  const submitNewLogo = async () => {
    const formData = new FormData();
    formData.append("sellerLogo", sellerLogo);
    try {
      await Instance.put(
        `${BASE_URL}user-service/sellers/${userId}/sellerLogo`,
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      ).then((res) => {
        alert("로고가 변경되었습니다.");
      });
    } catch (err) {
      console.log(err);
      alert(err.response.data.error);
    }
  };

  const submitNewPassword = async () => {
    try {
      await Instance.put(`${BASE_URL}user-service/sellers/${userId}/password`, {
        oldPassword,
        newPassword,
      }).then(() => {
        alert("비밀번호가 변경되었습니다.");
        setOldPassword("");
        setNewPassword("");
      });
    } catch (err) {
      console.log(err);
      alert(err.response.data.error);
    }
  };

  // ComponentDidMount-------------------------------------
  const fetchData = async () => {
    try {
      await Instance.get(`${BASE_URL}user-service/sellers/${userId}`).then(
        (res) => {
          setSellerName(res.data.payload.sellerName);
          setSellerLogo(res.data.payload.sellerLogo);
        }
      );
    } catch (err) {
      console.log(err);
    }
  };
  useEffect(() => {
    fetchData();
  }, []);
  console.log("실행됨");
  return (
    <Modal
      isOpen={modal}
      onRequestClose={() => setModal(false)}
      style={customStyles}
    >
      <H.ModalSection>
        <H.ModalTitle>회원 이름 수정</H.ModalTitle>
        <H.ModalInputSection>
          <H.ModalInput
            type="text"
            placeholder="변경할 유저 이름을 작성하세요"
            defaultValue={sellerName && sellerName}
            onChange={(e) => setSellerName(e.target.value)}
          />
          <S.ColorButton width="30%" onClick={submitNewName}>
            수정
          </S.ColorButton>
        </H.ModalInputSection>
        <H.ModalTitle>회원 비밀번호 수정</H.ModalTitle>
        <H.ModalInputSection>
          <H.ModalBetweenBox width="70%">
            <H.ModalInput
              type="password"
              placeholder="기존 비밀번호를 작성하세요"
              onChange={(e) => setOldPassword(e.target.value)}
            />
            <H.ModalInput
              type="password"
              placeholder="변경할 비밀번호를 작성하세요"
              onChange={(e) => setNewPassword(e.target.value)}
            />
          </H.ModalBetweenBox>
          <S.ColorButton width="30%" onClick={submitNewPassword}>
            수정
          </S.ColorButton>
        </H.ModalInputSection>
        <H.ModalTitle>회원 로고 수정</H.ModalTitle>
        <H.ModalInputSection>
          <H.ModalInput
            type="text"
            placeholder="로고 이미지"
            defaultValue={sellerLogo && sellerLogo}
          />
          <H.ModalBetweenBox width="30%">
            <H.ModalFileInput
              id="file-input"
              type="file"
              accept="image/"
              placeholder="로고 이미지"
              onChange={(e) => setSellerLogo(e.target.files[0])}
            />
            <H.ModalFileInputLabel htmlFor="file-input">
              로고 업로드
            </H.ModalFileInputLabel>
            <S.ColorButton width="80px" onClick={submitNewLogo}>
              수정
            </S.ColorButton>
          </H.ModalBetweenBox>
        </H.ModalInputSection>
      </H.ModalSection>
    </Modal>
  );
};

export const MemoizedSetModal = React.memo(SetModal);

SetModal.propTypes = {
  modal: PropTypes.bool,
  setModal: PropTypes.func,
};
