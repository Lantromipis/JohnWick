import { FC, memo, useEffect } from "react";
import { orderApi } from "../../../store/order/order.api.ts";
import RegularOrderManagementListComponent from "./regular-order-management-list.component.tsx";

type OrderListContainerProps = {};

const RegularOrderManagementListContainer: FC<OrderListContainerProps> = () => {
  const { data, refetch } = orderApi.useListRegularOrdersQuery();

  useEffect(() => {
    refetch();
  }, [refetch]);

  return <RegularOrderManagementListComponent orders={data ?? []} />;
};

export default memo(RegularOrderManagementListContainer);
