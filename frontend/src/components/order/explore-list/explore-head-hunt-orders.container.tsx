import { FC, memo, useEffect } from "react";
import { orderApi } from "../../../store/order/order.api.ts";
import { Alert, Stack } from "@mui/material";
import HeadHauntOrderExploreCardComponent from "./head-haunt-order-explore-list.component.tsx";
import { OrderStatus, OrderType } from "../../../models/order.model.ts";
import { enqueueSnackbar } from "notistack";
import { emit } from "@rsql/emitter";
import builder from "@rsql/builder";

type ExploreRegularOrdersContainerProps = {};

const ExploreRegularOrdersContainer: FC<
  ExploreRegularOrdersContainerProps
> = () => {
  const { data: headHauntOrderList, refetch: refetchHeadHauntOrderList } =
    orderApi.useListHeadHuntOrdersQuery({
      rsqlPredicate: emit(builder.eq("status", "AWAITING_SUBMISSION")),
    });

  const [updateOrder] = orderApi.usePatchOrderMutation();

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

  return (
    <Stack spacing={2}>
      {headHauntOrderList?.length === 0 && (
        <Alert severity="info">
          Sorry, currently there are no orders available. Please check later.
        </Alert>
      )}
      {headHauntOrderList && (
        <HeadHauntOrderExploreCardComponent
          headHauntOrders={headHauntOrderList}
          onTargetEliminated={onTargetEliminated}
        />
      )}
    </Stack>
  );
};

export default memo(ExploreRegularOrdersContainer);
