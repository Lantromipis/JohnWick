import { FC, memo } from "react";
import {
  alpha,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  useTheme,
} from "@mui/material";
import ScheduleIcon from "@mui/icons-material/Schedule";
import {
  AppointmentDtoModel,
  AppointmentScheduleDtoModel,
} from "../../../models/schedule.model.ts";
import { formatHour } from "../../../utils/appointment-utils.ts";
import dayjs, { Dayjs } from "dayjs";
import ScheduleTimeslotComponent from "./schedule-timeslot.component.tsx";
import { UserRole } from "../../../models/user.model.ts";

const FIRST_COLUMN_WIDTH = 70;

type WeeklyAppointmentsScheduleComponentProps = {
  userRole?: UserRole;
  onTimeSlotClicked?: (
    schedule: AppointmentScheduleDtoModel | undefined,
    appointment: AppointmentDtoModel | undefined,
    start: Dayjs,
    end: Dayjs,
  ) => void;
  appointmentsSchedules: AppointmentScheduleDtoModel[];
  currentWeekStart: Dayjs;
  currentWeekEnd: Dayjs;
};

const WeeklyAppointmentsScheduleComponent: FC<
  WeeklyAppointmentsScheduleComponentProps
> = ({
  userRole,
  appointmentsSchedules,
  currentWeekStart,
  onTimeSlotClicked,
}) => {
  const theme = useTheme();
  const today = dayjs();

  return (
    <>
      <TableContainer>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell
                align={"center"}
                key={`${-1}${-1}`}
                width={FIRST_COLUMN_WIDTH}
              >
                <ScheduleIcon />
              </TableCell>
              {[...Array(7)].map((_, idx) => {
                const currentDate = currentWeekStart.add(idx, "days");
                return (
                  <TableCell
                    key={`${-1}${idx}`}
                    align={"center"}
                    sx={{
                      ...(currentDate.isSame(today, "day") && {
                        backgroundColor: alpha(theme.palette.info.dark, 0.2),
                      }),
                    }}
                  >
                    <b> {currentDate.format("D, dd")}</b>
                  </TableCell>
                );
              })}
            </TableRow>
          </TableHead>
          <TableBody>
            {[...Array(24)].map((_, i) => {
              const currentRowTime = currentWeekStart.add(i, "hours");
              return (
                <TableRow key={i}>
                  <TableCell
                    key={i}
                    align={"center"}
                    width={FIRST_COLUMN_WIDTH}
                    sx={{
                      ...(currentRowTime.get("hours") ===
                        today.get("hours") && {
                        backgroundColor: alpha(theme.palette.info.dark, 0.2),
                      }),
                    }}
                  >
                    <b>{formatHour(i)}</b>
                  </TableCell>
                  {[...Array(7)].map((_, j) => (
                    <ScheduleTimeslotComponent
                      slotStartTime={currentWeekStart
                        .add(j, "days")
                        .add(i, "hours")
                        .set("minutes", 0)
                        .set("seconds", 0)}
                      todayTime={today}
                      appointmentsSchedules={appointmentsSchedules}
                      onTimeSlotClicked={onTimeSlotClicked}
                      userRole={userRole}
                    />
                  ))}
                </TableRow>
              );
            })}
          </TableBody>
        </Table>
      </TableContainer>
    </>
  );
};

export default memo(WeeklyAppointmentsScheduleComponent);
