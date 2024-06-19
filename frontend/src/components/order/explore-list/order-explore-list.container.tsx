import { FC, memo, useCallback, useEffect } from "react";
import OrderExploreListComponent from "./order-explore-lsit.component.tsx";
import { orderApi } from "../../../store/order/order.api.ts";

type OrderExploreListContainerProps = {};

const OrderExploreListContainer: FC<OrderExploreListContainerProps> = () => {
  const { data, refetch } = orderApi.useGetExploreOrdersQuery();
  const [applyForOrder] = orderApi.useApplyForOrderMutation();

  useEffect(() => {
    refetch();
  }, [refetch]);

  const handleApplyForOrder: (orderId: string) => void = useCallback(
    (orderId) => {
      applyForOrder(orderId)
        .unwrap()
        .then(() => {})
        .catch(() => {});
    },
    [],
  );

  return (
    <OrderExploreListComponent
      orders={data ?? []}
      onApplyForOrder={handleApplyForOrder}
    />
  );
};

export default memo(OrderExploreListContainer);
