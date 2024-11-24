import React, { FC, memo, useState } from "react";
import MainLayout from "../layouts/main.layout.tsx";
import { Stack, Tab, Tabs } from "@mui/material";
import ExploreRegularOrdersContainer from "../components/order/explore-list/explore-regular-orders.container.tsx";
import ExploreHeadHuntOrdersContainer from "../components/order/explore-list/explore-head-hunt-orders.container.tsx";

type ExploreOrdersPageProps = {};

const ExploreOrdersPage: FC<ExploreOrdersPageProps> = () => {
  const [tabNum, setTabNum] = useState(0);

  const handleTabChange = (_event: React.SyntheticEvent, newValue: number) => {
    setTabNum(newValue);
  };

  return (
    <MainLayout>
      <Stack spacing={2}>
        <Tabs value={tabNum} onChange={handleTabChange} variant={"fullWidth"}>
          <Tab label="Regular orders" />
          <Tab label="Head hunt orders" />
        </Tabs>
        {tabNum === 0 && <ExploreRegularOrdersContainer />}
        {tabNum === 1 && <ExploreHeadHuntOrdersContainer />}
      </Stack>
    </MainLayout>
  );
};

export default memo(ExploreOrdersPage);
