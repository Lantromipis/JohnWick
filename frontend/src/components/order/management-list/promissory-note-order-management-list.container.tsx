import { FC, memo, useEffect } from "react";
import { orderApi } from "../../../store/order/order.api.ts";
import PromissoryNoteOrderManagementListComponent from "./promissory-note-order-management-list.component.tsx";
import { OrderStatus, OrderType } from "../../../models/order.model.ts";
import { enqueueSnackbar } from "notistack";

type PromissoryNoteOrderManagementListContainerProps = {};

const PromissoryNoteOrderManagementListContainer: FC<
  PromissoryNoteOrderManagementListContainerProps
> = () => {
  const { data, refetch } = orderApi.useListPromissoryNoteOrdersQuery({});

  const [updateOrder] = orderApi.usePatchOrderMutation();

  const onOrderCompleted = (orderId: string | undefined) => {
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-expect-error
    updateOrder({
      id: orderId,
      type: OrderType.PROMISSORY_NOTE,
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
    <PromissoryNoteOrderManagementListComponent
      onOrderCompleted={onOrderCompleted}
      orders={data ?? []}
    />
  );
};

export default memo(PromissoryNoteOrderManagementListContainer);
