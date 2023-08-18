import Modal from "react-modal";
import * as H from "../../Home/HomeStyle";
import * as S from "../../Sign/SignStyle";
import PropTypes from "prop-types";
import { useCallback, useEffect, useState } from "react";
import { BASE_URL } from "../../../api/axios";
import { useParams } from "react-router-dom";
import axios from "axios";
import { useDebounce } from "../../../hooks/useDebounce";

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
    width: "50%",
    minWidth: "350px",
  },
};
Modal.setAppElement("#root");

const PwModal = ({ modal, setModal }) => {
  // Constant-------------------------------------------------
  const { userId } = useParams();

  // State----------------------------------------------------
  const [email, setEmail] = useState("");
  const [emailValidation, setEmailValidation] = useState("");
  const [password, setPassword] = useState("");
  const [passwordCheck, setPasswordCheck] = useState("");

  // ErrorMessage State----------------------------------------
  const [emailMessage, setEmailMessage] = useState("");
  const [emailValidationMessage, setEmailValidationMessage] = useState("");
  const [passwordMessage, setPasswordMessage] = useState("");
  const [passwordCheckMessage, setPasswordCheckMessage] = useState("");

  // Validation State------------------------------------------
  const [isEmail, setIsEmail] = useState(false);
  const [isEmailValidation, setIsEmailValidation] = useState(false);
  const [isPassword, setIsPassword] = useState(false);
  const [isPasswordCheck, setIsPasswordCheck] = useState(false);

  // Email 유효성 관리------------------------------------------------------
  const onChangeEmail = useCallback(
    (e) => {
      const emailRegex =
        /([\w-.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
      const emailCurrent = e.target.value;
      setEmail(emailCurrent);

      if (!emailRegex.test(emailCurrent)) {
        setEmailMessage("유효하지 않은 이메일 형식");
        setIsEmail(false);
      } else {
        setEmailMessage("유효한 이메일 형식");
        setIsEmail(true);
      }
    },
    [email]
  );

  // 비밀번호 유효성 관리----------------------------------------------------
  const onChangePassword = useCallback((e) => {
    const passwordRegex =
      /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$/;
    const passwordCurrent = e.target.value;
    setPassword(passwordCurrent);

    if (!passwordRegex.test(passwordCurrent)) {
      setPasswordMessage("숫자+영문자+특수문자를 조합하여 8글자 이상 작성");
      setIsPassword(false);
    } else {
      setPasswordMessage("유효한 비밀번호 형식");
      setIsPassword(true);
    }
  }, []);

  // 비밀번호 확인 유효성 관리-----------------------------------------------
  const onChangePasswordCheck = useCallback(
    (e) => {
      const password2Current = e.target.value;
      setPasswordCheck(password2Current);

      if (password === password2Current) {
        setPasswordCheckMessage("비밀번호 입력 일치");
        setIsPasswordCheck(true);
      } else {
        setPasswordCheckMessage("비밀번호 입력 불일치");
        setIsPasswordCheck(false);
      }
    },
    [password]
  );

  // 이메일 인증번호 발송---------------------------------------
  const sendEmail = async (e) => {
    e.preventDefault();
    try {
      await axios
        .post(`${BASE_URL}user-service/mail/send-verification-code`, {
          email: email,
        })
        .then(() => {
          alert("인증번호가 발송되었습니다.");
        });
    } catch (err) {
      alert("이메일을 다시 확인해주세요.");
    }
  };

  // 이메일 인증번호 확인----------------------------------------------------
  const debouncedEmailValidation = useDebounce(emailValidation, 1000);
  const emailValidationCheck = async () => {
    try {
      await axios
        .post(`${BASE_URL}user-service/mail/verify-code`, {
          email: email,
          verificationCode: debouncedEmailValidation,
        })
        .then(() => {
          setIsEmailValidation(true);
          setEmailValidationMessage("인증번호 확인 완료");
        });
    } catch (err) {
      setIsEmailValidation(false);
      setEmailValidationMessage("인증번호 확인 실패");
    }
  };
  useEffect(() => {
    if (debouncedEmailValidation) {
      emailValidationCheck();
    }
  }, [debouncedEmailValidation]);

  const submitFindPassword = async (e) => {
    e.preventDefault();
    try {
      await axios
        .put(`${BASE_URL}user-service/sellers/password`, {
          email: email,
          password: password,
        })
        .then((res) => {
          console.log(res);
          alert("비밀번호가 변경되었습니다.");
          setModal(false);
        });
    } catch (err) {
      console.log(err);
      alert("비밀번호 변경에 실패하였습니다.");
    }
  };

  return (
    <Modal
      isOpen={modal}
      onRequestClose={() => setModal(false)}
      style={customStyles}
    >
      <H.ModalSection>
        <H.ModalTitle>회원 이메일 입력</H.ModalTitle>
        <H.ModalInputSection>
          <H.ModalBetweenBox width="70%">
            <H.ModalInput
              type="email"
              placeholder="회원가입 시 등록한 이메일을 입력하세요"
              onChange={onChangeEmail}
            />
            <H.ModalInput
              type="text"
              placeholder="인증번호를 입력하세요"
              onChange={(e) => setEmailValidation(e.target.value)}
            />
          </H.ModalBetweenBox>
          <S.ColorButton width="30%" onClick={sendEmail}>
            인증번호 전송
          </S.ColorButton>
        </H.ModalInputSection>
        <H.ModalInputSection>
          <H.ModalBetweenBox width="70%">
            <S.FormHelperEmails is_email={isEmail ? "true" : "false"}>
              {emailMessage}
            </S.FormHelperEmails>
            <S.FormHelperEmailValidation
              is_email_validation={isEmailValidation ? "true" : "false"}
            >
              {emailValidationMessage}
            </S.FormHelperEmailValidation>
          </H.ModalBetweenBox>
        </H.ModalInputSection>
        <H.ModalTitle>새 비밀번호 입력</H.ModalTitle>
        <H.ModalInputSection>
          <H.ModalBetweenBox width="70%">
            <H.ModalInput
              type="password"
              placeholder="새 비밀번호를 입력하세요"
              onChange={onChangePassword}
            />
            <H.ModalInput
              type="password"
              placeholder="비밀번호 일치 확인"
              onChange={onChangePasswordCheck}
            />
          </H.ModalBetweenBox>
          <S.ColorButton width="30%" onClick={submitFindPassword}>
            수정 완료
          </S.ColorButton>
        </H.ModalInputSection>
        <H.ModalInputSection>
          <H.ModalBetweenBox width="70%">
            <S.FormHelperPWs is_password={isPassword ? "true" : "false"}>
              {passwordMessage}
            </S.FormHelperPWs>
            <S.FormHelperPWCF
              is_password_check={isPasswordCheck ? "true" : "false"}
            >
              {passwordCheckMessage}
            </S.FormHelperPWCF>
          </H.ModalBetweenBox>
        </H.ModalInputSection>
      </H.ModalSection>
    </Modal>
  );
};

export default PwModal;

PwModal.propTypes = {
  modal: PropTypes.bool,
  setModal: PropTypes.func,
};
