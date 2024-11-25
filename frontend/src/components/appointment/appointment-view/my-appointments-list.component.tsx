import { FC, memo } from "react";
import { AppointmentDtoModel } from "../../../models/schedule.model.ts";
import {
  Button,
  Card,
  CardActions,
  CardContent,
  CardHeader,
  Stack,
  Typography,
} from "@mui/material";
import dayjs from "dayjs";

type MyAppointmentsListComponentProps = {
  onCancelAppointment: (scheduleId: string, appointmentId: string) => void;
  appointments: AppointmentDtoModel[];
};

const MyAppointmentsListComponent: FC<MyAppointmentsListComponentProps> = ({
  appointments,
  onCancelAppointment,
}) => {
  const today = dayjs();

  return (
    <Stack
      direction={"row"}
      sx={{ flexWrap: "wrap" }}
      useFlexGap
      spacing={{ xs: 1, sm: 2 }}
    >
      {appointments.map((appointment) => {
        const appointmentStart = dayjs(appointment.startTime);
        const appointmentEnd = dayjs(appointment.endTime);

        return (
          <Card variant="outlined" sx={{ minWidth: "350px", flex: "1" }}>
            <CardHeader
              title={"Appointment " + appointmentStart.format("DD.MM.YYYY")}
            />
            <CardContent>
              <Typography gutterBottom variant="h6" component="div">
                Host: {appointment.appointmentSchedule?.host.displayName}
              </Typography>
              <Typography gutterBottom variant="body2" component="div">
                Start time: {appointmentStart.format("HH:00")}
              </Typography>
              <Typography gutterBottom variant="body2" component="div">
                End time: {appointmentEnd.format("HH:00")}
              </Typography>
            </CardContent>
            {today.isBefore(appointmentStart) && (
              <CardActions>
                <Button
                  variant="outlined"
                  color="error"
                  onClick={() =>
                    onCancelAppointment(
                      appointment.appointmentSchedule?.id ?? "",
                      appointment.id,
                    )
                  }
                >
                  Cancel
                </Button>
              </CardActions>
            )}
          </Card>
        );
      })}
    </Stack>
  );
};

export default memo(MyAppointmentsListComponent);
