// dto
import { UserDtoModel } from "./user.model.ts";

export interface AppointmentsScheduleDtoModel {
  id: string;
  host: UserDtoModel;
  date: string;
  fromTime: string;
  toTime: string;
  appointments: AppointmentDtoModel[];
}

export interface AppointmentDtoModel {
  id: string;
  bookedBy: UserDtoModel;
  date: string;
  fromTime: string;
  toTime: string;
  comment: string;
}
