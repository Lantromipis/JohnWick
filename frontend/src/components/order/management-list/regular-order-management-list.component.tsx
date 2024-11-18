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
  orders: RegularOrderDto[];
};

const RegularOrderManagementListComponent: FC<
  RegularOrderManagementListComponentProps
> = ({ orders }) => {
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
              <TableCell>Id</TableCell>
              <TableCell>Status</TableCell>
              <TableCell>Customer</TableCell>
              <TableCell>Target</TableCell>
              <TableCell>Creation time</TableCell>
              <TableCell>Price</TableCell>
              <TableCell>Assigned killer</TableCell>
              <TableCell>Actions</TableCell>
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
                {!order.assignee &&
                (order.status === OrderStatus.AWAITING_APPLICATIONS ||
                  order.status === OrderStatus.AWAITING_ASSIGMENT) ? (
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
                ) : (
                  <TableCell> </TableCell>
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

export default memo(RegularOrderManagementListComponent);
