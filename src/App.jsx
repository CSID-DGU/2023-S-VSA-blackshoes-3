import { GlobalStyle } from "./GlobalStyle";
import { Container } from "./components/Home/HomeStyle";
import Router from "./Router";
import { useState } from "react";
import { ThemeProvider } from "styled-components";
import { dark, light } from "./Theme";
import { Context } from "./Context/Context";

function App() {
  // User Theme----------------------------------------
  const [themeMode, setThemeMode] = useState(false);

  const theme = !themeMode ? light : dark;

  // page Handling-------------------------------------
  const [page, setPage] = useState(0);

  return (
    <ThemeProvider theme={theme}>
      <Context.Provider value={{ page, setPage }}>
        <GlobalStyle />
        <Container>
          <Router />
        </Container>
      </Context.Provider>
    </ThemeProvider>
  );
}

export default App;
