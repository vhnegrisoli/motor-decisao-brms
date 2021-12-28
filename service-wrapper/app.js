import express from "express";
import swaggerUi from 'swagger-ui-express';
import {
  SWAGGER_DOCS
}
from './src/config/swagger.js';

import {
  getApiWrapper
} from "./src/wrapper/apiWrapper.js";

const app = express();

app.use(express.json());
app.use('/swagger-ui.html', swaggerUi.serve, swaggerUi.setup(SWAGGER_DOCS));

app.get("/", (req, res) => {
  return res.redirect("/swagger-ui.html");
});

app.post("/api/wrapper", async (req, res) => {
  let data = req.body;
  let headers = req.headers;

  let response = await getApiWrapper(data, headers);

  return res.json(response);
});

const PORT = process.env.PORT || 3000;

app.listen(PORT, () => {
  console.info(`Wrapper started at port ${PORT}.`);
});