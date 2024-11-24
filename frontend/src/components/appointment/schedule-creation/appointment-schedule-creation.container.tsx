import { FC, memo, useCallback, useState } from "react";
import {
  Alert,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Stack,
} from "@mui/material";
import { SubmitHandler } from "react-hook-form";
import { APPOINTMENT_SCHEDULE_CREATION_FORM_ID } from "../../../constants/form.constants.ts";
import EditCalendarIcon from "@mui/icons-material/EditCalendar";
import AppointmentScheduleCreationForm from "./appointment-schedule-creation.form.tsx";
import { AppointmentScheduleFormModel } from "../../../models/schedule.model.ts";
import { scheduleApi } from "../../../store/schedule/schedule.api.ts";

type AppointmentScheduleCreationContainerProps = {};

const AppointmentScheduleCreationContainer: FC<
  AppointmentScheduleCreationContainerProps
> = () => {
  const [creationError, setCreationError] = useState<boolean>(false);
  const [dialogOpen, setDialogOpen] = useState<boolean>(false);
  const [createNewAppointmentSchedule, createNewAppointmentScheduleResponse] =
    scheduleApi.useCreateAppointmentScheduleMutation();

  const handleDialogOpen = () => {
    setCreationError(false);
    setDialogOpen(true);
  };

  const handleDialogClose = () => {
    setCreationError(false);
    setDialogOpen(false);
  };

  const handleSubmit: SubmitHandler<AppointmentScheduleFormModel> = useCallback(
    (formData) => {
      setCreationError(false);
      createNewAppointmentSchedule({
        startTime: formData.fromTime.toISOString(),
        endTime: formData.toTime.toISOString(),
      })
        .unwrap()
        .then(() => {
          handleDialogClose();
        })
        .catch(() => {
          setCreationError(true);
        });
    },
    [],
  );

  return (
    <>
      <Button
        variant="outlined"
        onClick={handleDialogOpen}
        startIcon={<EditCalendarIcon />}
        sx={{ width: "400px" }}
      >
        Schedule appointments for day
      </Button>
      <Dialog open={dialogOpen} onClose={handleDialogClose}>
        <DialogTitle>Create appointment schedule</DialogTitle>
        <DialogContent>
          <Stack spacing={4} sx={{ paddingTop: "10px" }}>
            {creationError && (
              <Alert severity="error" sx={{ width: "300px" }}>
                Failed to create appointment schedule. Please try again.
              </Alert>
            )}
            <AppointmentScheduleCreationForm onSubmit={handleSubmit} />
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button
            onClick={handleDialogClose}
            disabled={createNewAppointmentScheduleResponse.isLoading}
          >
            Cancel
          </Button>
          <Button
            type="submit"
            variant="contained"
            form={APPOINTMENT_SCHEDULE_CREATION_FORM_ID}
            disabled={createNewAppointmentScheduleResponse.isLoading}
          >
            Create
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
};

export default memo(AppointmentScheduleCreationContainer);
