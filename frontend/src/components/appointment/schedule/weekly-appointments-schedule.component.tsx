import { FC, memo } from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from "@mui/material";
import ScheduleIcon from "@mui/icons-material/Schedule";
import { AppointmentsScheduleDtoModel } from "../../../models/schedule.model.ts";

type WeeklyAppointmentsScheduleComponentProps = {
  appointmentsSchedules: AppointmentsScheduleDtoModel[];
};

const WeeklyAppointmentsScheduleComponent: FC<
  WeeklyAppointmentsScheduleComponentProps
> = () => {
  return (
    <>
      <TableContainer>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell align={"center"}>
                <ScheduleIcon />
              </TableCell>
              <TableCell align={"center"}>Monday</TableCell>
              <TableCell align={"center"}>Tuesday</TableCell>
              <TableCell align={"center"}>Wednesday</TableCell>
              <TableCell align={"center"}>Thursday</TableCell>
              <TableCell align={"center"}>Friday</TableCell>
              <TableCell align={"center"}>Saturday</TableCell>
              <TableCell align={"center"}>Sunday</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {[...Array(24)].map((_, i) => (
              <TableRow>
                <TableCell width={30}>
                  {(i < 10 ? "0" + i : i) + ":00"}
                </TableCell>
                <TableCell>{i}</TableCell>
                <TableCell>{i}</TableCell>
                <TableCell>{i}</TableCell>
                <TableCell>{i}</TableCell>
                <TableCell>{i}</TableCell>
                <TableCell>{i}</TableCell>
                <TableCell>{i}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </>
  );
};

export default memo(WeeklyAppointmentsScheduleComponent);
