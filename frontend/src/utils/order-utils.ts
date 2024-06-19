import { OrderType } from "../models/order.model.ts";

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
