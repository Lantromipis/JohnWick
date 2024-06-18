import { FC, memo } from "react";
import OrderManagementListComponent from "./order-management-list.component.tsx";
import { orderApi } from "../../../store/order/order.api.ts";

type OrderListContainerProps = {};

const OrderManagementListContainer: FC<OrderListContainerProps> = () => {
  const { data } = orderApi.useGetOrdersQuery();

  return <OrderManagementListComponent orders={data ?? []} />;
};

export default memo(OrderManagementListContainer);
