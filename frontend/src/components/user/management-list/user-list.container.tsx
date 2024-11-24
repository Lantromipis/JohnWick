import { FC, memo, useEffect } from "react";
import UserListComponent from "./user-list.component.tsx";
import { userApi } from "../../../store/user/user.api.ts";

type UserListContainerProps = {};

const UserListContainer: FC<UserListContainerProps> = () => {
  const { data, refetch } = userApi.useListUsersQuery({});

  useEffect(() => {
    refetch();
  }, [refetch]);

  return <UserListComponent users={data ?? []} />;
};

export default memo(UserListContainer);
