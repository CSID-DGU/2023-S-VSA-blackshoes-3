import { createGlobalStyle } from "styled-components";
import MontserratBlack from "./assets/fonts/Montserrat-Black.ttf";
import MontserratBold from "./assets/fonts/Montserrat-Bold.ttf";
import MontserratLight from "./assets/fonts/Montserrat-Light.ttf";
import MontserratMedium from "./assets/fonts/Montserrat-Medium.ttf";
import MontserratRegular from "./assets/fonts/Montserrat-Regular.ttf";
import MontserratThin from "./assets/fonts/Montserrat-Thin.ttf";

export const GlobalStyle = createGlobalStyle`
html, body, div, span, applet, object, iframe,
h1, h2, h3, h4, h5, h6, p, blockquote, pre,
a, abbr, acronym, address, big, cite, code,
del, dfn, em, img, ins, kbd, q, s, samp,
small, strike, strong, sub, sup, tt, var,
b, u, i, center,
dl, dt, dd, menu, ol, ul, li,
fieldset, form, label, legend,
table, caption, tbody, tfoot, thead, tr, th, td,
article, aside, canvas, details, embed,
figure, figcaption, footer, header, hgroup,
main, menu, nav, output, ruby, section, summary,
time, mark, audio, video {
  margin: 0;
  padding: 0;
  border: 0;
  font-size: 100%;
  font: inherit;
  vertical-align: baseline;
}

/* HTML5 display-role reset for older browsers */
article, aside, details, figcaption, figure,
footer, header, hgroup, main, menu, nav, section {
  display: block;
}
/* HTML5 hidden-attribute fix for newer browsers */
*[hidden] {
  display: none;
}
body {
  line-height: 1;
}
menu, ol, ul {
  list-style: none;
  padding-left: 0;
}
blockquote, q {
  quotes: none;
}
blockquote:before, blockquote:after,
q:before, q:after {
  content: '';
  content: none;
}
table {
  border-collapse: collapse;
  border-spacing: 0;
}
@font-face {
  font-family: "MontserratBlack";
  src: url(${MontserratBlack});
  font-weight: normal;
  font-style: normal;
}
@font-face {
  font-family: "MontserratBold";
  src: url(${MontserratBold});
  font-weight: normal;
  font-style: normal;
}
@font-face {
  font-family: "MontserratLight";
  src: url(${MontserratLight});
  font-weight: normal;
  font-style: normal;
}
@font-face {
  font-family: "MontserratMedium";
  src: url(${MontserratMedium});
  font-weight: normal;
  font-style: normal;
}
@font-face {
  font-family: "MontserratRegular";
  src: url(${MontserratRegular});
  font-weight: normal;
  font-style: normal;
}
@font-face {
  font-family: "MontserratThin";
  src: url(${MontserratThin});
  font-weight: normal;
  font-style: normal;
}

* {
  box-sizing: border-box;
}

*::-webkit-scrollbar {
  width: 7px;
  height: 7px;
}

*::-webkit-scrollbar-thumb {
  width: 7px;
  height: 7px;
  background: ${(props) => props.theme.primaryColor};
  border-radius: 10px;
}

*::selection {
  color: black;
  background: #daa520;
}

body {
  font-family: "MontserratMedium";
  background-color: ${(props) => props.theme.bgColor};
  color: ${(props) => props.theme.textColor};
}

`;
