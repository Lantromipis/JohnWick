import React, { FC, memo, useState } from "react";
import MainLayout from "../layouts/main.layout.tsx";
import { Stack, Tab, Tabs } from "@mui/material";
import MyRegularOrderListContainer from "../components/order/my-order-list/my-regular-order-list.container.tsx";
import MyPromissoryNoteOrderListContainer from "../components/order/my-order-list/my-promissory-order-list.container.tsx";

type MyOrdersPageProps = {};

const MyOrdersPage: FC<MyOrdersPageProps> = () => {
  const [tabNum, setTabNum] = useState(0);

  const handleTabChange = (_event: React.SyntheticEvent, newValue: number) => {
    setTabNum(newValue);
  };

  return (
    <MainLayout>
      <Stack spacing={2}>
        <Tabs value={tabNum} onChange={handleTabChange} variant={"fullWidth"}>
          <Tab label="Regular orders" />
          <Tab label="Promissory note orders" />
        </Tabs>
        {tabNum === 0 && <MyRegularOrderListContainer />}
        {tabNum === 1 && <MyPromissoryNoteOrderListContainer />}
      </Stack>
    </MainLayout>
  );
};

export default memo(MyOrdersPage);
