import { useContext, useEffect } from "react";
import Header from "../components/Fragments/Header";
import Nav from "../components/Fragments/Nav";
import { Body, GridWrapper } from "../components/Home/HomeStyle";
import { useNavigate, useParams } from "react-router-dom";
import { Context } from "../Context/Context";
import ResNav from "../components/Fragments/ResNav";

const Upload = () => {
  // Constant----------------------------------------------------
  const navigate = useNavigate();
  const { userId } = useParams();

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
      <Body>
        <ResNav userId={userId} />
      </Body>
    </GridWrapper>
  );
};

export default Upload;
