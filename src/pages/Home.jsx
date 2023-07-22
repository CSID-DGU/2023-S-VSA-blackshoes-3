import { useContext, useEffect } from "react";
import Header from "../components/Fragments/Header";
import Nav from "../components/Fragments/Nav";
import { Body, GridWrapper } from "../components/Home/HomeStyle";
import { useNavigate } from "react-router-dom";
import { Context } from "../Context/Context";
import axios from "axios";

// testSeller1 21d40e1a-86fc-480e-a4bf-b084f8ac6c55
// testSeller2 e2d052e4-009b-44c4-963a-21996b29a779
// testSeller3 badd288d-ea48-424c-9d1b-8e0fb4375094

const Home = () => {
  // Constant----------------------------------------------------
  const navigate = useNavigate();

  // State-------------------------------------------------------
  const { setPage } = useContext(Context);

  // Function----------------------------------------------------
  const fetchData = async () => {
    // try {
    //   const uploadData = await axios.get(
    //     `http://13.125.69.94:8011/content-slave-service/videos/sort?q=recent&page=0&size=10`
    //   );
    //   console.log(uploadData);
    // } catch (err) {
    //   console.log(err);
    // }
  };

  // ComponentDidMount-------------------------------------------
  useEffect(() => {
    setPage(0);
    fetchData();
  }, []);

  return (
    <GridWrapper>
      <Header />
      <Nav />
      <Body></Body>
    </GridWrapper>
  );
};

export default Home;
