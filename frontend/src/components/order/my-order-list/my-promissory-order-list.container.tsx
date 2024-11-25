import { FC, memo, useEffect } from "react";
import { orderApi } from "../../../store/order/order.api.ts";
import { Alert, Stack } from "@mui/material";
import MyPromissoryNoteOrderListComponent from "./my-promissory-note-order-list.component.tsx";
import { useSelector } from "react-redux";
import { selectCurrentUserId } from "../../../store/user/user.selectors.ts";
import { emit } from "@rsql/emitter";
import builder from "@rsql/builder";
import { enqueueSnackbar } from "notistack";
import { OrderStatus, OrderType } from "../../../models/order.model.ts";

type MyPromissoryNoteOrderListContainerProps = {};

const MyPromissoryNoteOrderListContainer: FC<
  MyPromissoryNoteOrderListContainerProps
> = () => {
  const currentUserId: string | undefined = useSelector(selectCurrentUserId);
  const { data: promissoryNoteOrders, refetch } =
    orderApi.useListPromissoryNoteOrdersQuery({
      rsqlPredicate: currentUserId
        ? emit(builder.eq("debtor.id", currentUserId))
        : undefined,
    });

  const [updateOrder] = orderApi.usePatchOrderMutation();

  useEffect(() => {
    refetch();
  }, [refetch]);

  const onOrderAction = (
    orderId: string | undefined,
    newStatus: OrderStatus,
  ) => {
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-expect-error
    updateOrder({
      id: orderId,
      type: OrderType.PROMISSORY_NOTE,
      status: newStatus,
    })
      .unwrap()
      .then(() => {
        enqueueSnackbar({
          variant: "success",
          message: `You changed order status`,
        });
      });
  };

  return (
    <Stack spacing={4}>
      {promissoryNoteOrders?.length === 0 && (
        <Alert severity="info">
          You have no promissory note orders. Lucky you!
        </Alert>
      )}
      {promissoryNoteOrders && (
        <MyPromissoryNoteOrderListComponent
          onOrderAction={onOrderAction}
          orders={promissoryNoteOrders}
        />
      )}
    </Stack>
  );
};

export default memo(MyPromissoryNoteOrderListContainer);
