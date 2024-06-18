import { commonApi } from "../common.api.ts";
import { ORDER_BASE_URL } from "../../constants/api.constants.ts";
import { OrderDtoModel } from "../../models/order.model.ts";

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
  }),
});
