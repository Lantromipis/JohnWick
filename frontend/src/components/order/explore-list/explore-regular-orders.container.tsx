import { FC, memo, useCallback, useEffect, useMemo } from "react";
import { orderApi } from "../../../store/order/order.api.ts";
import RegularOrderCardComponent from "./regular-order-explore-list.component.tsx";
import { Alert, Box, CircularProgress, Stack } from "@mui/material";
import { useSelector } from "react-redux";
import { selectCurrentUserId } from "../../../store/user/user.selectors.ts";
import builder from "@rsql/builder";
import { emit } from "@rsql/emitter";
import {
  OrderStatus,
  RegularOrderApplicationDto,
} from "../../../models/order.model.ts";

type ExploreRegularOrdersContainerProps = {};

const ExploreRegularOrdersContainer: FC<
  ExploreRegularOrdersContainerProps
> = () => {
  const currentUserId: string | undefined = useSelector(selectCurrentUserId);

  const {
    data: regularOrderList,
    refetch: refetchRegularOrderList,
    isLoading: isLoadingRegularOrderList,
    isFetching: isFetchingRegularOrdersList,
  } = orderApi.useListRegularOrdersQuery({
    rsqlPredicate: emit(
      builder.eq("status", OrderStatus.AWAITING_APPLICATIONS),
    ),
  });

  const {
    data: currentUserApplications,
    refetch: refetchCurrentUserApplications,
    isLoading: isLoadingCurrentUserApplications,
    isFetching: isFetchingCurrentUserApplications,
  } = orderApi.useListRegularOrderApplicationsQuery(
    emit(builder.eq("killer.id", currentUserId ?? "")),
  );
  const [createOrderApplication, createOrderApplicationResponse] =
    orderApi.useCreateRegularOrderApplicationMutation();

  useEffect(() => {
    refetchRegularOrderList();
    refetchCurrentUserApplications();
  }, [refetchRegularOrderList, refetchCurrentUserApplications]);

  const handleApplyForOrder: (orderId: string) => void = useCallback(
    (orderId) => {
      createOrderApplication(orderId)
        .unwrap()
        .then(() => {})
        .catch(() => {});
    },
    [createOrderApplication],
  );

  const applicationByOrderId: Record<string, RegularOrderApplicationDto> =
    useMemo(() => {
      const applicationByOrderIdMap: Record<
        string,
        RegularOrderApplicationDto
      > = {};
      currentUserApplications?.forEach((application) => {
        if (application.regularOrder.id) {
          applicationByOrderIdMap[application.regularOrder.id] = application;
        }
      });
      return applicationByOrderIdMap;
    }, [currentUserApplications]);

  if (isLoadingRegularOrderList || isLoadingCurrentUserApplications) {
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

  if (!regularOrderList || regularOrderList.length === 0) {
    return (
      <Alert severity="info">
        Sorry, there are no orders. Please check later.
      </Alert>
    );
  }

  const isListRefreshing =
    isFetchingRegularOrdersList ||
    isFetchingCurrentUserApplications ||
    createOrderApplicationResponse.isLoading;

  return (
    <Stack sx={{ position: "relative" }}>
      <Box
        sx={() =>
          isListRefreshing ? { opacity: 0.5, pointerEvents: "none" } : {}
        }
      >
        <RegularOrderCardComponent
          regularOrders={regularOrderList}
          existingOrderApplicationsByOrderId={applicationByOrderId}
          onApplyForOrder={handleApplyForOrder}
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
