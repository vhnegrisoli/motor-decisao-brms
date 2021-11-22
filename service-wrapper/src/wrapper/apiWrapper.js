import { callAgeApi } from "../clients/IdadeClient.js";
import WrapperException from "../exception/WrapperException.js";
import * as api from "./services.js";

const BAD_REQUEST = 400;
const INTERNAL_SERVER_ERROR = 500;

export async function getApiWrapper(data, headers) {
  try {
    validateInformedServiceId(data);
    validateInformedTransactionId(headers);
    switch (data.serviceId) {
      case api.CALCULO_IDADE:
        data = await callAgeApi(data, headers.transactionid);
        break;
      default:
        throw new WrapperException(BAD_REQUEST, "Nothing was consulted.");
    }
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
