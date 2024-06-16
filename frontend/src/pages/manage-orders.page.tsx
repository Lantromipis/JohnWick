import { FC, memo } from "react";
import MainLayout from "../layouts/main.layout.tsx";
import { Typography } from "@mui/material";

type ManageOrdersPageProps = {};

const ManageOrdersPage: FC<ManageOrdersPageProps> = () => {
  return (
    <MainLayout>
      <Typography>Manage orders page</Typography>
    </MainLayout>
  );
};

export default memo(ManageOrdersPage);
