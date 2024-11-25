import { OrderDtoModel } from "./order.model.ts";
import { UserDtoModel } from "./user.model.ts";

export enum CleaningRequestStatus {
  CREATED = "CREATED",
  IN_PROGRESS = "IN_PROGRESS",
  COMPLETED = "COMPLETED",
}

export interface CleaningRequestDtoModel {
  id: string;
  order: OrderDtoModel;
  requestedBy: UserDtoModel;
  createdTimestamp: string;
  status: CleaningRequestStatus;
  appliedCleaner: UserDtoModel;
}
