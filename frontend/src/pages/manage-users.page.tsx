import { FC, memo } from "react";
import MainLayout from "../layouts/main.layout.tsx";
import { Typography } from "@mui/material";

type ManageUsersPageProps = {};

const ManageUsersPage: FC<ManageUsersPageProps> = () => {
  return (
    <MainLayout>
      <Typography>Manage users page</Typography>
    </MainLayout>
  );
};

export default memo(ManageUsersPage);
