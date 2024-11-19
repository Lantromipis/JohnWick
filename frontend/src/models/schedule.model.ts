import { UserDtoModel } from "./user.model.ts";

// form
export interface AppointmentScheduleFormModel {
  fromTime: string;
  toTime: string;
}

// dto
export interface AppointmentsScheduleDtoModel {
  id: string;
  host: UserDtoModel;
  fromTime: string;
  toTime: string;
  appointments: AppointmentDtoModel[];
}

export interface AppointmentDtoModel {
  id: string;
  bookedBy: UserDtoModel;
  fromTime: string;
  toTime: string;
  comment: string;
}
