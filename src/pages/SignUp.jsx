import { useCallback, useState } from "react";
import { useNavigate } from "react-router-dom";
import * as S from "../components/Sign/SignStyle";
import { Wrapper } from "../components/Home/HomeStyle";
import landingImage from "../assets/images/travel.jpg";

const SignUp = () => {
  // Constant--------------------------------------------------
  const navigate = useNavigate();

  // Input State-----------------------------------------------
  const [email, setEmail] = useState("");
  const [eamilValidation, setEmailValidation] = useState("");
  const [password, setPassword] = useState("");
  const [passwordCheck, setPasswordCheck] = useState("");
  const [coporateName, setCoporateName] = useState("");
  const [icon, setIcon] = useState(null);

  // ErrorMessage State----------------------------------------
  const [emailMessage, setEmailMessage] = useState("");
  const [passwordMessage, setPasswordMessage] = useState("");
  const [passwordCheckMessage, setPasswordCheckMessage] = useState("");

  // Validation State------------------------------------------
  const [isEmail, setIsEmail] = useState(false);
  const [isPassword, setIsPassword] = useState(false);
  const [isPasswordCheck, setIsPasswordCheck] = useState(false);

  // Email 유효성 관리-------------------------------------------------------
  const onChangeEmail = useCallback((e) => {
    const emailRegex =
      /([\w-.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
    const emailCurrent = e.target.value;
    setEmail(emailCurrent);

    if (!emailRegex.test(emailCurrent)) {
      setEmailMessage("유효하지 않은 이메일");
      setIsEmail(false);
    } else {
      setEmailMessage("유효한 이메일 형식");
      setIsEmail(true);
    }
  }, []);

  // 비밀번호 유효성 관리----------------------------------------------------
  const onChangePassword = useCallback((e) => {
    const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$/;
    const passwordCurrent = e.target.value;
    setPassword(passwordCurrent);

    if (!passwordRegex.test(passwordCurrent)) {
      setPasswordMessage("숫자+영문자+특수문자[8글자↑]");
      setIsPassword(false);
    } else {
      setPasswordMessage("비밀번호 보안 높음");
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

  return (
    <Wrapper>
      <S.HalfSection style={{ backgroundColor: "#eaeaea" }}>
        <S.FullImage src={landingImage} alt="landingImage" loading="lazy" />
      </S.HalfSection>
      <S.HalfSection>
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
              width="230px"
              placeholder="이메일을 입력해주세요."
              onChange={onChangeEmail}
              required
            />
            @
            <S.SignSelect width="180px">
              <option value="naver.com">naver.com</option>
              <option value="gmail.com">gmail.com</option>
              <option value="daum.net">daum.net</option>
              <option value="nate.com">nate.com</option>
            </S.SignSelect>
          </S.InputBox>
          <S.FormHelperEmails is_email={isEmail ? "true" : "false"}>
            {emailMessage}
          </S.FormHelperEmails>
          <S.InputBox width="450px">
            <S.SignInput type="text" width="230px" placeholder="인증번호를 입력해주세요." />
            <S.ColorButton width="180px">인증번호 발송</S.ColorButton>
          </S.InputBox>
          <S.LeftAlignSection>
            <S.SignUpHead>비밀번호</S.SignUpHead>
          </S.LeftAlignSection>
          <S.SignInput
            type="password"
            width="450px"
            placeholder="사용하실 비밀번호를 입력해주세요."
            onChange={onChangePassword}
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
            onChange={(e) => setCoporateName(e.target.value)}
          />
          <S.LeftAlignSection>
            <S.SignUpHead>아이콘 등록</S.SignUpHead>
          </S.LeftAlignSection>
          <S.InputBox width="450px">
            <S.SignInput type="text" width="280px" placeholder="파일을 입력해주세요." />
            <S.ColorLabel htmlFor="sign-input">파일 선택</S.ColorLabel>
            <S.SignInput type="file" width="130px" id="sign-input" style={{ display: "none" }} />
          </S.InputBox>
          <br />
          <S.ColorButton width="450px">회원가입</S.ColorButton>
        </S.SignForm>
      </S.HalfSection>
    </Wrapper>
  );
};

export default SignUp;
