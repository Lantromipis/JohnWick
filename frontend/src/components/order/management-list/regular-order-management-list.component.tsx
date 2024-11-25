import { FC, memo, useState } from "react";
import { OrderStatus, RegularOrderDto } from "../../../models/order.model.ts";
import {
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from "@mui/material";
import OrderSelectExecutorContainer from "../select-executor/order-select-executor.container.tsx";
import { orderStatusToLabel } from "../../../utils/order-utils.ts";
import { isoStringToPrettyDateTime } from "../../../utils/time-utils.ts";

type RegularOrderManagementListComponentProps = {
  onOrderCompleted: (orderId: string) => void;
  orders: RegularOrderDto[];
};

const RegularOrderManagementListComponent: FC<
  RegularOrderManagementListComponentProps
> = ({ onOrderCompleted, orders }) => {
  const [currentOrderId, setCurrentOrderId] = useState<string>("");
  const [selectExecutorDialogOpen, setSelectExecutorDialogOpen] =
    useState<boolean>(false);

  const handleSelectExecutorDialogOpen = () => {
    setSelectExecutorDialogOpen(true);
  };

  const handleSelectExecutorDialogClose = () => {
    setSelectExecutorDialogOpen(false);
    setCurrentOrderId("");
  };

  return (
    <>
      <TableContainer>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell align={"center"}>Id</TableCell>
              <TableCell align={"center"}>Status</TableCell>
              <TableCell align={"center"}>Customer</TableCell>
              <TableCell align={"center"}>Target</TableCell>
              <TableCell align={"center"}>Creation time</TableCell>
              <TableCell align={"center"}>Price</TableCell>
              <TableCell align={"center"}>Assigned killer</TableCell>
              <TableCell align={"center"}>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {orders.map((order) => (
              <TableRow key={order.id}>
                <TableCell>{order.id}</TableCell>
                <TableCell>
                  <b>{order.status ? orderStatusToLabel(order.status) : ""}</b>
                </TableCell>
                <TableCell>{order.targetName}</TableCell>
                <TableCell>{order.customerName}</TableCell>
                <TableCell>
                  {isoStringToPrettyDateTime(order.createdTimestamp)}
                </TableCell>
                <TableCell>{order.price}</TableCell>
                <TableCell>{order.assignee?.displayName}</TableCell>
                <TableCell>
                  {!order.assignee &&
                    (order.status === OrderStatus.AWAITING_APPLICATIONS ||
                      order.status === OrderStatus.AWAITING_ASSIGMENT) && (
                      <Button
                        variant="outlined"
                        onClick={() => {
                          setCurrentOrderId(order.id ?? "");
                          handleSelectExecutorDialogOpen();
                        }}
                      >
                        Select executor
                      </Button>
                    )}
                  {order.status === OrderStatus.AWAITING_APPROVAL && (
                    <Button
                      variant="outlined"
                      onClick={() => {
                        onOrderCompleted(order.id ?? "");
                      }}
                    >
                      Complete order
                    </Button>
                  )}
                </TableCell>
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

export default memo(RegularOrderManagementListComponent);
