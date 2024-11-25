import { commonApi } from "../common.api.ts";
import {
  CLEANING_REQUEST_BASE_URL,
  getListEntitiesUrl,
} from "../../constants/api.constants.ts";
import { ListEntitiesRequest } from "../../models/common.model.ts";
import {
  CleaningRequestDtoModel,
  CleaningRequestStatus,
} from "../../models/cleaning.model.ts";

export const cleaningApi = commonApi.injectEndpoints({
  endpoints: (builder) => ({
    listCleanings: builder.query<
      CleaningRequestDtoModel[],
      ListEntitiesRequest
    >({
      query: (request) => ({
        url: getListEntitiesUrl(CLEANING_REQUEST_BASE_URL, request),
      }),
      providesTags: ["Cleanings"],
    }),
    updateCleaning: builder.mutation<
      CleaningRequestDtoModel,
      {
        id: string;
        status: CleaningRequestStatus;
      }
    >({
      query: (request) => ({
        url: `${CLEANING_REQUEST_BASE_URL}/${request.id}`,
        method: "PATCH",
        body: { ...request },
      }),
      invalidatesTags: ["Cleanings"],
    }),
  }),
});
