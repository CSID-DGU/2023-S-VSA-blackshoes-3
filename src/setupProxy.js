const { createProxyMiddleware } = require("http-proxy-middleware");

module.exports = (app) => {
  app.use(
    createProxyMiddleware({
      target: "http://13.125.69.94:8001",
      changeOrigin: true,
    })
  );
};
