export const BASE_PATH = "/api/v1";

export const USER_BASE_URL = BASE_PATH + "/user";
export const USER_SELF_BASE_URL = USER_BASE_URL + "/current";

export function getPatchUserUrl(userId?: string) {
  return `${USER_BASE_URL}/${userId}`;
}

export const ORDER_BASE_URL = BASE_PATH + "/order";
export const ORDER_EXPLORE_URL = ORDER_BASE_URL + "/available";
export const ORDER_MY_URL = ORDER_BASE_URL + "/my";

export function getOrderApplicationsUrl(orderId: string) {
  return `${ORDER_BASE_URL}/${orderId}/application`;
}

export function getOrderApplicationsChooseUrl(applicationId: string) {
  return `${ORDER_BASE_URL}/application/${applicationId}/choose`;
}

export function getOrderApplyForOrderUrl(orderId: string) {
  return `${ORDER_BASE_URL}/${orderId}/apply`;
}

export const NOTIFICATION_BASE_URL = BASE_PATH + "/notification";
