import { FC, memo } from "react";
import {
  alpha,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Theme,
  useTheme,
} from "@mui/material";
import ScheduleIcon from "@mui/icons-material/Schedule";
import { AppointmentScheduleDtoModel } from "../../../models/schedule.model.ts";
import {
  formatHour,
  getIntersectedAppointmentByTime,
  getIntersectedAppointmentScheduleByTime,
} from "../../../utils/appointment-utils.ts";
import dayjs, { Dayjs } from "dayjs";
import { SxProps } from "@mui/system";

type WeeklyAppointmentsScheduleComponentProps = {
  appointmentsSchedules: AppointmentScheduleDtoModel[];
  currentWeekStart: Dayjs;
  currentWeekEnd: Dayjs;
};

const CELL_ALPHA_FACTOR = 0.3;

export function getTableCellSxProps(
  isToday: boolean,
  isScheduled: boolean,
  isScheduledAndFree: boolean,
  theme: Theme,
): SxProps<Theme> {
  if (isToday && !isScheduled) {
    return {
      backgroundColor: alpha(theme.palette.info.light, CELL_ALPHA_FACTOR),
    };
  }

  if (isScheduled) {
    if (isScheduledAndFree) {
      return {
        backgroundColor: alpha(theme.palette.success.light, CELL_ALPHA_FACTOR),
      };
    } else {
      return {
        backgroundColor: alpha(theme.palette.error.light, CELL_ALPHA_FACTOR),
      };
    }
  }

  return {};
}

const WeeklyAppointmentsScheduleComponent: FC<
  WeeklyAppointmentsScheduleComponentProps
> = ({ appointmentsSchedules, currentWeekStart }) => {
  const theme = useTheme();
  const today = dayjs();

  return (
    <>
      <TableContainer>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell align={"center"} key={`${-1}${-1}`}>
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
              return (
                <TableRow key={i}>
                  <TableCell width={30} key={i}>
                    <b>{formatHour(i)}</b>
                  </TableCell>
                  {[...Array(7)].map((_, j) => {
                    const currentTime = currentWeekStart
                      .add(j, "days")
                      .add(i, "hours");
                    const intersectedAppointmentSchedule =
                      getIntersectedAppointmentScheduleByTime(
                        appointmentsSchedules,
                        currentTime,
                      );
                    const intersectedAppointment =
                      getIntersectedAppointmentByTime(
                        intersectedAppointmentSchedule?.appointments,
                        currentTime,
                      );
                    const isToday = currentWeekStart
                      .add(j, "days")
                      .isSame(today, "day");
                    const isScheduled = !!intersectedAppointmentSchedule;
                    const isScheduledAndFree =
                      isScheduled && !intersectedAppointment;
                    return (
                      <TableCell
                        key={`${i}${j}`}
                        sx={{
                          ...getTableCellSxProps(
                            isToday,
                            isScheduled,
                            isScheduledAndFree,
                            theme,
                          ),
                        }}
                      >
                        {intersectedAppointment
                          ? intersectedAppointment.bookedBy.displayName
                          : ""}
                      </TableCell>
                    );
                  })}
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
