import { Wrapper } from "../components/Home/HomeStyle";
import {
  ColorButton,
  ColorLabel,
  HalfSection,
  InputBox,
  LeftAlignSection,
  SignForm,
  SignInput,
  SignUpHead,
  SignUpTitle,
} from "../components/Sign/SignStyle";

const SignUp = () => {
  return (
    <Wrapper>
      <HalfSection style={{ backgroundColor: "#eaeaea" }}>Image Swiper</HalfSection>
      <HalfSection>
        <SignForm>
          <LeftAlignSection>
            <SignUpTitle>회원가입</SignUpTitle>
          </LeftAlignSection>
          <LeftAlignSection>
            <SignUpHead>Email</SignUpHead>
          </LeftAlignSection>
          <InputBox width="450px">
            <SignInput type="text" width="230px" placeholder="이메일을 입력해주세요." />
            @
            <SignInput type="select" width="180px" />
          </InputBox>
          <InputBox width="450px">
            <SignInput type="text" width="230px" placeholder="인증번호를 입력해주세요." />
            <ColorButton width="180px">인증번호 발송</ColorButton>
          </InputBox>
          <LeftAlignSection>
            <SignUpHead>비밀번호</SignUpHead>
          </LeftAlignSection>
          <SignInput type="text" width="450px" placeholder="사용하실 비밀번호를 입력해주세요." />
          <LeftAlignSection>
            <SignUpHead>비밀번호 확인</SignUpHead>
          </LeftAlignSection>
          <SignInput type="text" width="450px" placeholder="비밀번호를 확인해주세요." />
          <LeftAlignSection>
            <SignUpHead>회사명</SignUpHead>
          </LeftAlignSection>
          <SignInput type="text" width="450px" placeholder="회사명을 입력해주세요." />
          <InputBox width="450px">
            <SignInput type="text" width="280px" placeholder="파일을 입력해주세요." />
            <ColorLabel htmlFor="sign-input">파일 선택</ColorLabel>
            <SignInput type="file" width="130px" id="sign-input" style={{ display: "none" }} />
          </InputBox>
        </SignForm>
      </HalfSection>
    </Wrapper>
  );
};

export default SignUp;
