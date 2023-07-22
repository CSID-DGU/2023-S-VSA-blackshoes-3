import { GlobalStyle } from "./GlobalStyle";
import { Container } from "./components/Home/HomeStyle";
import Router from "./Router";

function App() {
  return (
    <>
      <GlobalStyle />
      <Container>
        <Router />
      </Container>
    </>
  );
}

export default App;
