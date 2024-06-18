import { UserDtoModel } from "./user.model.ts";

export enum OrderType {
  REGULAR = "DEFAULT",
  PROMISSORY_NOTE = "BILL",
  HEAD_HUNT = "HEAD_HUNT",
}

// form
export interface OrderCreationFormModel {
  type: OrderType;
  customer: string;
  price: number;
  description: string;
  target: string;
  // promissory note order
  debtorUsername: string;
  beneficiaryUsername: string;
}

export interface OrderSelectExecutorFormModel {
  executorUsername: string;
}

// dto

export type OrderDtoModel =
  | RegularOrderDto
  | HeadHuntOrderDto
  | PromissoryNoteOrderDto;

export interface BaseOrderDto {
  id?: string;
  type: OrderType;
  customer: string;
  price: number;
  description: string;
  target: string;
  assignee?: UserDtoModel;
}

export interface RegularOrderDto extends BaseOrderDto {}

export interface HeadHuntOrderDto extends BaseOrderDto {}

export interface PromissoryNoteOrderDto extends BaseOrderDto {
  beneficiary: UserDtoModel;
}
