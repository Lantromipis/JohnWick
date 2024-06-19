import { FC, memo } from "react";
import MainLayout from "../layouts/main.layout.tsx";
import { Stack } from "@mui/material";
import MyOrdersContainer from "../components/order/my-order-list/order-my-list.container.tsx";

type MyOrdersPageProps = {};

const MyOrdersPage: FC<MyOrdersPageProps> = () => {
  return (
    <MainLayout>
      <Stack>
        <MyOrdersContainer />
      </Stack>
    </MainLayout>
  );
};

export default memo(MyOrdersPage);
