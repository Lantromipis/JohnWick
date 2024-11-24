import { FC, memo, useEffect } from "react";
import { orderApi } from "../../../store/order/order.api.ts";
import { Alert, Stack } from "@mui/material";
import HeadHauntOrderExploreCardComponent from "./head-haunt-order-explore-card.component.tsx";

type ExploreRegularOrdersContainerProps = {};

const ExploreRegularOrdersContainer: FC<
  ExploreRegularOrdersContainerProps
> = () => {
  const { data: headHauntOrderList, refetch: refetchHeadHauntOrderList } =
    orderApi.useListHeadHuntOrdersQuery({});

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
      {headHauntOrderList?.map((headHauntOrder) => (
        <HeadHauntOrderExploreCardComponent headHauntOrder={headHauntOrder} />
      ))}
    </Stack>
  );
};

export default memo(ExploreRegularOrdersContainer);
