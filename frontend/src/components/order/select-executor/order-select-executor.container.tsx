import { FC, memo } from "react";

type OrderSelectExecutorContainerProps = {
  orderId: string;
  open: boolean;
  onClose: () => void;
};

const OrderSelectExecutorContainer: FC<
  OrderSelectExecutorContainerProps
> = () => {
  return <div>OrderSelectExecutorContainer</div>;
};

export default memo(OrderSelectExecutorContainer);
