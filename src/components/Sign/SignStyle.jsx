import { styled } from "styled-components";

export const HalfSection = styled.section`
  width: 50%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  border: 2px solid green;
`;

export const SignForm = styled.form`
  width: 500px;
  height: 600px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 10px;
  border: 2px solid green;
`;

export const Logo = styled.img`
  width: 200px;
  height: auto;
`;

export const SubText = styled.h1`
  font-weight: 600;
  font-size: 20px;
`;

export const SignInput = styled.input`
  width: 90%;
  height: 50px;
  border: none;
  background-color: ${(props) => props.theme.lightGray};
  color: ${(props) => props.theme.textColor};
  border-radius: 16px;
  padding: 15px;
`;

export const ColorButton = styled.button`
  width: ${(props) => props.width};
  height: 50px;
  border: none;
  border-radius: 16px;
  background-color: ${(props) => props.theme.primaryColor};
  color: ${(props) => props.theme.bgColor};
  cursor: pointer;
`;

export const RightAlignSection = styled.section`
  width: 90%;
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
