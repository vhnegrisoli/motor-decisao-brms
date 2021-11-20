import express from "express";
import { callAgeApi } from "./clients/IdadeClient.js";

const app = express();

app.use(express.json());

app.post("/api/wrapper", async (req, res) => {
  let data = req.body;
  let headers = req.headers;
  const BAD_REQUEST = 400;

  console.log(headers);

  if (!data.serviceId) {
    return res.status(BAD_REQUEST).json({
      status: BAD_REQUEST,
      message: "The serviceId must be informed.",
    });
  }

  if (!headers.transactionid) {
    return res.status(BAD_REQUEST).json({
      status: BAD_REQUEST,
      message: "The transactionId must be informed.",
    });
  }
  
  switch (data.serviceId) {
    case "CALCULO_IDADE":
      data = await callAgeApi(data, headers.transactionid);
      break;
    default:
      console.info("Nothing was consulted.");
  }

  return res.json(data);
});

const PORT = process.env.PORT || 3000;

app.listen(PORT, () => {
  console.info(`Wrapper started at port ${PORT}.`);
});
