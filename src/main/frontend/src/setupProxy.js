const { createProxyMiddleware } = require("http-proxy-middleware");

module.exports = function (app) {
  app.use(
    ["/api", "/member"],
    createProxyMiddleware({
      // target: "https://15.164.212.176:8080",
      target: "http://localhost:8080",
      changeOrigin: true,
    })
  );
};
