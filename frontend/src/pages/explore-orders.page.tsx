import { FC, memo } from "react";
import MainLayout from "../layouts/main.layout.tsx";
import { Stack } from "@mui/material";
import OrderExploreListContainer from "../components/order/explore-list/order-explore-list.container.tsx";

type ExploreOrdersPageProps = {};

const ExploreOrdersPage: FC<ExploreOrdersPageProps> = () => {
  return (
    <MainLayout>
      <Stack>
        <OrderExploreListContainer />
      </Stack>
    </MainLayout>
  );
};

export default memo(ExploreOrdersPage);
