import { commonApi } from "../common.api.ts";
import {
  UserChangePasswordDtoModel,
  UserDtoModel,
  UserWithPasswordDtoModel,
} from "../../models/user.model.ts";
import {
  getChangeUserPasswordUrl,
  USER_BASE_URL,
} from "../../constants/api.constants.ts";

export const userApi = commonApi.injectEndpoints({
  endpoints: (builder) => ({
    createNewUser: builder.mutation<UserDtoModel, UserWithPasswordDtoModel>({
      query: (user) => ({
        url: USER_BASE_URL,
        method: "POST",
        body: { ...user },
      }),
      invalidatesTags: ["Users"],
      /*      async onQueryStarted(__, { dispatch, queryFulfilled }) {
                    try {
                      const { data: createdUser } = await queryFulfilled;
                      dispatch(userApi.util.updateQueryData('getUsers', undefined, (users)=>{
                        users.push(createdUser);
                      }));
                    } catch {}
                  },*/
    }),
    getUsers: builder.query<UserDtoModel[], void>({
      query: () => ({
        url: USER_BASE_URL,
      }),
      providesTags: ["Users"],
    }),
    changeUserPassword: builder.mutation<
      UserDtoModel,
      UserChangePasswordDtoModel
    >({
      query: (dto) => ({
        url: getChangeUserPasswordUrl(dto.username),
        method: "PUT",
        body: { password: dto.password },
      }),
    }),
  }),
});

export const { useCreateNewUserMutation, useLazyGetUsersQuery } = userApi;
