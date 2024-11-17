import { UserDtoModel } from "./user.model.ts";

export enum OrderType {
  REGULAR = "REGULAR",
  PROMISSORY_NOTE = "PROMISSORY_NOTE",
  HEAD_HUNT = "HEAD_HUNT",
}

export enum OrderStatus {
  CREATED = "CREATED",
  AWAITING_APPLICATIONS = "AWAITING_APPLICATIONS",
  AWAITING_ASSIGMENT = "AWAITING_ASSIGMENT",
  AWAITING_ASSIGNEE = "AWAITING_ASSIGNEE",
  AWAITING_SUIT = "AWAITING_SUIT",
  AWAITING_DEGUSTATION = "AWAITING_DEGUSTATION",
  AWAITING_SUBMISSION = "AWAITING_SUBMISSION",
  AWAITING_CLEANING = "AWAITING_CLEANING",
  AWAITING_APPROVAL = "AWAITING_APPROVAL",
  COMPLETED = "COMPLETED",
  CANCELLED = "CANCELLED",
}

// form
export interface OrderCreationFormModel {
  type: OrderType;
  customer: string;
  price: number;
  description: string;
  target: string;
  // promissory note order
  debtorId: string;
  beneficiaryId: string;
}

export interface OrderSelectExecutorFormModel {
  selectedKillerId: string;
}

// dto
export type OrderDtoModel =
  | ({ type: OrderType.REGULAR } & RegularOrderDto)
  | ({ type: OrderType.PROMISSORY_NOTE } & PromissoryNoteOrderDto)
  | ({ type: OrderType.HEAD_HUNT } & HeadHuntOrderDto);

export interface BaseOrderDto {
  id?: string;
  createdTimestamp?: string;
  type: OrderType;
  description?: string;
  status?: OrderStatus;
  targetName?: string;
  applications?: RegularOrderApplicationDto[];
}

export interface RegularOrderDto extends BaseOrderDto {
  type: OrderType.REGULAR;
  assignee?: UserDtoModel;
  price?: number;
  customerName?: string;
}

export interface HeadHuntOrderDto extends BaseOrderDto {
  type: OrderType.HEAD_HUNT;
  succeededKiller?: UserDtoModel;
  currentPrice?: number;
  customerName?: string;
}

export interface PromissoryNoteOrderDto extends BaseOrderDto {
  type: OrderType.PROMISSORY_NOTE;
  beneficiary: UserDtoModel;
  debtor: UserDtoModel;
}

export interface RegularOrderApplicationDto {
  id: string;
  killer: UserDtoModel;
  regularOrder: OrderDtoModel;
  createdTimestamp: string;
}
