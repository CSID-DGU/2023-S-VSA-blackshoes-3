import { useContext, useEffect } from "react";
import Header from "../components/Fragments/Nav/Header";
import Nav from "../components/Fragments/Nav/Nav";
import ResNav from "../components/Fragments/Nav/ResNav";
import { Body, GridWrapper } from "../components/Home/HomeStyle";
import {
  AdSection,
  MainSegment,
  MainSubTitle,
  MainTitle,
  StatisticSection,
  VideoSection,
} from "../components/Home/MainStyle";
import { useParams } from "react-router-dom";
import { GlobalContext } from "../context/GlobalContext";

import axios from "axios";

// testSeller1 21d40e1a-86fc-480e-a4bf-b084f8ac6c55
// testSeller2 e2d052e4-009b-44c4-963a-21996b29a779
// testSeller3 badd288d-ea48-424c-9d1b-8e0fb4375094

const Home = () => {
  // Constant----------------------------------------------------

  const { userId } = useParams();

  // State-------------------------------------------------------
  const { page, setPage } = useContext(GlobalContext);

  // Function----------------------------------------------------
  const fetchData = async () => {
    try {
      // const uploadData = await axios.get(
      //   `http://13.125.69.94:8011/content-slave-service/videos/${userId}/sort?q=recent&page=0&size=10`
      // );
      // console.log(uploadData);
    } catch (err) {
      console.log(err);
    }
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
      <Body>
        <ResNav userId={userId} />
        <MainSegment>
          <MainTitle>광고 통계</MainTitle>
          <AdSection>
            <MainSubTitle>광고 클릭 동영상 랭킹</MainSubTitle>
            <StatisticSection></StatisticSection>
            <br />
            <MainSubTitle>광고 클릭 태그 랭킹</MainSubTitle>
            <StatisticSection></StatisticSection>
          </AdSection>
        </MainSegment>
        <MainSegment>
          <MainTitle>영상 통계</MainTitle>
          <VideoSection>
            <MainSubTitle>동영장 조회수 랭킹</MainSubTitle>
            <StatisticSection></StatisticSection>
            <br />
            <MainSubTitle>동영장 좋아요 수 랭킹</MainSubTitle>
            <StatisticSection></StatisticSection>
            <br />
            <MainSubTitle>동영장 태그 랭킹</MainSubTitle>
            <StatisticSection></StatisticSection>
          </VideoSection>
        </MainSegment>
      </Body>
    </GridWrapper>
  );
};

export default Home;
