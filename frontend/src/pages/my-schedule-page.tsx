import { FC, memo } from "react";
import MainLayout from "../layouts/main.layout.tsx";
import { Stack } from "@mui/material";
import AppointmentScheduleContainer from "../components/appointment/schedule/appointment-schedule.container.tsx";
import AppointmentScheduleCreationContainer from "../components/appointment/creation/appointment-schedule-creation.container.tsx";

type MySchedulePageProps = {};

const MySchedulePage: FC<MySchedulePageProps> = () => {
  return (
    <MainLayout>
      <Stack spacing={4}>
        <AppointmentScheduleCreationContainer />
        <AppointmentScheduleContainer />
      </Stack>
    </MainLayout>
  );
};

export default memo(MySchedulePage);
