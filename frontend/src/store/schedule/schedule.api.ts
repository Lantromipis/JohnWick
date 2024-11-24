import { commonApi } from "../common.api.ts";
import {
  APPOINTMENT_SCHEDULE_BASE_URL,
  getCreateAppointmentUrl,
  getListEntitiesUrl,
} from "../../constants/api.constants.ts";
import {
  AppointmentDtoModel,
  AppointmentScheduleDtoModel,
} from "../../models/schedule.model.ts";
import { ListEntitiesRequest } from "../../models/common.model.ts";

export const scheduleApi = commonApi.injectEndpoints({
  endpoints: (builder) => ({
    createAppointmentSchedule: builder.mutation<
      AppointmentScheduleDtoModel,
      Omit<AppointmentScheduleDtoModel, "id" | "host" | "appointments">
    >({
      query: (appointmentSchedule) => ({
        url: APPOINTMENT_SCHEDULE_BASE_URL,
        method: "POST",
        body: { ...appointmentSchedule },
      }),
      invalidatesTags: ["Appointment schedules"],
    }),
    listAppointmentSchedule: builder.query<
      AppointmentScheduleDtoModel[],
      ListEntitiesRequest
    >({
      query: (request) => ({
        url: getListEntitiesUrl(APPOINTMENT_SCHEDULE_BASE_URL, request),
      }),
      providesTags: ["Appointment schedules"],
    }),
    createAppointment: builder.mutation<
      AppointmentDtoModel,
      {
        scheduleId: string;
        appointment: Omit<AppointmentDtoModel, "id" | "bookedBy">;
      }
    >({
      query: (request) => ({
        url: getCreateAppointmentUrl(request.scheduleId),
        method: "POST",
        body: { ...request.appointment },
      }),
      invalidatesTags: ["Appointment schedules"],
    }),
  }),
});
