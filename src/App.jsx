import { GlobalStyle } from "./GlobalStyle";
import { Container } from "./components/Home/HomeStyle";
import Router from "./Router";
import { useState } from "react";
import { ThemeProvider } from "styled-components";
import { dark, light } from "./Theme";

function App() {
  // User Theme----------------------------------------
  const [themeMode, setThemeMode] = useState(false);

  const theme = !themeMode ? light : dark;

  return (
    <ThemeProvider theme={theme}>
      <GlobalStyle />
      <Container>
        <Router />
      </Container>
    </ThemeProvider>
  );
}

export default App;
