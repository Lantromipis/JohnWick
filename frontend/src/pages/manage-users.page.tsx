import { FC, memo } from "react";
import MainLayout from "../layouts/main.layout.tsx";
import { Typography } from "@mui/material";
import UserCreationContainer from "../components/user/user-creation.container.tsx";

type ManageUsersPageProps = {};

const ManageUsersPage: FC<ManageUsersPageProps> = () => {
  return (
    <MainLayout>
      <UserCreationContainer />
      <Typography>Manage users page</Typography>
    </MainLayout>
  );
};

export default memo(ManageUsersPage);
