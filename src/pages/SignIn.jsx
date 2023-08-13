import { useNavigate } from "react-router-dom";
import { Wrapper } from "../components/Home/HomeStyle";
import * as S from "../components/Sign/SignStyle";
import logo from "../assets/images/logo.svg";
import landingImage from "../assets/images/travel.jpg";

const SELLOR_1_ID = "21d40e1a-86fc-480e-a4bf-b084f8ac6c55";
const SELLOR_2_ID = "e2d052e4-009b-44c4-963a-21996b29a779";
const SELLOR_3_ID = "14517d06-88cb-4edf-b6dd-67a63f469b6b";

const SignIn = () => {
  // Constant--------------------------------------------------
  const navigate = useNavigate();

  // State-----------------------------------------------------

  return (
    <Wrapper>
      <S.HalfSection style={{ backgroundColor: "#eaeaea" }}>
        <S.FullImage src={landingImage} alt="landingImage" loading="lazy" />
      </S.HalfSection>
      <S.HalfSection>
        <S.SignForm>
          <S.Logo src={logo} alt="logo" loading="lazy" />
          <S.SubText>판매자 로그인</S.SubText>
          <S.SignInput type="text" placeholder="ID" width="450px" />
          <S.SignInput type="text" placeholder="PW" width="450px" />
          <S.ColorButton width="450px" onClick={() => navigate(`/home/${SELLOR_3_ID}`)}>
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
