import { useCallback, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import * as S from "../components/Sign/SignStyle";
import { Wrapper } from "../components/Home/HomeStyle";
import landingImage from "../assets/images/travel.jpg";
import axios from "axios";
import { useDebounce } from "../hooks/useDebounce";

// 13.125.69.94
// :8001 user
// :8011 content
// :8031 statistics
// :8041 comment
// :8051 personalized

// 210.94.179.19
// :9127 upload
const SignUp = () => {
  // Constant--------------------------------------------------
  const navigate = useNavigate();

  // Input State-----------------------------------------------
  const [email, setEmail] = useState("");
  const [emailValidation, setEmailValidation] = useState("");
  const [password, setPassword] = useState("");
  const [passwordCheck, setPasswordCheck] = useState("");
  const [companyName, setCompanyName] = useState("");
  const [sellerLogo, setSellorLogo] = useState(null);

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
        setEmailMessage("유효하지 않은 이메일 형식입니다.");
        setIsEmail(false);
      } else {
        setEmailMessage("유효한 이메일 형식입니다.");
        setIsEmail(true);
      }
    },
    [email]
  );

  // 비밀번호 유효성 관리----------------------------------------------------
  const onChangePassword = useCallback((e) => {
    const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$/;
    const passwordCurrent = e.target.value;
    setPassword(passwordCurrent);

    if (!passwordRegex.test(passwordCurrent)) {
      setPasswordMessage("숫자+영문자+특수문자를 조합하여 8글자 이상 작성하세요.");
      setIsPassword(false);
    } else {
      setPasswordMessage("유효한 비밀번호 형식입니다.");
      setIsPassword(true);
    }
  }, []);

  // 비밀번호 확인 유효성 관리-----------------------------------------------
  const onChangePasswordCheck = useCallback(
    (e) => {
      const password2Current = e.target.value;
      setPasswordCheck(password2Current);

      if (password === password2Current) {
        setPasswordCheckMessage("비밀번호 입력 일치합니다.");
        setIsPasswordCheck(true);
      } else {
        setPasswordCheckMessage("비밀번호 입력 불일치합니다.");
        setIsPasswordCheck(false);
      }
    },
    [password]
  );

  // 이메일 인증번호 발송----------------------------------------------------
  const sendEmail = async (e) => {
    e.preventDefault();
    try {
      await axios
        .post(`http://13.125.69.94:8001/user-service/mail/send-verification-code`, {
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
        .post(`http://13.125.69.94:8001/user-service/mail/verify-code`, {
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

  // 아이콘 등록--------------------------------------------------------------
  const handleIconFile = (e) => {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        // 파일 읽기 끝나면 실행
      };
      reader.readAsDataURL(file);
      setSellorLogo(file);
    } else {
      alert("파일을 등록하는데 실패했습니다.");
    }
  };

  // 회원가입--------------------------------------------------------------
  const submitSignUp = async (e) => {
    e.preventDefault();
    if (window.confirm("회원가입을 하시겠습니까?")) {
      try {
        const requestData = {
          email: email,
          password: password,
          companyName: companyName,
        };
        const jsonData = JSON.stringify(requestData);
        const blob = new Blob([jsonData], { type: "application/json" });
        const formData = new FormData();
        formData.append("joinRequest", blob);
        formData.append("sellerLogo", sellerLogo);
        await axios
          .post(`http://13.125.69.94:8001/user-service/sellers/join`, formData, {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          })
          .then((res) => {
            console.log(res);
            alert("회원가입이 완료되었습니다.");
            navigate("/");
          });
      } catch (err) {
        console.log(err);
        alert("회원가입에 실패했습니다.");
      }
    } else {
      return;
    }
  };

  return (
    <Wrapper>
      <S.HalfSection $position="left">
        <S.FullImage src={landingImage} alt="landingImage" loading="lazy" />
      </S.HalfSection>
      <S.HalfSection $position="right">
        <S.SignForm>
          <S.LeftAlignSection>
            <S.SignUpTitle>회원가입</S.SignUpTitle>
          </S.LeftAlignSection>
          <S.LeftAlignSection>
            <S.SignUpHead>Email</S.SignUpHead>
          </S.LeftAlignSection>
          <S.InputBox width="450px">
            <S.SignInput
              type="email"
              width="450px"
              placeholder="이메일을 입력해주세요."
              onChange={onChangeEmail}
              required
            />
          </S.InputBox>
          <S.FormHelperEmails is_email={isEmail ? "true" : "false"}>
            {emailMessage}
          </S.FormHelperEmails>
          <S.InputBox width="450px">
            <S.SignInput
              type="text"
              width="230px"
              placeholder="인증번호를 입력해주세요."
              onChange={(e) => setEmailValidation(e.target.value)}
              required
            />
            <S.ColorButton width="180px" onClick={sendEmail}>
              인증번호 발송
            </S.ColorButton>
          </S.InputBox>
          <S.FormHelperEmailValidation is_email_validation={isEmailValidation ? "true" : "false"}>
            {emailValidationMessage}
          </S.FormHelperEmailValidation>
          <S.LeftAlignSection>
            <S.SignUpHead>비밀번호</S.SignUpHead>
          </S.LeftAlignSection>
          <S.SignInput
            type="password"
            width="450px"
            placeholder="사용하실 비밀번호를 입력해주세요."
            onChange={onChangePassword}
            required
          />
          <S.FormHelperPWs is_password={isPassword ? "true" : "false"}>
            {passwordMessage}
          </S.FormHelperPWs>
          <S.LeftAlignSection>
            <S.SignUpHead>비밀번호 확인</S.SignUpHead>
          </S.LeftAlignSection>
          <S.SignInput
            type="password"
            width="450px"
            placeholder="비밀번호를 확인해주세요."
            onChange={onChangePasswordCheck}
            required
          />
          <S.FormHelperPWCF is_password_check={isPasswordCheck ? "true" : "false"}>
            {passwordCheckMessage}
          </S.FormHelperPWCF>
          <S.LeftAlignSection>
            <S.SignUpHead>회사명</S.SignUpHead>
          </S.LeftAlignSection>
          <S.SignInput
            type="text"
            width="450px"
            placeholder="회사명을 입력해주세요."
            onChange={(e) => setCompanyName(e.target.value)}
            required
          />
          <S.LeftAlignSection>
            <S.SignUpHead>아이콘 등록</S.SignUpHead>
          </S.LeftAlignSection>
          <S.InputBox width="450px">
            <S.SignInput
              type="text"
              width="280px"
              placeholder="파일을 선택해주세요."
              defaultValue={sellerLogo && sellerLogo.name}
              readOnly
            />
            <S.ColorLabel htmlFor="sign-input">파일 선택</S.ColorLabel>
            <S.SignInput
              type="file"
              accept="image/png, image/jpeg, image/jpg"
              width="130px"
              id="sign-input"
              style={{ display: "none" }}
              onChange={handleIconFile}
            />
          </S.InputBox>
          <br />
          <S.ColorButton width="450px" onClick={submitSignUp}>
            회원가입
          </S.ColorButton>
        </S.SignForm>
      </S.HalfSection>
    </Wrapper>
  );
};

export default SignUp;
