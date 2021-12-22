import axios from "axios";
import {
  OBJECT,
  getResponseData
} from "./responseUtils.js";
import {
  DATA_VALIDA_API_URL
} from '../../src/config/url.js'

export async function callAgeApi(data, transactionid) {
  let birthday = data.payload.person.birthday;
  let response = getResponseData(data.serviceId, OBJECT);
  let headers = {
    transactionId: transactionid,
  };
  await axios
    .get(`${DATA_VALIDA_API_URL}/api/v1/idade/${birthday}`, {
      headers
    })
    .then((res) => {
      response.data = res.data;
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