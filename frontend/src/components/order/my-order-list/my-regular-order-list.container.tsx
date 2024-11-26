import { FC, memo, useEffect } from "react";
import { orderApi } from "../../../store/order/order.api.ts";
import { Alert, Box, CircularProgress, Stack } from "@mui/material";
import { useSelector } from "react-redux";
import { selectCurrentUserId } from "../../../store/user/user.selectors.ts";
import { emit } from "@rsql/emitter";
import builder from "@rsql/builder";
import MyRegularOrderListComponent from "./my-regular-order-list.component.tsx";
import { OrderStatus, OrderType } from "../../../models/order.model.ts";
import { enqueueSnackbar } from "notistack";

type MyRegularOrderListContainerProps = {};

const MyRegularOrderListContainer: FC<
  MyRegularOrderListContainerProps
> = () => {
  const currentUserId: string | undefined = useSelector(selectCurrentUserId);
  const {
    data: regularOrders,
    refetch,
    isLoading: listOrdersLoading,
    isFetching: listOrdersFetching,
  } = orderApi.useListRegularOrdersQuery({
    rsqlPredicate: currentUserId
      ? emit(builder.eq("assignee.id", currentUserId))
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
    updateOrder({
      id: orderId,
      type: OrderType.REGULAR,
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

  if (!regularOrders || regularOrders?.length === 0) {
    return (
      <Alert severity="info">
        You have no active orders. Apply for a new one using "Explore orders"
        page!
      </Alert>
    );
  }

  const isUpdatingList = listOrdersFetching || updateOrderLoading;

  return (
    <Stack spacing={2} sx={{ position: "relative" }}>
      <Box
        sx={() =>
          isUpdatingList ? { opacity: 0.5, pointerEvents: "none" } : {}
        }
      >
        <MyRegularOrderListComponent
          onOrderAction={onOrderAction}
          orders={regularOrders}
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

export default memo(MyRegularOrderListContainer);
