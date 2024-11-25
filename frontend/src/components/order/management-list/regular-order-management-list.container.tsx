import { FC, memo, useEffect } from "react";
import { orderApi } from "../../../store/order/order.api.ts";
import RegularOrderManagementListComponent from "./regular-order-management-list.component.tsx";
import { OrderStatus, OrderType } from "../../../models/order.model.ts";
import { enqueueSnackbar } from "notistack";

type OrderListContainerProps = {};

const RegularOrderManagementListContainer: FC<OrderListContainerProps> = () => {
  const { data, refetch } = orderApi.useListRegularOrdersQuery({});

  const [updateOrder] = orderApi.usePatchOrderMutation();

  const onOrderCompleted = (orderId: string | undefined) => {
    updateOrder({
      id: orderId,
      type: OrderType.REGULAR,
      status: OrderStatus.COMPLETED,
    })
      .unwrap()
      .then(() => {
        enqueueSnackbar({
          variant: "success",
          message: `You marked order as completed`,
        });
      });
  };

  useEffect(() => {
    refetch();
  }, [refetch]);

  return (
    <RegularOrderManagementListComponent
      onOrderCompleted={onOrderCompleted}
      orders={data ?? []}
    />
  );
};

export default memo(RegularOrderManagementListContainer);
