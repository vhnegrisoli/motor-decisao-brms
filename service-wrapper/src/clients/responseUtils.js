export const OBJECT = "OBJECT";
export const ARRAY = "ARRAY";

export function getResponseData(serviceId, returnType) {
  return {
    data: returnType === "OBJECT" ? {} : [],
    success: false,
    id: serviceId,
    status: null,
    reason: null,
  };
}
