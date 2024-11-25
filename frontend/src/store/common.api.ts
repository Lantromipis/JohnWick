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
import { clearCurrentUser } from "./user/user.slice.ts";
import { LOGIN_PAGE_PATH } from "../constants/route.constants.ts";
import { enqueueSnackbar } from "notistack";
import { ErrorResponseDtoModel } from "../models/common.model.ts";

const baseQuery = fetchBaseQuery({
  prepareHeaders: (headers) => setCommonHeaders(headers),
});

const baseQueryWithRedirectIfUnauthorized: BaseQueryFn<
  string | FetchArgs,
  unknown,
  FetchBaseQueryError
> = async (args, api, extraOptions) => {
  const result = await baseQuery(args, api, extraOptions);
  let url = "";
  if ((args as FetchArgs).url) {
    url = (args as FetchArgs).url;
  }
  if (result.error) {
    if (
      result.error.status === 401 &&
      !REDIRECT_TO_LOGIN_API_BLACKLIST.includes(url)
    ) {
      localStorage.removeItem(AUTHORIZATION_HEADER_STORAGE_KEY);
      api.dispatch(clearCurrentUser);
      window.location.href = LOGIN_PAGE_PATH;
    } else {
      const errorDto = result.error.data as ErrorResponseDtoModel;
      if (errorDto?.message) {
        enqueueSnackbar({
          variant: "error",
          message: `${errorDto.message}`,
        });
      } else {
        enqueueSnackbar({
          variant: "error",
          message: "Unexpected error from server",
        });
      }
    }
  }

  return result;
};

export const commonApi = createApi({
  reducerPath: "api",
  baseQuery: baseQueryWithRedirectIfUnauthorized,
  tagTypes: [
    "Users",
    "Orders",
    "Notification",
    "Regular orders",
    "Head hunt orders",
    "Promissory note orders",
    "Regular order applications",
    "Appointment schedules",
    "Appointments",
  ],
  endpoints: (_) => ({}),
});
