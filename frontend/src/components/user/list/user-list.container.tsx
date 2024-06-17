import { FC, memo } from "react";
import UserListComponent from "./user-list.component.tsx";
import { userApi } from "../../../store/user/user.api.ts";

type UserListContainerProps = {};

const UserListContainer: FC<UserListContainerProps> = () => {
  const { data } = userApi.useGetUsersQuery();

  return <UserListComponent users={data ?? []} />;
};

export default memo(UserListContainer);
