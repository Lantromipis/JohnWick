import { FC, memo, useEffect } from "react";
import { orderApi } from "../../../store/order/order.api.ts";
import MyOrdersComponent from "./order-my-list.component.tsx";

type MyOrdersContainerProps = {};

const MyOrdersContainer: FC<MyOrdersContainerProps> = () => {
  const { data, refetch } = orderApi.useGetMyOrdersQuery();

  useEffect(() => {
    refetch();
  }, [refetch]);

  return <MyOrdersComponent orders={data ?? []} />;
};

export default memo(MyOrdersContainer);
