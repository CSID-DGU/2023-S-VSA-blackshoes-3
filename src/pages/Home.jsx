import { useContext, useEffect, useState } from "react";
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
import { BASE_URL, Instance } from "../api/axios";

const Home = () => {
  // Constant----------------------------------------------------
  const { userId } = useParams();

  // State-------------------------------------------------------
  const { page, setPage } = useContext(GlobalContext);
  const [videoViewRank, setVideoViewRank] = useState([]);
  const [videoLikeRank, setVideoLikeRank] = useState([]);
  const [videoTagRank, setVideoTagRank] = useState([]);
  const [videoAdClickRank, setVideoAdClickRank] = useState([]);

  // Function----------------------------------------------------
  const fetchData = async () => {
    try {
      // 동영상 조회수 랭킹
      await Instance.get(`${BASE_URL}statistics-service/rank/videos/views/${userId}`).then(
        (res) => {
          console.log(res);
        }
      );

      // 동영상 좋아요 수 랭킹
      await Instance.get(`${BASE_URL}statistics-service/rank/videos/likes/${userId}`).then(
        (res) => {
          console.log(res);
        }
      );

      // 동영상 태그 랭킹
      await Instance.get(`${BASE_URL}statistics-service/rank/tags/views/${userId}`).then((res) => {
        console.log(res);
      });

      // 동영상 광고클릭 랭킹
      await Instance.get(`${BASE_URL}statistics-service/rank/videos/adClicks/${userId}`).then(
        (res) => {
          console.log(res);
        }
      );
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
          <MainTitle>조회수 통계</MainTitle>
          <AdSection>
            <MainSubTitle>광고 클릭 동영상 랭킹</MainSubTitle>
            <StatisticSection></StatisticSection>
            <br />
            <MainSubTitle>광고 클릭 태그 랭킹</MainSubTitle>
            <StatisticSection></StatisticSection>
          </AdSection>
        </MainSegment>
        <MainSegment>
          <MainTitle>상호작용 통계</MainTitle>
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
