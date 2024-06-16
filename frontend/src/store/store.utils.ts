import {
  AUTHORIZATION_HEADER,
  AUTHORIZATION_HEADER_BASIC_PREFIX,
  X_REQUEST_ID_HEADER,
} from "../constants/http-headers.constants.ts";
import { v4 as uuid } from "uuid";
import { AUTHORIZATION_HEADER_STORAGE_KEY } from "../constants/local-storage.constant.ts";

export function setCommonHeaders(headers: Headers) {
  headers.set(X_REQUEST_ID_HEADER, uuid().toString());
  const authHeaderValue = prepareAuthorizationHeaderValue();
  if (authHeaderValue) {
    headers.set(AUTHORIZATION_HEADER, authHeaderValue);
  }
}

export function prepareAuthorizationHeaderValue(): string | null {
  const base64Value = localStorage.getItem(AUTHORIZATION_HEADER_STORAGE_KEY);
  if (base64Value) {
    return AUTHORIZATION_HEADER_BASIC_PREFIX + base64Value;
  }

  return null;
}
