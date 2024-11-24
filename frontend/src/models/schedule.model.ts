import { UserDtoModel } from "./user.model.ts";
import { Dayjs } from "dayjs";

// form
export interface AppointmentScheduleFormModel {
  fromTime: Dayjs;
  toTime: Dayjs;
}

export interface AppointmentFormModel {
  fromTime: Dayjs;
  toTime: Dayjs;
  comment: string;
}

export interface HostSelectionFormModel {
  hostId: string;
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
  message: string;
}
