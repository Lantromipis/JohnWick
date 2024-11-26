import { FC, memo, useEffect } from "react";
import { orderApi } from "../../../store/order/order.api.ts";
import { Alert, Box, CircularProgress, Stack } from "@mui/material";
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
  const {
    data: promissoryNoteOrders,
    refetch,
    isLoading: listOrdersLoading,
    isFetching: listOrdersFetching,
  } = orderApi.useListPromissoryNoteOrdersQuery({
    rsqlPredicate: currentUserId
      ? emit(builder.eq("debtor.id", currentUserId))
      : undefined,
  });

  const [updateOrder, { isLoading: updateOrderLoading }] =
    orderApi.usePatchOrderMutation();

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

  if (listOrdersLoading) {
    return (
      <Stack
        spacing={2}
        alignItems="center"
        justifyContent="center"
        display="flex"
      >
        <CircularProgress />
      </Stack>
    );
  }

  if (!promissoryNoteOrders || promissoryNoteOrders.length === 0) {
    return (
      <Alert severity="info">
        You have no promissory note orders. Lucky you!
      </Alert>
    );
  }

  const isUpdatingList = listOrdersFetching || updateOrderLoading;

  return (
    <Stack spacing={4} sx={{ position: "relative" }}>
      <Box
        sx={() =>
          isUpdatingList ? { opacity: 0.5, pointerEvents: "none" } : {}
        }
      >
        <MyPromissoryNoteOrderListComponent
          onOrderAction={onOrderAction}
          orders={promissoryNoteOrders}
        />
      </Box>
      {isUpdatingList && (
        <CircularProgress
          sx={{
            position: "absolute",
            top: "20%",
            left: "50%",
            transform: "translate(-50%, 0)",
          }}
        />
      )}
    </Stack>
  );
};

export default memo(MyPromissoryNoteOrderListContainer);
