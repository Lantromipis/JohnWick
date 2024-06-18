import { FC, memo } from "react";
import { OrderDtoModel } from "../../../models/order.model.ts";

type OrderExploreListComponentProps = {
  orders: OrderDtoModel[];
};

const OrderExploreListComponent: FC<OrderExploreListComponentProps> = () => {
  return <div>OrderExploreListComponent</div>;
};

export default memo(OrderExploreListComponent);
