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
import { AppointmentsScheduleDtoModel } from "../../../models/schedule.model.ts";
import {
  addDays,
  formatHour,
  isSameDay,
} from "../../../utils/appointment-utils.ts";

const daysMap = [
  "Monday",
  "Tuesday",
  "Wednesday",
  "Thursday",
  "Friday",
  "Saturday",
  "Sunday",
];

type WeeklyAppointmentsScheduleComponentProps = {
  appointmentsSchedules: AppointmentsScheduleDtoModel[];
  currentWeekStart: Date;
  currentWeekEnd: Date;
};

const WeeklyAppointmentsScheduleComponent: FC<
  WeeklyAppointmentsScheduleComponentProps
> = ({ currentWeekStart }) => {
  const theme = useTheme();
  const today = new Date();

  return (
    <>
      <TableContainer>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell align={"center"}>
                <ScheduleIcon />
              </TableCell>
              {daysMap.map((value, idx) => {
                const currentDate = addDays(currentWeekStart, idx);
                return (
                  <TableCell
                    align={"center"}
                    sx={{
                      ...(isSameDay(currentDate, today) && {
                        backgroundColor: alpha(theme.palette.info.dark, 0.2),
                      }),
                    }}
                  >
                    <b> {currentDate.getDate() + ", " + value}</b>
                  </TableCell>
                );
              })}
            </TableRow>
          </TableHead>
          <TableBody>
            {[...Array(24)].map((_, i) => (
              <TableRow key={i}>
                <TableCell width={30}>
                  <b>{formatHour(i)}</b>
                </TableCell>
                {[...Array(7)].map((_, j) => (
                  <TableCell
                    sx={{
                      ...(isSameDay(addDays(currentWeekStart, j), today) && {
                        backgroundColor: alpha(theme.palette.info.light, 0.2),
                      }),
                    }}
                  >
                    {j}
                  </TableCell>
                ))}
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </>
  );
};

export default memo(WeeklyAppointmentsScheduleComponent);
