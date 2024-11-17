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
              <TableCell>Target</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {orders.map((order) => (
              <TableRow>
                <TableCell>{order.id}</TableCell>
                <TableCell>{order.targetName}</TableCell>
                {!order.assignee && (
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

export default memo(RegularOrderManagementListComponent);
