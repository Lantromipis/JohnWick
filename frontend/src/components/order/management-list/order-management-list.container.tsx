import { FC, memo, useEffect } from "react";
import OrderManagementListComponent from "./order-management-list.component.tsx";
import { orderApi } from "../../../store/order/order.api.ts";

type OrderListContainerProps = {};

const OrderManagementListContainer: FC<OrderListContainerProps> = () => {
  const { data, refetch } = orderApi.useGetOrdersQuery();

  useEffect(() => {
    refetch();
  }, [refetch]);

  return <OrderManagementListComponent orders={data ?? []} />;
};

export default memo(OrderManagementListContainer);
