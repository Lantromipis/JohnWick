import { FC, memo, useState } from "react";
import { OrderDtoModel, OrderType } from "../../../models/order.model.ts";
import {
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from "@mui/material";
import { orderTypeToLabel } from "../../../utils/order-utils.ts";
import OrderSelectExecutorContainer from "../select-executor/order-select-executor.container.tsx";

type OrderListComponentProps = {
  orders: OrderDtoModel[];
};

const OrderManagementListComponent: FC<OrderListComponentProps> = ({
  orders,
}) => {
  const [currentOrderId, setCurrentOrderId] = useState<string>("");
  const [selectExecutorDialogOpen, setSelectExecutorDialogOpen] =
    useState<boolean>(false);

  const handleSelectExecutorDialogOpen = () => {
    setSelectExecutorDialogOpen(true);
  };

  const handleSelectExecutorDialogClose = () => {
    setSelectExecutorDialogOpen(false);
  };

  return (
    <>
      <TableContainer>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Order type</TableCell>
              <TableCell>Customer</TableCell>
              <TableCell>Target</TableCell>
              <TableCell></TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {orders.map((order) => (
              <TableRow>
                <TableCell>{orderTypeToLabel(order.type)}</TableCell>
                <TableCell>{order.customer}</TableCell>
                <TableCell>{order.target}</TableCell>
                {order.type == OrderType.REGULAR && !order.assignee && (
                  <TableCell>
                    <Button
                      variant="outlined"
                      onClick={() => {
                        setCurrentOrderId(order.id ?? "");
                        handleSelectExecutorDialogOpen();
                      }}
                    >
                      Select executor
                    </Button>
                  </TableCell>
                )}
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
      <OrderSelectExecutorContainer
        orderId={currentOrderId}
        onClose={handleSelectExecutorDialogClose}
        open={selectExecutorDialogOpen}
      />
    </>
  );
};

export default memo(OrderManagementListComponent);
