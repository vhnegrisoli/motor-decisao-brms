import axios from "axios";
import { OBJECT, getResponseData } from "./responseUtils.js";

export async function callAgeApi(data, transactionid) {
  let birthday = data.person.birthday;
  let response = getResponseData(data.serviceId, OBJECT);
  let headers = {
    'transactionId': transactionid,
  };
  await axios
    .get(`http://localhost:8083/api/v1/idade/${birthday}`, { headers })
    .then((res) => {
      response.data = res.data;
      response.success = true;
    })
    .catch((err) => {
      console.error(`Erro ao tentar consultar serviço: ${err.message}`);
    });
  if (!data.consultedApis) {
    data.consultedApis = [];
  }
  data.consultedApis.push(response);
  return data;
}
