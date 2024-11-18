import { OrderStatus, OrderType } from "../models/order.model.ts";

export function orderTypeToLabel(role: OrderType): string {
  switch (role) {
    case OrderType.REGULAR:
      return "Regular";
    case OrderType.PROMISSORY_NOTE:
      return "Promissory note";
    case OrderType.HEAD_HUNT:
      return "Head haunt";
  }
}

export function orderStatusToLabel(status: OrderStatus): string {
  switch (status) {
    case OrderStatus.CREATED:
      return "Created";
    case OrderStatus.AWAITING_APPLICATIONS:
      return "Awaiting applications";
    case OrderStatus.AWAITING_ASSIGMENT:
      return "Awaiting assignment";
    case OrderStatus.AWAITING_ASSIGNEE:
      return "Awaiting assignee";
    case OrderStatus.AWAITING_SUIT:
      return "Awaiting suit";
    case OrderStatus.AWAITING_DEGUSTATION:
      return "Awaiting degustation";
    case OrderStatus.AWAITING_SUBMISSION:
      return "Awaiting submission";
    case OrderStatus.AWAITING_CLEANING:
      return "Awaiting cleaning";
    case OrderStatus.AWAITING_APPROVAL:
      return "Awaiting approval";
    case OrderStatus.COMPLETED:
      return "Awaiting completed";
    case OrderStatus.CANCELLED:
      return "Awaiting cancelled";
  }
}
