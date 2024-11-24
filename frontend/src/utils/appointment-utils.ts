import dayjs, { Dayjs } from "dayjs";
import {
  AppointmentDtoModel,
  AppointmentScheduleDtoModel,
} from "../models/schedule.model.ts";

export function formatHour(hour: number): string {
  return (hour < 10 ? "0" + hour : hour) + ":00";
}

export function getDayJsNextHour(): Dayjs {
  let ret = dayjs();
  ret = ret.hour(ret.hour() + 1);
  ret = ret.minute(0);
  ret = ret.second(0);
  ret = ret.millisecond(0);
  return ret;
}

export function getIntersectedAppointmentScheduleByTime(
  schedule: AppointmentScheduleDtoModel[] | undefined,
  time: Dayjs,
): AppointmentScheduleDtoModel | undefined {
  return schedule?.find((schedule) => {
    const scheduleStartTime = dayjs(schedule.startTime);
    const scheduleEndTime = dayjs(schedule.endTime);
    return isTimeInInterval(scheduleStartTime, scheduleEndTime, time);
  });
}

export function getIntersectedAppointmentByTime(
  appointments: AppointmentDtoModel[] | undefined,
  time: Dayjs,
): AppointmentDtoModel | undefined {
  return appointments?.find((appointment) => {
    const appointmentStartTime = dayjs(appointment.startTime);
    const appointmentEndTime = dayjs(appointment.endTime);
    return (
      appointmentStartTime.isSame(time) &&
      appointmentEndTime.isSame(appointmentEndTime)
    );
  });
}

export function isTimeInInterval(
  start: Dayjs,
  end: Dayjs,
  time: Dayjs,
): boolean {
  return (
    (start.isBefore(time) || start.isSame(time)) &&
    (time.isSame(end) || time.isBefore(end))
  );
}

export function formatTimeslotHours(start: Dayjs, end: Dayjs): string {
  return `${start.format("HH:00")} - ${end.format("HH:00")}`;
}

export function formatTimeslotDayAndHours(start: Dayjs, end: Dayjs): string {
  return `${start.format("D.MM.YYYY HH:00")} - ${end.format("HH:00")}`;
}
