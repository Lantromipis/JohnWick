import { FC, memo } from "react";
import MainLayout from "../layouts/main.layout.tsx";
import { Stack } from "@mui/material";
import PlanAppointmentStepperContainer from "../components/appointment/appointment-creation/plan-appointment-steper.container.tsx";

type PlanAppointmentPageProps = {};

const PlanAppointmentPage: FC<PlanAppointmentPageProps> = () => {
  return (
    <MainLayout>
      <Stack spacing={4}>
        <PlanAppointmentStepperContainer />
      </Stack>
    </MainLayout>
  );
};

export default memo(PlanAppointmentPage);
