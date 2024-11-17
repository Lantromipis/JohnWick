import { FC, memo, useEffect } from "react";
import { orderApi } from "../../../store/order/order.api.ts";
import { Alert, Stack } from "@mui/material";
import MyPromissoryNoteOrderCardComponent from "./my-promissory-note-order-card.component.tsx";
import { useSelector } from "react-redux";
import { selectCurrentUserId } from "../../../store/user/user.selectors.ts";
import { emit } from "@rsql/emitter";
import builder from "@rsql/builder";

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

  useEffect(() => {
    refetch();
  }, [refetch]);

  return (
    <Stack spacing={2}>
      {promissoryNoteOrders?.length == 0 && (
        <Alert severity="info">
          You have no promissory note orders. Lucky you!
        </Alert>
      )}
      {promissoryNoteOrders?.map((order) => (
        <MyPromissoryNoteOrderCardComponent order={order} />
      ))}
    </Stack>
  );
};

export default memo(MyPromissoryNoteOrderListContainer);
