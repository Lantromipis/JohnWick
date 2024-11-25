import { FC, memo, useCallback } from "react";
import { Dayjs } from "dayjs";
import {
  alpha,
  Button,
  Stack,
  TableCell,
  Tooltip,
  Typography,
  useTheme,
} from "@mui/material";
import {
  formatTimeslotHours,
  getIntersectedAppointmentByTime,
  getIntersectedAppointmentScheduleByTime,
} from "../../../utils/appointment-utils.ts";
import {
  AppointmentDtoModel,
  AppointmentScheduleDtoModel,
} from "../../../models/schedule.model.ts";
import AccessControlComponent from "../../menu/access-control.component.tsx";
import { UserRole } from "../../../models/user.model.ts";

type ScheduleTimeslotComponentProps = {
  userRole?: UserRole;
  todayTime: Dayjs;
  slotStartTime: Dayjs;
  appointmentsSchedules: AppointmentScheduleDtoModel[];
  onTimeSlotClicked?: (
    schedule: AppointmentScheduleDtoModel | undefined,
    appointment: AppointmentDtoModel | undefined,
    start: Dayjs,
    end: Dayjs,
  ) => void;
};

const TIMESLOT_DURATION_HOURS = 1;
const CELL_ALPHA_FACTOR = 0.3;

const ScheduleTimeslotComponent: FC<ScheduleTimeslotComponentProps> = ({
  userRole,
  todayTime,
  slotStartTime,
  appointmentsSchedules,
  onTimeSlotClicked,
}) => {
  const theme = useTheme();

  const getTimeslotSx = useCallback(
    (
      isToday: boolean,
      isCurrentTime: boolean,
      isScheduled: boolean,
      isScheduledAndFree: boolean,
    ) => {
      if (isScheduled) {
        if (isScheduledAndFree) {
          return {
            backgroundColor: alpha(
              theme.palette.success.light,
              CELL_ALPHA_FACTOR,
            ),
          };
        } else {
          return {
            backgroundColor: alpha(
              theme.palette.error.light,
              CELL_ALPHA_FACTOR,
            ),
          };
        }
      } else {
        if (isToday || isCurrentTime) {
          return {
            backgroundColor: alpha(theme.palette.info.light, CELL_ALPHA_FACTOR),
          };
        }
      }

      return {};
    },
    [theme],
  );

  const intersectedAppointmentSchedule =
    getIntersectedAppointmentScheduleByTime(
      appointmentsSchedules,
      slotStartTime,
    );
  const intersectedAppointment = getIntersectedAppointmentByTime(
    intersectedAppointmentSchedule?.appointments,
    slotStartTime,
  );
  console.log(slotStartTime, todayTime);
  const isCurrentTime = slotStartTime.get("hours") === todayTime.get("hours");
  const isToday = slotStartTime.isSame(todayTime, "day");
  const isScheduled = !!intersectedAppointmentSchedule;
  const isScheduledAndFree = isScheduled && !intersectedAppointment;
  const slotEndTime = slotStartTime.add(TIMESLOT_DURATION_HOURS, "hours");
  const timeslotIsAfterNow = slotStartTime.isAfter(todayTime.add(1, "hours"));

  return (
    <TableCell
      key={slotStartTime.toISOString()}
      sx={{
        minWidth: 100,
        ...getTimeslotSx(
          isToday,
          isCurrentTime,
          isScheduled,
          isScheduledAndFree,
        ),
      }}
    >
      <Tooltip
        componentsProps={{
          tooltip: {
            sx: {
              backgroundColor: theme.palette.warning.light,
              color: theme.palette.common.white,
            },
          },
        }}
        title={
          <Stack sx={{ margin: "10px" }} spacing={1}>
            <Typography variant="h6">
              Timeslot {formatTimeslotHours(slotStartTime, slotEndTime)}
            </Typography>
            <AccessControlComponent
              role={userRole}
              showFor={[UserRole.TAILOR, UserRole.SOMMELIER]}
            >
              <Typography>
                {isScheduled ? (
                  intersectedAppointment ? (
                    <span>
                      Timeslot is booked by{" "}
                      <b>{intersectedAppointment.bookedBy?.displayName}</b>
                    </span>
                  ) : (
                    "Timeslot is free"
                  )
                ) : (
                  "Timeslot is not scheduled for appointments"
                )}
              </Typography>
            </AccessControlComponent>
            <AccessControlComponent role={userRole} showFor={UserRole.KILLER}>
              <Typography>
                {isScheduled
                  ? intersectedAppointment
                    ? "Timeslot is already booked"
                    : timeslotIsAfterNow
                      ? "Timeslot is free and can be booked"
                      : "Timeslot is free but can not be booked anymore"
                  : "Timeslot is not scheduled for appointments and can not be booked"}
              </Typography>
              {isScheduled && !intersectedAppointment && timeslotIsAfterNow && (
                <Button
                  variant={"contained"}
                  onClick={() =>
                    onTimeSlotClicked &&
                    onTimeSlotClicked(
                      intersectedAppointmentSchedule,
                      intersectedAppointment,
                      slotStartTime,
                      slotEndTime,
                    )
                  }
                >
                  Select this timeslot
                </Button>
              )}
            </AccessControlComponent>
          </Stack>
        }
      >
        {isScheduled ? (
          <Typography variant={"body2"}>
            {intersectedAppointment ? "Booked" : "Free"}
          </Typography>
        ) : (
          <Typography variant={"body2"} sx={{ color: "transparent" }}>
            Empty
          </Typography>
        )}
      </Tooltip>
    </TableCell>
  );
};

export default memo(ScheduleTimeslotComponent);
