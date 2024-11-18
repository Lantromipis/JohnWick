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
import { UserCreateFormModel } from "../../../models/user.model.ts";
import { userApi } from "../../../store/user/user.api.ts";
import UserCreationForm from "./appointment-schedule-creation.form.tsx";
import { APPOINTMENT_SCHEDULE_CREATION_FORM_ID } from "../../../constants/form.constants.ts";
import EditCalendarIcon from "@mui/icons-material/EditCalendar";

type AppointmentScheduleCreationContainerProps = {};

const AppointmentScheduleCreationContainer: FC<
  AppointmentScheduleCreationContainerProps
> = () => {
  const [creationError, setCreationError] = useState<boolean>(false);
  const [dialogOpen, setDialogOpen] = useState<boolean>(false);
  const [createNewUser, createNewUserResponse] =
    userApi.useCreateNewUserMutation();

  const handleDialogOpen = () => {
    setCreationError(false);
    setDialogOpen(true);
  };

  const handleDialogClose = () => {
    setCreationError(false);
    setDialogOpen(false);
  };

  const handleSubmit: SubmitHandler<UserCreateFormModel> = useCallback(
    (formData) => {
      setCreationError(false);
      createNewUser({
        displayName: formData.displayName,
        username: formData.username,
        password: formData.password,
        role: formData.role,
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
        <DialogTitle>Create new user</DialogTitle>
        <DialogContent>
          <Stack spacing={2} sx={{ paddingTop: "10px" }}>
            {creationError && (
              <Alert severity="error">
                Failed to create user. Please try again.
              </Alert>
            )}
            <UserCreationForm onSubmit={handleSubmit} />
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button
            onClick={handleDialogClose}
            disabled={createNewUserResponse.isLoading}
          >
            Cancel
          </Button>
          <Button
            type="submit"
            variant="contained"
            form={APPOINTMENT_SCHEDULE_CREATION_FORM_ID}
            disabled={createNewUserResponse.isLoading}
          >
            Create
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
};

export default memo(AppointmentScheduleCreationContainer);
