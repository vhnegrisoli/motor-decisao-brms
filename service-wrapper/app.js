import express from "express";

const app = express();

app.use(express.json());

import { getApiWrapper } from "./src/wrapper/apiWrapper.js";

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
