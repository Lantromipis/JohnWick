import { FC, memo } from "react";
import MainLayout from "../layouts/main.layout.tsx";
import { Stack } from "@mui/material";
import AppointmentScheduleContainer from "../components/appointment/schedule/appointment-schedule.container.tsx";
import AppointmentScheduleCreationContainer from "../components/appointment/schedule-creation/appointment-schedule-creation.container.tsx";
import { useSelector } from "react-redux";
import { selectCurrentUserId } from "../store/user/user.selectors.ts";

type MySchedulePageProps = {};

const MySchedulePage: FC<MySchedulePageProps> = () => {
  const currentUserId: string | undefined = useSelector(selectCurrentUserId);

  return (
    <MainLayout>
      <Stack spacing={4}>
        <AppointmentScheduleCreationContainer />
        <AppointmentScheduleContainer
          hostId={currentUserId ? currentUserId : ""}
        />
      </Stack>
    </MainLayout>
  );
};

export default memo(MySchedulePage);
