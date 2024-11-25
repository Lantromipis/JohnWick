import { FC, memo, useEffect } from "react";
import { Alert, Stack } from "@mui/material";
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
  const { data: currentUserAppointments, refetch } =
    scheduleApi.useListAppointmentsQuery({
      rsqlPredicate: currentUserId
        ? emit(builder.eq("bookedBy.id", currentUserId))
        : undefined,
    });

  const [deleteAppointment] = scheduleApi.useDeleteAppointmentMutation();

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

  return (
    <Stack spacing={2}>
      {currentUserAppointments?.length === 0 && (
        <Alert severity="info">
          You have no appointments. To create one, use "Plan appointment" page.
        </Alert>
      )}
      {currentUserAppointments && (
        <MyAppointmentsListComponent
          appointments={currentUserAppointments}
          onCancelAppointment={onAppointmentCancel}
        />
      )}
    </Stack>
  );
};

export default memo(MyAppointmentsListContainer);
