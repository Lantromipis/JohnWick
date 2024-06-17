import { FC, memo } from "react";
import MainLayout from "../layouts/main.layout.tsx";
import { Stack } from "@mui/material";
import UserCreationContainer from "../components/user/creation/user-creation.container.tsx";
import UserListContainer from "../components/user/list/user-list.container.tsx";

type ManageUsersPageProps = {};

const ManageUsersPage: FC<ManageUsersPageProps> = () => {
  return (
    <MainLayout>
      <Stack spacing={2}>
        <UserCreationContainer />
        <UserListContainer />
      </Stack>
    </MainLayout>
  );
};

export default memo(ManageUsersPage);
