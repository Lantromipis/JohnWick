import { commonApi } from "../common.api.ts";
import { UserDtoModel } from "../../models/user.model.ts";
import {
  getPatchUserUrl,
  USER_BASE_URL,
  USER_SELF_BASE_URL,
} from "../../constants/api.constants.ts";

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
    getUsers: builder.query<UserDtoModel[], void>({
      query: () => ({
        url: USER_BASE_URL,
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

export const { useCreateNewUserMutation, useLazyGetUsersQuery } = userApi;
