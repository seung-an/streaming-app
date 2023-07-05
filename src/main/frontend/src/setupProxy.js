const { createProxyMiddleware } = require("http-proxy-middleware");

module.exports = function (app) {
  app.use(
    ["/api", "/member"],
    createProxyMiddleware({
      // target: "https://3.35.209.35:8080",
      target: "http://localhost:8080",
      changeOrigin: true,
    })
  );
};
