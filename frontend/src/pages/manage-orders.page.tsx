import { FC, memo } from "react";
import MainLayout from "../layouts/main.layout.tsx";
import { Stack } from "@mui/material";
import OrderCreationContainer from "../components/order/creation/order-creation.container.tsx";
import OrderListContainer from "../components/order/management-list/order-management-list.container.tsx";

type ManageOrdersPageProps = {};

const ManageOrdersPage: FC<ManageOrdersPageProps> = () => {
  return (
    <MainLayout>
      <Stack spacing={2}>
        <OrderCreationContainer />
        <OrderListContainer />
      </Stack>
    </MainLayout>
  );
};

export default memo(ManageOrdersPage);
