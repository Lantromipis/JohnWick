import React, { FC, memo, useState } from "react";
import MainLayout from "../layouts/main.layout.tsx";
import { Stack, Tab, Tabs } from "@mui/material";
import RegularOrderManagementListContainer from "../components/order/management-list/regular-order-management-list.container.tsx";
import HeadHuntOrderManagementListContainer from "../components/order/management-list/head-hunt-order-management-list.container.tsx";
import PromissoryNoteOrderManagementListContainer from "../components/order/management-list/promissory-note-order-management-list.container.tsx";
import OrderCreationContainer from "../components/order/creation/order-creation.container.tsx";

type ManageOrdersPageProps = {};

const ManageOrdersPage: FC<ManageOrdersPageProps> = () => {
  const [tabNum, setTabNum] = useState(0);

  const handleTabChange = (_event: React.SyntheticEvent, newValue: number) => {
    setTabNum(newValue);
  };

  return (
    <MainLayout>
      <Stack spacing={2}>
        <OrderCreationContainer />
        <Tabs value={tabNum} onChange={handleTabChange} variant={"fullWidth"}>
          <Tab label="Regular orders" />
          <Tab label="Head hunt orders" />
          <Tab label="Promissory note orders" />
        </Tabs>
        {tabNum === 0 && <RegularOrderManagementListContainer />}
        {tabNum === 1 && <HeadHuntOrderManagementListContainer />}
        {tabNum === 2 && <PromissoryNoteOrderManagementListContainer />}
      </Stack>
    </MainLayout>
  );
};

export default memo(ManageOrdersPage);
