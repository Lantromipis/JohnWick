import { FC, memo, useEffect } from "react";
import { orderApi } from "../../../store/order/order.api.ts";
import { Alert, Box, CircularProgress, Stack } from "@mui/material";
import HeadHauntOrderExploreCardComponent from "./head-haunt-order-explore-list.component.tsx";
import { OrderStatus, OrderType } from "../../../models/order.model.ts";
import { enqueueSnackbar } from "notistack";
import { emit } from "@rsql/emitter";
import builder from "@rsql/builder";

type ExploreRegularOrdersContainerProps = {};

const ExploreRegularOrdersContainer: FC<
  ExploreRegularOrdersContainerProps
> = () => {
  const {
    data: headHauntOrderList,
    refetch: refetchHeadHauntOrderList,
    isFetching: isFetchingHeadHuntOrders,
    isLoading: isLoadingHeadHuntOrders,
  } = orderApi.useListHeadHuntOrdersQuery({
    rsqlPredicate: emit(builder.eq("status", "AWAITING_SUBMISSION")),
  });

  const [updateOrder, updateOrderResponse] = orderApi.usePatchOrderMutation();

  const onTargetEliminated = (orderId: string) => {
    updateOrder({
      id: orderId,
      type: OrderType.HEAD_HUNT,
      status: OrderStatus.AWAITING_CLEANING,
    })
      .unwrap()
      .then(() => {
        enqueueSnackbar({
          variant: "success",
          message: `You successfully submitted order for cleaning`,
        });
      });
  };

  useEffect(() => {
    refetchHeadHauntOrderList();
  }, [refetchHeadHauntOrderList]);

  if (isLoadingHeadHuntOrders) {
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

  if (!headHauntOrderList || headHauntOrderList.length === 0) {
    return (
      <Alert severity="info">
        Sorry, there are no orders. Please check later.
      </Alert>
    );
  }

  const isListRefreshing =
    isFetchingHeadHuntOrders || updateOrderResponse.isLoading;

  return (
    <Stack sx={{ position: "relative" }}>
      <Box
        sx={() =>
          isListRefreshing ? { opacity: 0.5, pointerEvents: "none" } : {}
        }
      >
        <HeadHauntOrderExploreCardComponent
          headHauntOrders={headHauntOrderList}
          onTargetEliminated={onTargetEliminated}
        />
      </Box>
      {isListRefreshing && (
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

export default memo(ExploreRegularOrdersContainer);
