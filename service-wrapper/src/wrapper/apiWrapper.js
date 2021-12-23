import {
  callAgeApi
} from "../clients/IdadeClient.js";
import {
  callPostcodeApi
} from "../clients/CepClient.js";
import {
  callValidDocumentApi,
  callCleanDocumentApi
} from "../clients/DocumentClient.js";
import WrapperException from "../exception/WrapperException.js";
import * as api from "./services.js";

const BAD_REQUEST = 400;
const INTERNAL_SERVER_ERROR = 500;

export async function getApiWrapper(data, headers) {
  try {
    validateInformedServiceId(data);
    validateInformedTransactionId(headers);
    if (isNotConsultedService(data)) {
      switch (data.serviceId) {
        case api.CALCULO_IDADE:
          data = await callAgeApi(data, headers.transactionid);
          break;
        case api.CEP_VALIDO:
          data = await callPostcodeApi(data, headers.transactionid);
          break;
        case api.CPF_VALIDO:
          data = await callValidDocumentApi(data, headers.transactionid);
          break;
        case api.CPF_LIMPO:
          data = await callCleanDocumentApi(data, headers.transactionid);
          break;
        default:
          throw new WrapperException(BAD_REQUEST, "Nothing was consulted.");
      }
    }
    delete data.serviceId;
    return data;
  } catch (error) {
    return {
      status: error.status ? error.status : INTERNAL_SERVER_ERROR,
      message: error.message,
    };
  }
}

function validateInformedServiceId(data) {
  if (!data.serviceId) {
    throw new WrapperException(BAD_REQUEST, "The serviceId must be informed.");
  }
}

function validateInformedTransactionId(headers) {
  if (!headers.transactionid) {
    throw new WrapperException(
      BAD_REQUEST,
      "The transactionId must be informed."
    );
  }
}

function isNotConsultedService(data) {
  if (data.payload.consultedApis) {
    let filteredBureau = data.payload.consultedApis.filter(api => {
      return api.serviceId === data.serviceId
    })
    return !filteredBureau || filteredBureau.length === 0;
  }
  return false;
}