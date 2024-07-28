import { commonApi } from "../common.api.ts";
import { NOTIFICATION_BASE_URL } from "../../constants/api.constants.ts";
import { NotificationDto } from "../../models/notification.model.ts";

export const notificationApi = commonApi.injectEndpoints({
  endpoints: (builder) => ({
    getNotifications: builder.query<NotificationDto[], void>({
      query: () => ({
        url: NOTIFICATION_BASE_URL,
      }),
      providesTags: ["Notification"],
    }),
  }),
});
