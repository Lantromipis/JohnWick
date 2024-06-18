import { commonApi } from "../common.api.ts";
import {
  getOrderApplicationsChooseUrl,
  getOrderApplicationsUrl,
  ORDER_BASE_URL,
} from "../../constants/api.constants.ts";
import {
  OrderApplicationDto,
  OrderDtoModel,
} from "../../models/order.model.ts";

export const orderApi = commonApi.injectEndpoints({
  endpoints: (builder) => ({
    createNewOrder: builder.mutation<OrderDtoModel, OrderDtoModel>({
      query: (order) => ({
        url: ORDER_BASE_URL,
        method: "POST",
        body: { ...order },
      }),
      invalidatesTags: ["Orders"],
    }),
    getOrders: builder.query<OrderDtoModel[], void>({
      query: () => ({
        url: ORDER_BASE_URL,
      }),
      providesTags: ["Orders"],
    }),
    getOrderApplications: builder.query<OrderApplicationDto[], string>({
      query: (orderId) => ({
        url: getOrderApplicationsUrl(orderId),
      }),
      providesTags: ["Order applications"],
    }),
    selectOrderApplication: builder.mutation<OrderDtoModel, string>({
      query: (applicationId) => ({
        url: getOrderApplicationsChooseUrl(applicationId),
        method: "PUT",
      }),
      invalidatesTags: ["Orders"],
    }),
  }),
});
