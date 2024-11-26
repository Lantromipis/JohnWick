import { FC, memo, useCallback, useEffect, useMemo } from "react";
import { orderApi } from "../../../store/order/order.api.ts";
import RegularOrderCardComponent from "./regular-order-explore-list.component.tsx";
import { Alert, Stack } from "@mui/material";
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

  const { data: regularOrderList, refetch: refetchRegularOrderList } =
    orderApi.useListRegularOrdersQuery({
      rsqlPredicate: emit(
        builder.eq("status", OrderStatus.AWAITING_APPLICATIONS),
      ),
    });

  const {
    data: currentUserApplications,
    refetch: refetchCurrentUserApplications,
  } = orderApi.useListRegularOrderApplicationsQuery(
    emit(builder.eq("killer.id", currentUserId ?? "")),
  );
  const [createOrderApplication] =
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

  return (
    <Stack spacing={2}>
      {regularOrderList?.length === 0 && (
        <Alert severity="info">
          Sorry, currently there are no orders available. Please check later.
        </Alert>
      )}
      {regularOrderList && (
        <RegularOrderCardComponent
          regularOrders={regularOrderList}
          existingOrderApplicationsByOrderId={applicationByOrderId}
          onApplyForOrder={handleApplyForOrder}
        />
      )}
    </Stack>
  );
};

export default memo(ExploreRegularOrdersContainer);
