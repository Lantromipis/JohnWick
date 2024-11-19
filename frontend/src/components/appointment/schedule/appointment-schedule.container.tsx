import { FC, memo, useEffect, useState } from "react";
import WeeklyAppointmentsScheduleComponent from "./weekly-appointments-schedule.component.tsx";
import { Button, Stack, Typography } from "@mui/material";
import ArrowBackIosNewIcon from "@mui/icons-material/ArrowBackIosNew";
import ArrowForwardIosIcon from "@mui/icons-material/ArrowForwardIos";
import dayjs from "dayjs";
import { scheduleApi } from "../../../store/schedule/schedule.api.ts";
import { emit } from "@rsql/emitter";
import builder from "@rsql/builder";

type AppointmentScheduleContainerProps = {};

const AppointmentScheduleContainer: FC<
  AppointmentScheduleContainerProps
> = () => {
  const today = dayjs();
  const [currentWeekStart, setCurrentWeekStart] = useState(
    today.startOf("week"),
  );
  const [currentWeekEnd, setCurrentWeekEnd] = useState(today.endOf("week"));

  const handleChangeWeekPrevOpen = () => {
    setCurrentWeekStart(currentWeekStart.subtract(7, "days"));
    setCurrentWeekEnd(currentWeekEnd.subtract(7, "days"));
  };

  const handleChangeWeekNextOpen = () => {
    setCurrentWeekStart(currentWeekStart.add(7, "days"));
    setCurrentWeekEnd(currentWeekEnd.add(7, "days"));
  };

  const {
    data: appointmentScheduleList,
    refetch: refetchAppointmentScheduleList,
  } = scheduleApi.useListAppointmentScheduleQuery({
    rsqlPredicate: emit(
      builder.and(
        builder.ge("endTime", currentWeekStart.toISOString()),
        builder.le("startTime", currentWeekEnd.toISOString()),
      ),
    ),
  });

  useEffect(() => {
    refetchAppointmentScheduleList();
  }, [refetchAppointmentScheduleList]);

  return (
    <Stack spacing={2}>
      <Stack direction={"row"} spacing={4} alignItems="center">
        <Stack direction={"row"}>
          <Button
            onClick={handleChangeWeekPrevOpen}
            disabled={currentWeekStart.isBefore(today)}
            startIcon={<ArrowBackIosNewIcon />}
          ></Button>
          <Button
            onClick={handleChangeWeekNextOpen}
            endIcon={<ArrowForwardIosIcon />}
          ></Button>
        </Stack>
        <Typography variant={"h5"}>
          {currentWeekStart.format("D MMMM")} —{" "}
          {currentWeekEnd.format("D MMMM")}
        </Typography>
      </Stack>
      <WeeklyAppointmentsScheduleComponent
        appointmentsSchedules={appointmentScheduleList ?? []}
        currentWeekStart={currentWeekStart}
        currentWeekEnd={currentWeekEnd}
      />
    </Stack>
  );
};

export default memo(AppointmentScheduleContainer);
