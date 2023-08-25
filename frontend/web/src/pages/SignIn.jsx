import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Wrapper } from "../components/Home/HomeStyle";
import * as S from "../components/Sign/SignStyle";
import logo from "../assets/images/logo.svg";
import landingImage from "../assets/images/travel.jpg";
import { getCookie, setCookie } from "../Cookie";
import axios from "axios";
import { BASE_URL } from "../api/axios";
import PwModal from "../components/Fragments/Reusable/PwModal";

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
  const [modal, setModal] = useState(false);

  // Function--------------------------------------------------
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
    if (email === "" && password === "") {
      setIsEmail(false);
      setIsPassword(false);
      setEmailMessage("이메일을 입력해주세요.");
      setPasswordMessage("비밀번호를 입력해주세요.");
    } else if (email === "" && password !== "") {
      setIsEmail(false);
      setIsPassword(true);
      setEmailMessage("이메일을 입력해주세요.");
      setPasswordMessage("");
    } else if (email !== "" && password === "") {
      setIsEmail(true);
      setIsPassword(false);
      setPasswordMessage("비밀번호를 입력해주세요.");
      setEmailMessage("");
    } else if (email && password) {
      setIsEmail(true);
      await axios
        .post(`${BASE_URL}user-service/sellers/login`, user)
        .then((res) => {
          localStorage.setItem("accessToken", res.data.payload.accessToken);
          setCookie("refreshToken", res.data.payload.refreshToken);
          return res.data.payload.sellerId;
        })
        .then((sellerId) => {
          navigate(`/home/${sellerId}`, { replace: true });
        })
        .catch((err) => {
          console.log(err);
          if (
            err.response?.data.error === `Seller not found with email: ${email}`
          ) {
            alert(err.response?.data.error);
            return;
          } else if (err.response?.data.error === "Invalid password.") {
            alert(err.response?.data.error);
            return;
          } else {
            alert(err.code);
          }
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
            autoComplete="email"
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
            autoComplete="password"
          />
          <S.FormHelperPWs ispassword={isPassword ? "true" : "false"}>
            {passwordMessage}
          </S.FormHelperPWs>
          {/* <S.ColorButton width="450px" onClick={() => navigate(`/home/${SELLOR_3_ID}`)}> */}
          <S.ColorButton width="450px" onClick={onSubmitSignIn}>
            시작하기
          </S.ColorButton>
          <S.RightAlignSection>
            <S.SignUpText onClick={() => setModal(true)}>
              비밀번호 찾기
            </S.SignUpText>
            <PwModal modal={modal} setModal={setModal} />
            <S.SignUpText onClick={() => navigate("/signup")}>
              회원가입
            </S.SignUpText>
          </S.RightAlignSection>
        </S.SignForm>
      </S.HalfSection>
    </Wrapper>
  );
};

export default SignIn;
