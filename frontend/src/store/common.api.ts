import {
  BaseQueryFn,
  createApi,
  FetchArgs,
  fetchBaseQuery,
  FetchBaseQueryError,
} from "@reduxjs/toolkit/query/react";
import { AUTHORIZATION_HEADER_STORAGE_KEY } from "../constants/local-storage.constant.ts";
import { setCommonHeaders } from "./store.utils.ts";
import { REDIRECT_TO_LOGIN_API_BLACKLIST } from "../constants/store.contants.ts";
import { clearCurrentUser } from "./current-user/current-user.slice.ts";
import { LOGIN_PAGE_PATH } from "../constants/route.constants.ts";

const baseQuery = fetchBaseQuery({
  prepareHeaders: (headers) => setCommonHeaders(headers),
});

const baseQueryWithRedirectIfUnauthorized: BaseQueryFn<
  string | FetchArgs,
  unknown,
  FetchBaseQueryError
> = async (args, api, extraOptions) => {
  let result = await baseQuery(args, api, extraOptions);
  let url = "";
  if ((args as FetchArgs).url) {
    url = (args as FetchArgs).url;
  }
  if (
    result.error &&
    result.error.status === 401 &&
    !REDIRECT_TO_LOGIN_API_BLACKLIST.includes(url)
  ) {
    localStorage.removeItem(AUTHORIZATION_HEADER_STORAGE_KEY);
    api.dispatch(clearCurrentUser);
    window.location.href = LOGIN_PAGE_PATH;
  }
  return result;
};

export const commonApi = createApi({
  reducerPath: "api",
  baseQuery: baseQueryWithRedirectIfUnauthorized,
  tagTypes: [
    "Users",
    "Orders",
    "Order applications",
    "Explore orders",
    "My orders",
    "Notification",
  ],
  endpoints: (_) => ({}),
});
