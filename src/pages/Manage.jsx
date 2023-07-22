import { useNavigate } from "react-router-dom";
import Header from "../components/Fragments/Header";
import Nav from "../components/Fragments/Nav";
import { Body, GridWrapper } from "../components/Home/HomeStyle";
import { useContext, useEffect } from "react";
import { Context } from "../Context/Context";

const Manage = () => {
  // Constant----------------------------------------------------
  const navigate = useNavigate();

  // State-------------------------------------------------------
  const { setPage } = useContext(Context);
  // ComponentDidMount-------------------------------------------
  useEffect(() => {
    setPage(2);
  }, []);

  return (
    <GridWrapper>
      <Header />
      <Nav />
      <Body></Body>
    </GridWrapper>
  );
};

export default Manage;
