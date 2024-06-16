import { commonApi } from "../common.api.ts";
import { UserDtoModel } from "../../models/user.model.ts";
import { USER_SELF_BASE_URL } from "../../constants/api.constants.ts";

export const currentUserApi = commonApi.injectEndpoints({
  endpoints: (builder) => ({
    getCurrentUserInfo: builder.query<UserDtoModel, void>({
      query: () => ({
        url: USER_SELF_BASE_URL,
      }),
    }),
  }),
});

export const { useGetCurrentUserInfoQuery } = currentUserApi;
