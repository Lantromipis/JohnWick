import { FC, memo, useEffect, useState } from "react";
import WeeklyAppointmentsScheduleComponent from "./weekly-appointments-schedule.component.tsx";
import { Button, Stack, Typography } from "@mui/material";
import ArrowBackIosNewIcon from "@mui/icons-material/ArrowBackIosNew";
import ArrowForwardIosIcon from "@mui/icons-material/ArrowForwardIos";
import dayjs, { Dayjs } from "dayjs";
import { scheduleApi } from "../../../store/schedule/schedule.api.ts";
import { emit } from "@rsql/emitter";
import builder from "@rsql/builder";
import {
  AppointmentDtoModel,
  AppointmentScheduleDtoModel,
} from "../../../models/schedule.model.ts";
import { UserRole } from "../../../models/user.model.ts";
import { useSelector } from "react-redux";
import { selectCurrentUserRole } from "../../../store/user/user.selectors.ts";

type AppointmentScheduleContainerProps = {
  hostId: string;
  onTimeSlotClicked?: (
    schedule: AppointmentScheduleDtoModel | undefined,
    appointment: AppointmentDtoModel | undefined,
    start: Dayjs,
    end: Dayjs,
  ) => void;
};

function buildRsqlPredicate(start: Dayjs, end: Dayjs, hostId: string) {
  return emit(
    builder.and(
      builder.ge("startTime", start.toISOString()),
      builder.le("endTime", end.toISOString()),
      builder.eq("host.id", hostId),
    ),
  );
}

const AppointmentScheduleContainer: FC<AppointmentScheduleContainerProps> = ({
  onTimeSlotClicked,
  hostId,
}) => {
  const today = dayjs();
  const [currentWeekStart, setCurrentWeekStart] = useState(
    today.startOf("week"),
  );
  const currentUserRole: UserRole | undefined = useSelector(
    selectCurrentUserRole,
  );
  const [currentWeekEnd, setCurrentWeekEnd] = useState(today.endOf("week"));

  const [listAppoitnemntSchedule, listAppoitnemntScheduleResponse] =
    scheduleApi.useLazyListAppointmentScheduleQuery();

  const handleChangeWeekPrevOpen = () => {
    const newWeekStart = currentWeekStart.subtract(7, "days");
    const newWeekEnd = currentWeekEnd.subtract(7, "days");

    listAppoitnemntSchedule({
      rsqlPredicate: buildRsqlPredicate(newWeekStart, newWeekEnd, hostId),
    })
      .unwrap()
      .then(() => {
        setCurrentWeekStart(newWeekStart);
        setCurrentWeekEnd(newWeekEnd);
      });
  };

  const handleChangeWeekNextOpen = () => {
    const newWeekStart = currentWeekStart.add(7, "days");
    const newWeekEnd = currentWeekEnd.add(7, "days");

    listAppoitnemntSchedule({
      rsqlPredicate: buildRsqlPredicate(newWeekStart, newWeekEnd, hostId),
    })
      .unwrap()
      .then(() => {
        setCurrentWeekStart(newWeekStart);
        setCurrentWeekEnd(newWeekEnd);
      });
  };

  useEffect(() => {
    listAppoitnemntSchedule({
      rsqlPredicate: buildRsqlPredicate(
        currentWeekStart,
        currentWeekEnd,
        hostId,
      ),
    });
  }, []);

  return (
    <Stack spacing={2}>
      <Stack direction={"row"} spacing={4} alignItems="center">
        <Stack direction={"row"}>
          <Button
            onClick={handleChangeWeekPrevOpen}
            disabled={
              currentWeekStart.isBefore(today) ||
              listAppoitnemntScheduleResponse.isFetching
            }
            startIcon={<ArrowBackIosNewIcon />}
          />
          <Button
            onClick={handleChangeWeekNextOpen}
            disabled={listAppoitnemntScheduleResponse.isFetching}
            endIcon={<ArrowForwardIosIcon />}
          />
        </Stack>
        <Typography variant={"h5"}>
          {currentWeekStart.format("D MMMM")} —{" "}
          {currentWeekEnd.format("D MMMM")}
        </Typography>
      </Stack>
      <WeeklyAppointmentsScheduleComponent
        appointmentsSchedules={listAppoitnemntScheduleResponse.data ?? []}
        currentWeekStart={currentWeekStart}
        currentWeekEnd={currentWeekEnd}
        onTimeSlotClicked={onTimeSlotClicked}
        userRole={currentUserRole}
        isLoading={listAppoitnemntScheduleResponse.isFetching}
      />
    </Stack>
  );
};

export default memo(AppointmentScheduleContainer);
