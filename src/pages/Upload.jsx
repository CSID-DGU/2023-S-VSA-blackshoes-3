import { useContext, useEffect } from "react";
import Header from "../components/Fragments/Header";
import Nav from "../components/Fragments/Nav";
import { Body, GridWrapper } from "../components/Home/HomeStyle";
import { useNavigate } from "react-router-dom";
import { Context } from "../Context/Context";

const Upload = () => {
  // Constant----------------------------------------------------
  const navigate = useNavigate();

  // State-------------------------------------------------------
  const { setPage } = useContext(Context);
  // ComponentDidMount-------------------------------------------
  useEffect(() => {
    setPage(1);
  }, []);

  return (
    <GridWrapper>
      <Header />
      <Nav />
      <Body></Body>
    </GridWrapper>
  );
};

export default Upload;
