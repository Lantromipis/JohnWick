import React, { FC, memo, useCallback, useState } from "react";
import { SubmitHandler } from "react-hook-form";
import {
  AppointmentDtoModel,
  AppointmentScheduleDtoModel,
  HostSelectionFormModel,
} from "../../../models/schedule.model.ts";
import {
  Alert,
  Box,
  Button,
  Stack,
  Step,
  StepLabel,
  Stepper,
} from "@mui/material";
import HostSelectionContainer from "./host-selection/host-selection.container.tsx";
import { HOST_SELECTION_FORM_ID } from "../../../constants/form.constants.ts";
import AppointmentScheduleContainer from "../schedule/appointment-schedule.container.tsx";
import { Dayjs } from "dayjs";
import { formatTimeslotDayAndHours } from "../../../utils/appointment-utils.ts";
import { scheduleApi } from "../../../store/schedule/schedule.api.ts";

type PlanAppointmentStepperContainerProps = {};

const STEPS_COUNT = 3;

const PlanAppointmentStepperContainer: FC<
  PlanAppointmentStepperContainerProps
> = () => {
  const [activeStep, setActiveStep] = useState(0);
  const [selectedHost, setSelectedHost] = useState("");
  const [selectedTimeslotStart, setSelectedTimeslotStart] = useState<
    Dayjs | undefined
  >(undefined);
  const [selectedTimeslotEnd, setSelectedTimeslotEnd] = useState<
    Dayjs | undefined
  >(undefined);
  const [selectedScheduleId, setSelectedScheduleId] = useState<
    string | undefined
  >(undefined);
  const [createNewAppointment, createNewAppointmentResponse] =
    scheduleApi.useCreateAppointmentMutation();

  const handleNext = () => {
    setActiveStep((prevActiveStep) => prevActiveStep + 1);
  };

  const handleBack = () => {
    setActiveStep((prevActiveStep) => prevActiveStep - 1);
    setSelectedTimeslotStart(undefined);
    setSelectedTimeslotEnd(undefined);
    setSelectedScheduleId(undefined);
  };

  const handleReset = () => {
    setActiveStep(0);
    setSelectedTimeslotStart(undefined);
    setSelectedTimeslotEnd(undefined);
    setSelectedScheduleId(undefined);
  };

  const handleSubmitHostSelection: SubmitHandler<HostSelectionFormModel> =
    useCallback((formData) => {
      handleNext();
      setSelectedHost(formData.hostId);
    }, []);

  const onTimeSlotClicked: (
    schedule: AppointmentScheduleDtoModel | undefined,
    appointment: AppointmentDtoModel | undefined,
    start: Dayjs,
    end: Dayjs,
  ) => void = useCallback((schedule, _appointment, start, end) => {
    setSelectedTimeslotStart(start);
    setSelectedTimeslotEnd(end);
    if (schedule && schedule.id) {
      setSelectedScheduleId(schedule.id);
    }
  }, []);

  const onScheduleAppointmentClicked: () => void = useCallback(() => {
    const startISO = selectedTimeslotStart?.toISOString();
    const endISO = selectedTimeslotEnd?.toISOString();
    createNewAppointment({
      scheduleId: selectedScheduleId ? selectedScheduleId : "123",
      appointment: {
        startTime: startISO ? startISO : "",
        endTime: endISO ? endISO : "",
        message: "",
      },
    })
      .unwrap()
      .then(() => {
        handleNext();
      });
  }, [
    createNewAppointment,
    selectedScheduleId,
    selectedTimeslotEnd,
    selectedTimeslotStart,
  ]);

  return (
    <Stack sx={{ width: "100%" }} spacing={2}>
      <Stepper activeStep={activeStep}>
        <Step>
          <StepLabel>Select host for appointment</StepLabel>
        </Step>
        <Step>
          <StepLabel>Select time for appointment</StepLabel>
        </Step>
        <Step>
          <StepLabel>Review</StepLabel>
        </Step>
      </Stepper>
      {activeStep === STEPS_COUNT - 1 ? (
        <React.Fragment>
          <Alert sx={{ mt: 2, mb: 1 }} severity={"success"}>
            You successfully created appointment{"  "}
            {selectedTimeslotStart &&
              selectedTimeslotEnd &&
              formatTimeslotDayAndHours(
                selectedTimeslotStart,
                selectedTimeslotEnd,
              )}
            . Please, do not be late.
          </Alert>
          <Box
            display="flex"
            justifyContent="center"
            minHeight="100vh"
            alignItems="start"
          >
            <Button variant="contained" onClick={handleReset}>
              Create new appointment
            </Button>
          </Box>
          <Box sx={{ display: "flex", flexDirection: "row", pt: 2 }}>
            <Box sx={{ flex: "1 1 auto" }} />
            <Button onClick={handleReset}>Reset</Button>
          </Box>
        </React.Fragment>
      ) : (
        <React.Fragment>
          <Box sx={{ display: "flex", flexDirection: "row", pt: 2 }}>
            {activeStep == 1 && (
              <Button variant="outlined" onClick={handleBack} sx={{ mr: 1 }}>
                Back to host selection
              </Button>
            )}
            <Box sx={{ flex: "1 1 auto" }} />
            {activeStep == 0 && (
              <Button
                variant="contained"
                type={"submit"}
                form={HOST_SELECTION_FORM_ID}
              >
                To timeslot selection
              </Button>
            )}
            {activeStep == 1 && (
              <Button
                disabled={
                  !selectedScheduleId || createNewAppointmentResponse.isLoading
                }
                variant="contained"
                type={"submit"}
                onClick={onScheduleAppointmentClicked}
              >
                Schedule appointment
              </Button>
            )}
          </Box>
          <Box display="flex" justifyContent="center" minHeight="100vh">
            {activeStep === 0 && (
              <HostSelectionContainer onSubmit={handleSubmitHostSelection} />
            )}
            {activeStep === 1 && (
              <Stack spacing={2}>
                {createNewAppointmentResponse.error && (
                  <Alert severity="error">
                    Failed to create appointment. Please retry.
                  </Alert>
                )}
                {!selectedTimeslotEnd || !selectedTimeslotStart ? (
                  <Alert severity={"info"}>
                    To select time, hover on timeslot you like in the schedule
                    below
                  </Alert>
                ) : (
                  <Alert severity={"info"}>
                    You selected timeslot{" "}
                    {formatTimeslotDayAndHours(
                      selectedTimeslotStart,
                      selectedTimeslotEnd,
                    )}
                  </Alert>
                )}
                <Alert severity={"warning"}>
                  You can book timeslot only if it starts after 1 hour from now.
                </Alert>

                <AppointmentScheduleContainer
                  onTimeSlotClicked={onTimeSlotClicked}
                  hostId={selectedHost}
                />
              </Stack>
            )}
          </Box>
        </React.Fragment>
      )}
    </Stack>
  );
};

export default memo(PlanAppointmentStepperContainer);
