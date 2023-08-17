import { FormHelperText } from "@mui/material";
import { styled } from "styled-components";

export const HalfSection = styled.section`
  width: 50%;
  height: 100%;
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: all 0, 3s;
  @media all and (max-width: 1080px) {
    width: ${(props) => (props.$position === "right" ? "100%" : "0")};
  }
`;

export const FullImage = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
`;

// 로그인----------------------------------------------------
export const SignForm = styled.form`
  width: 500px;
  height: 100%;
  max-height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 10px;
  overflow-y: auto;
`;

export const Logo = styled.img`
  width: 200px;
  height: auto;
`;

export const SubText = styled.h1`
  font-family: "MontserratBold";
  font-weight: 900;
  font-size: 16px;
  color: ${(props) => props.theme.secondBlack};
`;

export const SignInput = styled.input`
  width: ${(props) => props.width};
  height: 50px;
  border: none;
  background-color: ${(props) => props.theme.lightGray};
  color: ${(props) => props.theme.textColor};
  border-radius: 16px;
  padding: 15px;
  &:focus {
    outline: none;
  }
`;

export const ColorLabel = styled.label`
  width: 130px;
  height: 50px;
  display: flex;
  justify-content: center;
  align-items: center;
  border: none;
  border-radius: 16px;
  background-color: ${(props) => props.theme.primaryColor};
  font-size: 14px;
  color: ${(props) => props.theme.bgColor};
  cursor: pointer;
`;

export const ColorButton = styled.button`
  width: ${(props) => props.width};
  height: 50px;
  border: none;
  border-radius: 16px;
  background-color: ${(props) => props.theme.primaryColor};
  color: ${(props) => props.theme.bgColor};
  font-size: 14px;
  cursor: pointer;
`;

export const LeftAlignSection = styled.section`
  width: 450px;
  height: 30px;
  display: flex;
  justify-content: flex-start;
  align-items: center;
`;

export const RightAlignSection = styled.section`
  width: 450px;
  height: 30px;
  display: flex;
  justify-content: flex-end;
  align-items: center;
`;

export const SignUpText = styled.span`
  font-size: 14px;
  color: ${(props) => props.theme.middleGray};
  cursor: pointer;
`;

// 회원가입--------------------------------------------------
export const SignUpTitle = styled.h1`
  font-family: "MontserratBold";
  font-weight: 900;
  font-size: 20px;
  color: ${(props) => props.theme.secondBlack};
`;

export const SignUpHead = styled.h2`
  font-size: 14px;
  color: ${(props) => props.theme.secondBlack};
`;

export const InputBox = styled.section`
  width: ${(props) => props.width};
  height: 50px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 10px;
`;

export const ClauseBox = styled.section`
  width: 450px;
  height: 100px;
  border: none;
  background-color: ${(props) => props.theme.lightGray};
  color: ${(props) => props.theme.textColor};
  border-radius: 16px;
  padding: 15px;
`;

export const SignCheckBox = styled.input``;

// MUI-------------------------------------------------------
export const FormHelperEmails = styled(FormHelperText)`
  width: 450px;
  margin-left: 0 !important;
  font-weight: 700 !important;
  color: ${(props) =>
    props.is_email === "true" ? props.theme.primaryColor : props.theme.secondBlack} !important;
`;

export const FormHelperEmailValidation = styled(FormHelperText)`
  width: 450px;
  margin-left: 0 !important;
  font-weight: 700 !important;
  color: ${(props) =>
    props.is_email_validation === "true"
      ? props.theme.primaryColor
      : props.theme.secondBlack} !important;
`;

export const FormHelperPWs = styled(FormHelperText)`
  width: 450px;
  margin-left: 0 !important;
  font-weight: 700 !important;
  color: ${(props) =>
    props.is_password === "true" ? props.theme.primaryColor : props.theme.secondBlack} !important;
`;

export const FormHelperPWCF = styled(FormHelperText)`
  width: 450px;
  margin-left: 0 !important;
  font-weight: 700 !important;
  color: ${(props) =>
    props.is_password_check === "true"
      ? props.theme.primaryColor
      : props.theme.secondBlack} !important;
`;
