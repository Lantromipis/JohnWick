import { FC, memo } from "react";
import { OrderDtoModel, OrderType } from "../../../models/order.model.ts";
import OrderExploreListComponent from "./order-explore-lsit.component.tsx";

type OrderExploreListContainerProps = {};

const OrderExploreListContainer: FC<OrderExploreListContainerProps> = () => {
  const data: OrderDtoModel[] = [
    {
      id: "123",
      type: OrderType.REGULAR,
      customer: "Ivan ivanovich",
      description: "Yes yes yes skibidi dabidi dop dop",
      price: 1000,
      target: "Sikibi toilet",
    },
    {
      id: "345",
      type: OrderType.PROMISSORY_NOTE,
      customer: "Petr  petrov",
      description:
        "sdfjadsnkjfnaskjd fsadfn sjdknfkjsad sdnfjskadn fsdajkfn sdakjfnasdjknf sjadnf sdkjafn skjdanf sjkadnf jksadfn jksadnf sad",
      price: 1234,
      target: "Abobus",
    },
  ];

  return <OrderExploreListComponent orders={data ?? []} />;
};

export default memo(OrderExploreListContainer);
