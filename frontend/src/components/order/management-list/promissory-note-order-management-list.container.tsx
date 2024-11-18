import { FC, memo, useEffect } from "react";
import { orderApi } from "../../../store/order/order.api.ts";
import PromissoryNoteOrderManagementListComponent from "./promissory-note-order-management-list.component.tsx";

type PromissoryNoteOrderManagementListContainerProps = {};

const PromissoryNoteOrderManagementListContainer: FC<
  PromissoryNoteOrderManagementListContainerProps
> = () => {
  const { data, refetch } = orderApi.useListPromissoryNoteOrdersQuery({});

  useEffect(() => {
    refetch();
  }, [refetch]);

  return <PromissoryNoteOrderManagementListComponent orders={data ?? []} />;
};

export default memo(PromissoryNoteOrderManagementListContainer);
