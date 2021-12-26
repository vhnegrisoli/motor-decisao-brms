import axios from "axios";
import { OBJECT, getResponseData } from "./responseUtils.js";
import { CPF_API_URL } from "../config/url.js";

export async function callValidDocumentApi(data, transactionid) {
  let document = data.payload.person.document;
  let response = getResponseData(data.serviceId, OBJECT);
  let headers = {
    transactionId: transactionid,
  };
  await axios
    .get(`${CPF_API_URL}/api/v1/cpf/${document}/valido`, {
      headers,
    })
    .then((res) => {
      data.payload.apiData.validCpf = res.data;
      response.status = res.status;
      response.success = true;
    })
    .catch((err) => {
      response.reason = err.message;
      response.status = err.status;
      console.error(
        `Erro ao tentar consultar serviço ${data.serviceId}: ${err.message}`
      );
    });
  if (!data.payload.consultedApis) {
    data.payload.consultedApis = [];
  }
  data.payload.consultedApis.push(response);
  return data;
}

export async function callCleanDocumentApi(data, transactionid) {
  let document = data.payload.person.document;
  let response = getResponseData(data.serviceId, OBJECT);
  let headers = {
    transactionId: transactionid,
  };
  await axios
    .get(`${CPF_API_URL}/api/v1/cpf/${document}/limpo`, {
      headers,
    })
    .then((res) => {
      data.payload.apiData.cleanCpf = res.data;
      response.status = res.status;
      response.success = true;
    })
    .catch((err) => {
      response.reason = err.message;
      response.status = err.status;
      console.error(`Erro ao tentar consultar serviço: ${err.message}`);
    });
  if (!data.payload.consultedApis) {
    data.payload.consultedApis = [];
  }
  data.payload.consultedApis.push(response);
  return data;
}
