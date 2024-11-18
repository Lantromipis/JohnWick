import { FC, memo, useState } from "react";
import { RegularOrderDto } from "../../../models/order.model.ts";
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
              <TableCell>Customer</TableCell>
              <TableCell>Target</TableCell>
              <TableCell>Status</TableCell>
              <TableCell>Creation date</TableCell>
              <TableCell>Price</TableCell>
              <TableCell>Assigned killer</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {orders.map((order) => (
              <TableRow>
                <TableCell>{order.id}</TableCell>
                <TableCell>{order.targetName}</TableCell>
                <TableCell>{order.customerName}</TableCell>
                <TableCell>
                  {order.status ? orderStatusToLabel(order.status) : ""}
                </TableCell>
                <TableCell>{order.createdTimestamp}</TableCell>
                <TableCell>{order.price}</TableCell>
                <TableCell>{order.assignee?.displayName}</TableCell>
                {!order.assignee ? (
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
