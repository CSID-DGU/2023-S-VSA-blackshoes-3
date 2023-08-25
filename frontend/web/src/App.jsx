import { GlobalStyle } from "./GlobalStyle";
import { Container } from "./components/Home/HomeStyle";
import Router from "./Router";
import { useState } from "react";
import { ThemeProvider } from "styled-components";
import { dark, light } from "./Theme";
import { GlobalContext } from "./context/GlobalContext";

function App() {
  // User Theme----------------------------------------
  const [themeMode, setThemeMode] = useState(false);

  const theme = !themeMode ? light : dark;

  // page Handling-------------------------------------
  const [page, setPage] = useState(0);

  return (
    <ThemeProvider theme={theme}>
      <GlobalContext.Provider value={{ page, setPage }}>
        <GlobalStyle />
        <Container>
          <Router />
        </Container>
      </GlobalContext.Provider>
    </ThemeProvider>
  );
}

export default App;
