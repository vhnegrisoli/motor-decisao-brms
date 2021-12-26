import axios from "axios";
import { OBJECT, getResponseData } from "./responseUtils.js";
import { CEP_VALIDO_API_URL } from "../config/url.js";

export async function callPostcodeApi(data, transactionid) {
  let postcode = data.payload.person.postcode;
  let response = getResponseData(data.serviceId, OBJECT);
  let headers = {
    transactionId: transactionid,
  };
  await axios
    .get(`${CEP_VALIDO_API_URL}/api/v1/cep/${postcode}`, {
      headers,
    })
    .then((res) => {
      data.payload.apiData.cep = res.data;
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
