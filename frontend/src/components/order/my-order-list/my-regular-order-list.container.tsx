import { FC, memo, useEffect } from "react";
import { orderApi } from "../../../store/order/order.api.ts";
import { Alert, Stack } from "@mui/material";
import MyRegularOrderCardComponent from "./my-regular-order-card.component.tsx";
import { useSelector } from "react-redux";
import { selectCurrentUserId } from "../../../store/user/user.selectors.ts";
import { emit } from "@rsql/emitter";
import builder from "@rsql/builder";

type MyRegularOrderListContainerProps = {};

const MyRegularOrderListContainer: FC<
  MyRegularOrderListContainerProps
> = () => {
  const currentUserId: string | undefined = useSelector(selectCurrentUserId);
  const { data: regularOrders, refetch } = orderApi.useListRegularOrdersQuery({
    rsqlPredicate: currentUserId
      ? emit(builder.eq("assignee.id", currentUserId))
      : undefined,
  });

  useEffect(() => {
    refetch();
  }, [refetch]);

  return (
    <Stack spacing={2}>
      {regularOrders?.length == 0 && (
        <Alert severity="info">
          You have no active orders. Apply for a new one using "Explore orders"
          page!
        </Alert>
      )}
      {regularOrders?.map((order) => (
        <MyRegularOrderCardComponent order={order} />
      ))}
    </Stack>
  );
};

export default memo(MyRegularOrderListContainer);
