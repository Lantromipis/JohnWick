import { commonApi } from "../common.api.ts";
import { UserDtoModel } from "../../models/user.model.ts";
import {
  getListEntitiesUrl,
  getPatchUserUrl,
  USER_BASE_URL,
  USER_SELF_BASE_URL,
} from "../../constants/api.constants.ts";
import { ListEntitiesRequest } from "../../models/common.model.ts";

export const userApi = commonApi.injectEndpoints({
  endpoints: (builder) => ({
    createNewUser: builder.mutation<UserDtoModel, UserDtoModel>({
      query: (user) => ({
        url: USER_BASE_URL,
        method: "POST",
        body: { ...user },
      }),
      invalidatesTags: ["Users"],
    }),
    listUsers: builder.query<UserDtoModel[], ListEntitiesRequest>({
      query: (request) => ({
        url: getListEntitiesUrl(USER_BASE_URL, request),
      }),
      providesTags: ["Users"],
    }),
    patchUser: builder.mutation<UserDtoModel, UserDtoModel>({
      query: (user) => ({
        url: getPatchUserUrl(user?.id),
        method: "PATCH",
        body: { ...user },
      }),
    }),
    getCurrentUserInfo: builder.query<UserDtoModel, void>({
      query: () => ({
        url: USER_SELF_BASE_URL,
      }),
    }),
  }),
});

export const { useCreateNewUserMutation } = userApi;
