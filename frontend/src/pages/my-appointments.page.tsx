import { FC, memo } from "react";
import MainLayout from "../layouts/main.layout.tsx";
import { Stack } from "@mui/material";
import MyAppointmentsListContainer from "../components/appointment/appointment-view/my-appointments-list.container.tsx";

type MyAppointmentsPageProps = {};

const MyAppointmentsPage: FC<MyAppointmentsPageProps> = () => {
  return (
    <MainLayout>
      <Stack spacing={4}>
        <MyAppointmentsListContainer />
      </Stack>
    </MainLayout>
  );
};

export default memo(MyAppointmentsPage);
