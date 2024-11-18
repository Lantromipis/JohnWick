import { FC, memo } from "react";
import WeeklyAppointmentsScheduleComponent from "./weekly-appointments-schedule.component.tsx";

type AppointmentScheduleContainerProps = {};

const AppointmentScheduleContainer: FC<
  AppointmentScheduleContainerProps
> = () => {
  return <WeeklyAppointmentsScheduleComponent appointmentsSchedules={[]} />;
};

export default memo(AppointmentScheduleContainer);
