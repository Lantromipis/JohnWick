import { UserDtoModel } from "./user.model.ts";
import { Dayjs } from "dayjs";

// form
export interface AppointmentScheduleFormModel {
  fromTime: Dayjs;
  toTime: Dayjs;
}

// dto
export interface AppointmentScheduleDtoModel {
  id: string;
  host: UserDtoModel;
  startTime: string;
  endTime: string;
  appointments: AppointmentDtoModel[];
}

export interface AppointmentDtoModel {
  id: string;
  bookedBy: UserDtoModel;
  startTime: string;
  endTime: string;
  comment: string;
}
