import { FC, memo, useEffect } from "react";
import { orderApi } from "../../../store/order/order.api.ts";
import PromissoryNoteOrderManagementListComponent from "./promissory-note-order-management-list.component.tsx";
import { OrderStatus, OrderType } from "../../../models/order.model.ts";
import { enqueueSnackbar } from "notistack";
import { Alert, Box, CircularProgress, Stack } from "@mui/material";

type PromissoryNoteOrderManagementListContainerProps = {};

const PromissoryNoteOrderManagementListContainer: FC<
  PromissoryNoteOrderManagementListContainerProps
> = () => {
  const { data, refetch, isLoading, isFetching } =
    orderApi.useListPromissoryNoteOrdersQuery({});

  const [updateOrder, updateOrderResponse] = orderApi.usePatchOrderMutation();

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

  if (isLoading) {
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

  if (!data || data.length === 0) {
    return (
      <Alert severity="info">
        There are no orders. Please create a new one.
      </Alert>
    );
  }

  const isListRefreshing = isFetching || updateOrderResponse.isLoading;

  return (
    <Stack sx={{ position: "relative" }}>
      <Box
        sx={() =>
          isListRefreshing ? { opacity: 0.5, pointerEvents: "none" } : {}
        }
      >
        <PromissoryNoteOrderManagementListComponent
          onOrderCompleted={onOrderCompleted}
          orders={data ?? []}
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

export default memo(PromissoryNoteOrderManagementListContainer);
