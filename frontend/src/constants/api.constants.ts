export const BASE_PATH = "/api/v1";

export const USER_BASE_URL = BASE_PATH + "/user";
export const USER_SELF_BASE_URL = USER_BASE_URL + "/me";

export function getChangeUserPasswordUrl(username: string) {
  return `${USER_BASE_URL}/${username}/password`;
}

export const ORDER_BASE_URL = BASE_PATH + "/order";

export function getOrderApplicationsUrl(orderId: string) {
  return `${ORDER_BASE_URL}/${orderId}/application`;
}

export function getOrderApplicationsChooseUrl(applicationId: string) {
  return `${ORDER_BASE_URL}/application/${applicationId}/choose`;
}
