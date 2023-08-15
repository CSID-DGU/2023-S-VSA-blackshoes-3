import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Wrapper } from "../components/Home/HomeStyle";
import * as S from "../components/Sign/SignStyle";
import logo from "../assets/images/logo.svg";
import landingImage from "../assets/images/travel.jpg";
import { getCookie, setCookie } from "../Cookie";
import axios from "axios";

const SELLOR_1_ID = "21d40e1a-86fc-480e-a4bf-b084f8ac6c55";
const SELLOR_2_ID = "e2d052e4-009b-44c4-963a-21996b29a779";
const SELLOR_3_ID = "14517d06-88cb-4edf-b6dd-67a63f469b6b";
// naver sellerId : "6b8484c1-5c2e-4c3a-a21c-54eb9a7681b5"

const SignIn = () => {
  // Constant--------------------------------------------------
  const navigate = useNavigate();
  const accessToken = localStorage.getItem("accessToken");
  const refreshToken = getCookie("refreshToken");

  // State-----------------------------------------------------
  const [inputs, setInputs] = useState({
    email: "",
    password: "",
  });
  const [isEmail, setIsEmail] = useState(false);
  const [isPassword, setIsPassword] = useState(false);
  const [emailMessage, setEmailMessage] = useState("");
  const [passwordMessage, setPasswordMessage] = useState("");

  const onChangeSignIn = async (e) => {
    const { name, value } = e.target;
    setInputs({
      ...inputs,
      [name]: value,
    });
  };

  const onSubmitSignIn = async (e) => {
    e.preventDefault();
    const { email, password } = inputs;
    const user = {
      email,
      password,
    };
    if (email && password) {
      setIsEmail(true);
      await axios
        .post(`http://13.125.69.94:8001/user-service/sellers/login`, user)
        .then((res) => {
          console.log(res);
          localStorage.setItem("accessToken", res.data.payload.accessToken);
          setCookie("refreshToken", res.data.payload.refreshToken);
          navigate(`/home/${res.data.payload.sellerId}`, { replace: true });
        })
        .catch((err) => {
          console.log(err);
        });
    }
  };

  return (
    <Wrapper>
      <S.HalfSection $position="left">
        <S.FullImage src={landingImage} alt="landingImage" loading="lazy" />
      </S.HalfSection>
      <S.HalfSection $position="right">
        <S.SignForm>
          <S.Logo src={logo} alt="logo" loading="lazy" />
          <S.SubText>판매자 로그인</S.SubText>
          <br />
          <S.SignInput
            type="email"
            name="email"
            placeholder="Email"
            width="450px"
            onChange={onChangeSignIn}
            required
          />
          <S.FormHelperEmails isemail={isEmail ? "true" : "false"}>
            {emailMessage}
          </S.FormHelperEmails>
          <S.SignInput
            type="password"
            name="password"
            placeholder="PW"
            width="450px"
            onChange={onChangeSignIn}
            required
          />
          <S.FormHelperPWs ispassword={isPassword ? "true" : "false"}>
            {passwordMessage}
          </S.FormHelperPWs>
          {/* <S.ColorButton width="450px" onClick={() => navigate(`/home/${SELLOR_3_ID}`)}> */}
          <S.ColorButton width="450px" onClick={onSubmitSignIn}>
            시작하기
          </S.ColorButton>
          <S.RightAlignSection>
            <S.SignUpText onClick={() => navigate("/signup")}>회원가입</S.SignUpText>
          </S.RightAlignSection>
        </S.SignForm>
      </S.HalfSection>
    </Wrapper>
  );
};

export default SignIn;
