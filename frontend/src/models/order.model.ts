import { UserDtoModel } from "./user.model.ts";

export enum OrderType {
  REGULAR = "REGULAR",
  PROMISSORY_NOTE = "PROMISSORY_NOTE",
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
  selectedApplicationId: string;
}

// dto

export type OrderDtoModel =
  | RegularOrderDto
  | HeadHuntOrderDto
  | PromissoryNoteOrderDto;

export interface AvailableOrderDtoModel {
  id?: string;
  type: OrderType;
  customer: string;
  price: number;
  description: string;
  target: string;
  alreadyApplied: boolean;
}

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

export interface OrderApplicationDto {
  id: string;
  appliedKiller: UserDtoModel;
  order: OrderDtoModel;
}
