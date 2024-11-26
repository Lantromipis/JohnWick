import { FC, memo, useEffect } from "react";
import { Alert, Box, CircularProgress, Stack } from "@mui/material";
import { useSelector } from "react-redux";
import { selectCurrentUserId } from "../../../store/user/user.selectors.ts";
import { emit } from "@rsql/emitter";
import builder from "@rsql/builder";
import { scheduleApi } from "../../../store/schedule/schedule.api.ts";
import MyAppointmentsListComponent from "./my-appointments-list.component.tsx";
import { enqueueSnackbar } from "notistack";

type MyAppointmentsListContainerProps = {};

const MyAppointmentsListContainer: FC<
  MyAppointmentsListContainerProps
> = () => {
  const currentUserId: string | undefined = useSelector(selectCurrentUserId);
  const {
    data: currentUserAppointments,
    refetch,
    isLoading,
    isFetching,
  } = scheduleApi.useListAppointmentsQuery({
    rsqlPredicate: currentUserId
      ? emit(builder.eq("bookedBy.id", currentUserId))
      : undefined,
  });

  const [deleteAppointment, deleteAppointmentResponse] =
    scheduleApi.useDeleteAppointmentMutation();

  const onAppointmentCancel = (scheduleId: string, appointmentId: string) => {
    deleteAppointment({
      scheduleId: scheduleId,
      appointmentId: appointmentId,
    })
      .unwrap()
      .then(() => {
        enqueueSnackbar({
          variant: "success",
          message: `Appointment successfully cancelled`,
        });
      });
  };

  useEffect(() => {
    refetch();
  }, [refetch]);

  if (isLoading) {
    return (
      <Stack
        spacing={2}
        alignItems="center"
        justifyContent="center"
        display="flex"
      >
        <CircularProgress />
      </Stack>
    );
  }

  if (!currentUserAppointments || currentUserAppointments.length === 0) {
    return (
      <Alert severity="info">
        You have no appointments. To create one, use "Plan appointment" page.
      </Alert>
    );
  }

  const isListRefreshing = isFetching || deleteAppointmentResponse.isLoading;

  return (
    <Stack sx={{ position: "relative" }}>
      <Box
        sx={() =>
          isListRefreshing ? { opacity: 0.5, pointerEvents: "none" } : {}
        }
      >
        <MyAppointmentsListComponent
          appointments={currentUserAppointments}
          onCancelAppointment={onAppointmentCancel}
        />
      </Box>
      {isListRefreshing && (
        <CircularProgress
          sx={{
            position: "absolute",
            top: "20%",
            left: "50%",
            transform: "translate(-50%, 0)",
          }}
        />
      )}
    </Stack>
  );
};

export default memo(MyAppointmentsListContainer);
