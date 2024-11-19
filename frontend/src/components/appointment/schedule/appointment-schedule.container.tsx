import { FC, memo, useState } from "react";
import WeeklyAppointmentsScheduleComponent from "./weekly-appointments-schedule.component.tsx";
import { Button, Stack, Typography } from "@mui/material";
import ArrowBackIosNewIcon from "@mui/icons-material/ArrowBackIosNew";
import ArrowForwardIosIcon from "@mui/icons-material/ArrowForwardIos";
import {
  addDays,
  getCurrentWeekEndDay,
  getCurrentWeekStartDay,
} from "../../../utils/appointment-utils.ts";

type AppointmentScheduleContainerProps = {};

const monthsMap = [
  "January",
  "February",
  "March",
  "April",
  "May",
  "June",
  "July",
  "August",
  "September",
  "October",
  "November",
  "December",
];

const AppointmentScheduleContainer: FC<
  AppointmentScheduleContainerProps
> = () => {
  const [currentWeekStart, setCurrentWeekStart] = useState(
    getCurrentWeekStartDay(),
  );
  const [currentWeekEnd, setCurrentWeekEnd] = useState(getCurrentWeekEndDay());

  const handleChangeWeekPrevOpen = () => {
    setCurrentWeekStart(addDays(currentWeekStart, -7));
    setCurrentWeekEnd(addDays(currentWeekEnd, -7));
  };

  const handleChangeWeekNextOpen = () => {
    setCurrentWeekStart(addDays(currentWeekStart, 7));
    setCurrentWeekEnd(addDays(currentWeekEnd, 7));
  };

  return (
    <Stack spacing={2}>
      <Stack direction={"row"} spacing={4} alignItems="center">
        <Stack direction={"row"}>
          <Button
            onClick={handleChangeWeekPrevOpen}
            startIcon={<ArrowBackIosNewIcon />}
          ></Button>
          <Button
            onClick={handleChangeWeekNextOpen}
            endIcon={<ArrowForwardIosIcon />}
          ></Button>
        </Stack>
        <Typography variant={"h5"}>
          {currentWeekStart.getDate()} {monthsMap[currentWeekStart.getMonth()]}{" "}
          — {currentWeekEnd.getDate()} {monthsMap[currentWeekEnd.getMonth()]}
        </Typography>
      </Stack>
      <WeeklyAppointmentsScheduleComponent
        appointmentsSchedules={[]}
        currentWeekStart={currentWeekStart}
        currentWeekEnd={currentWeekEnd}
      />
    </Stack>
  );
};

export default memo(AppointmentScheduleContainer);
