import { ListEntitiesRequest } from "../models/common.model.ts";

export const BASE_PATH = "/api/v1";

export const USER_BASE_URL = BASE_PATH + "/user";
export const USER_SELF_BASE_URL = USER_BASE_URL + "/current";

export function getPatchUserUrl(userId?: string) {
  return `${USER_BASE_URL}/${userId}`;
}

export const ORDER_BASE_URL = BASE_PATH + "/order";

export function getPatchOrderUrl(orderId: string) {
  return `${ORDER_BASE_URL}/${orderId}`;
}

export const REGULAR_ORDER_BASE_URL = ORDER_BASE_URL + "/regular";

export function getRegularOrderUrl(orderId: string) {
  return `${REGULAR_ORDER_BASE_URL}/${orderId}`;
}

export const PROMISSORY_NOTE_ORDER_BASE_URL =
  ORDER_BASE_URL + "/promissoryNote";
export const HEAD_HUNT_ORDER_BASE_URL = ORDER_BASE_URL + "/headHunt";

export function getCreateRegularOrderApplicationUrl(orderId: string) {
  return `${REGULAR_ORDER_BASE_URL}/${orderId}/applications`;
}

export function getListRegularOrderApplicationUrl(rsqlPredicate: string) {
  return `${REGULAR_ORDER_BASE_URL}/applications?rsqlPredicate${rsqlPredicate}`;
}

export const NOTIFICATION_BASE_URL = BASE_PATH + "/notification";

export const APPOINTMENT_SCHEDULE_BASE_URL = BASE_PATH + "/appointmentSchedule";

export function getCreateAppointmentUrl(scheduleId: string) {
  return `${APPOINTMENT_SCHEDULE_BASE_URL}/${scheduleId}/appointments`;
}

export function getListEntitiesUrl(
  baseUrl: string,
  request: ListEntitiesRequest | undefined,
) {
  if (!request) {
    return baseUrl;
  }

  const params = new URLSearchParams();
  if (request.rsqlPredicate) {
    params.append("rsqlPredicate", request.rsqlPredicate);
  }
  if (request.limit) {
    params.append("limit", String(request.limit));
  }
  if (request.offset) {
    params.append("offset", String(request.offset));
  }

  const paramsStr = params.toString();
  if (paramsStr.length > 0) {
    return `${baseUrl}?${paramsStr}`;
  } else {
    return baseUrl;
  }
}
