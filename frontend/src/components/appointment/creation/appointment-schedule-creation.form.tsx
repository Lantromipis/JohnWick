import { FC, memo } from "react";
import { Controller, SubmitHandler, useForm } from "react-hook-form";
import { APPOINTMENT_SCHEDULE_CREATION_FORM_ID } from "../../../constants/form.constants.ts";
import { Stack } from "@mui/material";
import { AppointmentScheduleFormModel } from "../../../models/schedule.model.ts";
import { DateTimePicker, LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import dayjs, { Dayjs } from "dayjs";
import "dayjs/locale/en-gb";
import { getDayJsNextHour } from "../../../utils/appointment-utils.ts";

type AppointmentScheduleCreationProps = {
  onSubmit: SubmitHandler<AppointmentScheduleFormModel>;
};

const AppointmentScheduleCreationForm: FC<AppointmentScheduleCreationProps> = ({
  onSubmit,
}) => {
  const {
    control,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm<AppointmentScheduleFormModel>({
    mode: "onBlur",
    reValidateMode: "onBlur",
    defaultValues: {
      fromTime: getDayJsNextHour(),
      toTime: getDayJsNextHour().add(1, "hour"),
    },
  });

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      noValidate
      id={APPOINTMENT_SCHEDULE_CREATION_FORM_ID}
    >
      <Stack direction={"column"} spacing={3} sx={{ minWidth: "300px" }}>
        <Controller
          name="fromTime"
          control={control}
          rules={{
            required: "Appointment schedule start time is required!",
            validate: (value: Dayjs) => {
              if (dayjs().isAfter(value)) {
                return "Start time is before current time!";
              }
            },
          }}
          render={({ field }) => (
            <LocalizationProvider
              dateAdapter={AdapterDayjs}
              adapterLocale={"en-gb"}
            >
              <DateTimePicker
                {...field}
                disablePast
                views={["year", "month", "day", "hours", "minutes"]}
                minutesStep={60}
                label="Start time"
                value={field.value}
                slotProps={{
                  textField: {
                    error: !!errors.fromTime,
                    helperText: errors.fromTime?.message,
                  },
                }}
              />
            </LocalizationProvider>
          )}
        />
        <Controller
          name="toTime"
          control={control}
          rules={{
            required: "Appointment schedule end time is required!",
            validate: (value: Dayjs) => {
              if (watch("fromTime").isSame(value)) {
                return "End and start time must differ!";
              }
              if (watch("fromTime").isAfter(value)) {
                return "End time must be after start time!";
              }
            },
          }}
          render={({ field }) => (
            <LocalizationProvider
              dateAdapter={AdapterDayjs}
              adapterLocale={"en-gb"}
            >
              <DateTimePicker
                {...field}
                disablePast
                views={["year", "month", "day", "hours", "minutes"]}
                label="End time"
                value={field.value}
                minutesStep={60}
                slotProps={{
                  textField: {
                    error: !!errors.toTime,
                    helperText: errors.toTime?.message,
                  },
                }}
              />
            </LocalizationProvider>
          )}
        />
      </Stack>
    </form>
  );
};

export default memo(AppointmentScheduleCreationForm);
