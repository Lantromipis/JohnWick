import { commonApi } from "../common.api.ts";
import {
  UserDtoModel,
  UserWithPasswordDtoModel,
} from "../../models/user.model.ts";
import { USER_BASE_URL } from "../../constants/api.constants.ts";

export const userApi = commonApi.injectEndpoints({
  endpoints: (builder) => ({
    createNewUser: builder.mutation<UserDtoModel, UserWithPasswordDtoModel>({
      query: (user) => ({
        url: USER_BASE_URL,
        method: "POST",
        body: { ...user },
      }),
    }),
  }),
});

export const { useCreateNewUserMutation } = userApi;
