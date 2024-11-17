import { memo, FC, useEffect } from "react";
import { orderApi } from "../../../store/order/order.api.ts";
import HeadHuntOrderManagementListComponent from "./head-hunt-order-management-list.component.tsx";

type HeadHuntOrderManagementListContainerProps = {};

const HeadHuntOrderManagementListContainer: FC<
  HeadHuntOrderManagementListContainerProps
> = () => {
  const { data, refetch } = orderApi.useListHeadHuntOrdersQuery();

  useEffect(() => {
    refetch();
  }, [refetch]);

  return <HeadHuntOrderManagementListComponent orders={data ?? []} />;
};

export default memo(HeadHuntOrderManagementListContainer);
