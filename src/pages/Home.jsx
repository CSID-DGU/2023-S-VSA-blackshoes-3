import { useContext, useEffect, useState } from "react";
import Header from "../components/Fragments/Nav/Header";
import Nav from "../components/Fragments/Nav/Nav";
import ResNav from "../components/Fragments/Nav/ResNav";
import { Body, GridWrapper } from "../components/Home/HomeStyle";
import * as M from "../components/Home/MainStyle";
import { useParams } from "react-router-dom";
import { GlobalContext } from "../context/GlobalContext";
import { BASE_URL, Instance } from "../api/axios";
import { Bar } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";
import { faBoxOpen, faRefresh } from "@fortawesome/free-solid-svg-icons";
import {
  BoldSpan,
  LargeFont,
  VideoEmptySection,
} from "../components/Fragments/Manage/ManageStyle";
import { getCookie } from "../Cookie";

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
);

const BACKGROUND_COLORS = [
  "rgba(255, 99, 132, 0.2)",
  "rgba(54, 162, 235, 0.2)",
  "rgba(255, 206, 86, 0.2)",
  "rgba(75, 192, 192, 0.2)",
  "rgba(153, 102, 255, 0.2)",
  "rgba(255, 159, 64, 0.2)",
  "rgba(255, 99, 132, 0.2)",
];

const BORDER_COLORS = [
  "rgba(255, 99, 132, 1)",
  "rgba(54, 162, 235, 1)",
  "rgba(255, 206, 86, 1)",
  "rgba(75, 192, 192, 1)",
  "rgba(153, 102, 255, 1)",
  "rgba(255, 159, 64, 1)",
  "rgba(255, 99, 132, 1)",
];

const OPTIONS = {
  responsive: true,
  maintainAspectRatio: false,
  scales: {
    y: {
      beginAtZero: true,
    },
  },
  plugins: {
    legend: {
      display: false,
    },
  },
};

const ACCESS_TOKEN = localStorage.getItem("accessToken");
const REFRESH_TOKEN = getCookie("refreshToken");

const HEADERS = {
  headers: {
    Authorization: `Bearer ${ACCESS_TOKEN}`,
  },
};

const CHART_DEFAULT_OPTIONS = {
  labels: [],
  datasets: [],
};

const Home = () => {
  // Constant----------------------------------------------------
  const { userId } = useParams();

  // State-------------------------------------------------------
  const { page, setPage } = useContext(GlobalContext);
  const [videoViewRank, setVideoViewRank] = useState(CHART_DEFAULT_OPTIONS);
  const [videoLikeRank, setVideoLikeRank] = useState(CHART_DEFAULT_OPTIONS);
  const [videoTagRank, setVideoTagRank] = useState(CHART_DEFAULT_OPTIONS);
  const [videoAdClickRank, setVideoAdClickRank] = useState(
    CHART_DEFAULT_OPTIONS
  );
  const [aggregatedAt, setAggregatedAt] = useState("");
  const [isRefresh, setIsRefresh] = useState(false);

  // Function----------------------------------------------------
  const fetchData = async () => {
    try {
      // 동영상 조회수 랭킹
      await Instance.get(
        `statistics-service/rank/videos/views/${userId}?refresh=${isRefresh}`,
        HEADERS
      ).then((res) => {
        setAggregatedAt(res.data.payload.aggregatedAt.slice(0, 19));
        const videoViewData = {
          labels: res.data.payload.videoViewRank.map((item) =>
            item.videoName.length > 8
              ? item.videoName.slice(0, 8) + "..."
              : item.videoName
          ),
          datasets: [
            {
              label: "동영상 조회수 랭킹",
              data: res.data.payload.videoViewRank.map((item) => item.views),
              backgroundColor: BACKGROUND_COLORS,
              borderColor: BORDER_COLORS,
              borderWidth: 1,
            },
          ],
        };
        setVideoViewRank(videoViewData);
      });
      // 동영상 좋아요 수 랭킹
      await Instance.get(
        `statistics-service/rank/videos/likes/${userId}?refresh=${isRefresh}`,
        HEADERS
      ).then((res) => {
        const videoLikeData = {
          labels: res.data.payload.videoLikeRank.map((item) =>
            item.videoName.length > 8
              ? item.videoName.slice(0, 8) + "..."
              : item.videoName
          ),
          datasets: [
            {
              label: "동영상 좋아요 수 랭킹",
              data: res.data.payload.videoLikeRank.map((item) => item.likes),
              backgroundColor: BACKGROUND_COLORS,
              borderColor: BORDER_COLORS,
              borderWidth: 1,
            },
          ],
        };
        setVideoLikeRank(videoLikeData);
      });
      // 동영상 태그 랭킹
      await Instance.get(
        `statistics-service/rank/tags/views/${userId}?refresh=${isRefresh}`,
        HEADERS
      ).then((res) => {
        const videoTagData = {
          labels: res.data.payload.tagViewRank.map((item) => item.tagName),
          datasets: [
            {
              label: "동영상 태그 랭킹",
              data: res.data.payload.tagViewRank.map((item) => item.views),
              backgroundColor: BACKGROUND_COLORS,
              borderColor: BORDER_COLORS,
              borderWidth: 1,
            },
          ],
        };
        setVideoTagRank(videoTagData);
      });
      // 동영상 광고클릭 랭킹
      await Instance.get(
        `statistics-service/rank/videos/adClicks/${userId}?refresh=${isRefresh}`,
        HEADERS
      ).then((res) => {
        const videoAdClickData = {
          labels: res.data.payload.videoAdClickRank.map((item) =>
            item.videoName.length > 8
              ? item.videoName.slice(0, 8) + "..."
              : item.videoName
          ),
          datasets: [
            {
              label: "동영상 광고클릭 랭킹",
              data: res.data.payload.videoAdClickRank.map(
                (item) => item.adClicks
              ),
              backgroundColor: BACKGROUND_COLORS,
              borderColor: BORDER_COLORS,
              borderWidth: 1,
            },
          ],
        };
        setVideoAdClickRank(videoAdClickData);
      });
    } catch (err) {
      console.log(err);
    }
  };

  // 데이터 집계일 새로고침-------------------------------------------
  const refreshAggregatedAt = async () => {
    try {
      setIsRefresh(true);
      await Instance.get(
        `statistics-service/rank/videos/views/${userId}?refresh=true`
      ).then((res) => {
        setAggregatedAt(res.data.payload.aggregatedAt.slice(0, 19));
        alert("데이터 집계일을 새로고침했습니다.");
      });
    } catch (err) {
      alert("데이터 집계일 새로고침에 실패했습니다.");
    }
  };

  // ComponentDidMount-------------------------------------------
  useEffect(() => {
    setPage(0);
    fetchData();
  }, []);

  return (
    <GridWrapper>
      <Header isRefresh={isRefresh} />
      <Nav />
      <Body>
        <ResNav userId={userId} />
        <M.MainSegment>
          <M.MainTitle>조회수 통계</M.MainTitle>
          <M.StatisticBox>
            <M.MainSubTitle>동영상 조회수 랭킹</M.MainSubTitle>
            <M.StatisticSection>
              {aggregatedAt === "" ? (
                <VideoEmptySection>
                  <LargeFont icon={faBoxOpen} />
                  <BoldSpan>업로드된 영상이 없습니다.</BoldSpan>
                </VideoEmptySection>
              ) : (
                <Bar data={videoViewRank} options={OPTIONS} />
              )}
            </M.StatisticSection>
            <br />
            <M.MainSubTitle>동영상 좋아요 랭킹</M.MainSubTitle>
            <M.StatisticSection>
              {aggregatedAt === "" ? (
                <VideoEmptySection>
                  <LargeFont icon={faBoxOpen} />
                  <BoldSpan>업로드된 영상이 없습니다.</BoldSpan>
                </VideoEmptySection>
              ) : (
                <Bar data={videoLikeRank} options={OPTIONS} />
              )}
            </M.StatisticSection>
          </M.StatisticBox>
        </M.MainSegment>
        <M.MainSegment>
          <M.MainTitle>
            상호작용 통계
            <M.SmallRightSpan>
              데이터 집계일: {aggregatedAt}{" "}
              <M.RefreshIcon icon={faRefresh} onClick={refreshAggregatedAt} />
            </M.SmallRightSpan>
          </M.MainTitle>
          <M.StatisticBox>
            <M.MainSubTitle>태그 조회수 랭킹</M.MainSubTitle>
            <M.StatisticSection>
              {aggregatedAt === "" ? (
                <VideoEmptySection>
                  <LargeFont icon={faBoxOpen} />
                  <BoldSpan>업로드된 영상이 없습니다.</BoldSpan>
                </VideoEmptySection>
              ) : (
                <Bar data={videoTagRank} options={OPTIONS} />
              )}
            </M.StatisticSection>
            <br />
            <M.MainSubTitle>동영장 광고클릭 랭킹</M.MainSubTitle>
            <M.StatisticSection>
              {aggregatedAt === "" ? (
                <VideoEmptySection>
                  <LargeFont icon={faBoxOpen} />
                  <BoldSpan>업로드된 영상이 없습니다.</BoldSpan>
                </VideoEmptySection>
              ) : (
                <Bar data={videoAdClickRank} options={OPTIONS} />
              )}
            </M.StatisticSection>
          </M.StatisticBox>
        </M.MainSegment>
      </Body>
    </GridWrapper>
  );
};

export default Home;
