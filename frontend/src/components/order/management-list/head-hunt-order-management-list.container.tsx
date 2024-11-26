import { FC, memo, useEffect } from "react";
import { orderApi } from "../../../store/order/order.api.ts";
import HeadHuntOrderManagementListComponent from "./head-hunt-order-management-list.component.tsx";
import { OrderStatus, OrderType } from "../../../models/order.model.ts";
import { enqueueSnackbar } from "notistack";

type HeadHuntOrderManagementListContainerProps = {};

const HeadHuntOrderManagementListContainer: FC<
  HeadHuntOrderManagementListContainerProps
> = () => {
  const { data, refetch } = orderApi.useListHeadHuntOrdersQuery({});

  const [updateOrder] = orderApi.usePatchOrderMutation();

  const onOrderCompleted = (orderId: string | undefined) => {
    updateOrder({
      id: orderId,
      type: OrderType.HEAD_HUNT,
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
    <HeadHuntOrderManagementListComponent
      onOrderCompleted={onOrderCompleted}
      orders={data ?? []}
    />
  );
};

export default memo(HeadHuntOrderManagementListContainer);
