import { commonApi } from "../common.api.ts";
import {
  getCreateRegularOrderApplicationUrl,
  getListEntitiesUrl,
  getListRegularOrderApplicationUrl,
  getPatchOrderUrl,
  getRegularOrderUrl,
  HEAD_HUNT_ORDER_BASE_URL,
  ORDER_BASE_URL,
  PROMISSORY_NOTE_ORDER_BASE_URL,
  REGULAR_ORDER_BASE_URL,
} from "../../constants/api.constants.ts";
import {
  HeadHuntOrderDto,
  OrderDtoModel,
  PromissoryNoteOrderDto,
  RegularOrderApplicationDto,
  RegularOrderDto,
} from "../../models/order.model.ts";
import { ListEntitiesRequest } from "../../models/common.model.ts";

export const orderApi = commonApi.injectEndpoints({
  endpoints: (builder) => ({
    createNewOrder: builder.mutation<OrderDtoModel, OrderDtoModel>({
      query: (order) => ({
        url: ORDER_BASE_URL,
        method: "POST",
        body: { ...order },
      }),
      invalidatesTags: [
        "Regular orders",
        "Head hunt orders",
        "Promissory note orders",
      ],
    }),
    patchOrder: builder.mutation<OrderDtoModel, OrderDtoModel>({
      query: (order) => ({
        url: getPatchOrderUrl(order.id ?? ""),
        method: "PATCH",
        body: { ...order },
      }),
      invalidatesTags: [
        "Regular orders",
        "Head hunt orders",
        "Promissory note orders",
        "Regular order applications",
      ],
    }),
    getRegularOrder: builder.query<RegularOrderDto, string>({
      query: (orderId) => ({
        url: getRegularOrderUrl(orderId),
      }),
    }),
    listRegularOrders: builder.query<
      RegularOrderDto[],
      ListEntitiesRequest | undefined
    >({
      query: (request) => ({
        url: getListEntitiesUrl(REGULAR_ORDER_BASE_URL, request),
      }),
      providesTags: ["Regular orders"],
    }),
    listHeadHuntOrders: builder.query<
      HeadHuntOrderDto[],
      ListEntitiesRequest | undefined
    >({
      query: (request) => ({
        url: getListEntitiesUrl(HEAD_HUNT_ORDER_BASE_URL, request),
      }),
      providesTags: ["Head hunt orders"],
    }),
    listPromissoryNoteOrders: builder.query<
      PromissoryNoteOrderDto[],
      ListEntitiesRequest | undefined
    >({
      query: (request) => ({
        url: getListEntitiesUrl(PROMISSORY_NOTE_ORDER_BASE_URL, request),
      }),
      providesTags: ["Promissory note orders"],
    }),
    createRegularOrderApplication: builder.mutation<
      RegularOrderApplicationDto,
      string
    >({
      query: (orderId) => ({
        url: getCreateRegularOrderApplicationUrl(orderId),
        method: "POST",
      }),
      invalidatesTags: ["Regular orders", "Regular order applications"],
    }),
    listRegularOrderApplications: builder.query<
      RegularOrderApplicationDto[],
      string
    >({
      query: (rsqlPredicate) => ({
        url: getListRegularOrderApplicationUrl(rsqlPredicate),
      }),
      providesTags: ["Regular order applications"],
    }),
  }),
});
