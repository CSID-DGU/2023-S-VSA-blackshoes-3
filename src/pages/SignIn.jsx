import { useNavigate } from "react-router-dom";
import { Wrapper } from "../components/Home/HomeStyle";
import {
  ColorButton,
  HalfSection,
  Logo,
  RightAlignSection,
  SignForm,
  SignInput,
  SignUpText,
  SubText,
} from "../components/Sign/SignStyle";
import logo from "../assets/images/logo.svg";

const SELLOR_1_ID = "21d40e1a-86fc-480e-a4bf-b084f8ac6c55";
const SELLOR_2_ID = "e2d052e4-009b-44c4-963a-21996b29a779";
const SELLOR_3_ID = "14517d06-88cb-4edf-b6dd-67a63f469b6b";

const SignIn = () => {
  // Constant--------------------------------------------------
  const navigate = useNavigate();

  // State-----------------------------------------------------

  return (
    <Wrapper>
      <HalfSection style={{ backgroundColor: "#eaeaea" }}>Image Swiper</HalfSection>
      <HalfSection>
        <SignForm>
          <Logo src={logo} alt="logo" loading="lazy" />
          <SubText>판매자 로그인</SubText>
          <SignInput type="text" placeholder="ID" width="450px" />
          <SignInput type="text" placeholder="PW" width="450px" />
          <ColorButton width="450px" onClick={() => navigate(`/home/${SELLOR_3_ID}`)}>
            시작하기
          </ColorButton>
          <RightAlignSection>
            <SignUpText onClick={() => navigate("/signup")}>회원가입</SignUpText>
          </RightAlignSection>
        </SignForm>
      </HalfSection>
    </Wrapper>
  );
};

export default SignIn;
