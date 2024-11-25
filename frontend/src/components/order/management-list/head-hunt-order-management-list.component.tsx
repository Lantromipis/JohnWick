import { FC, memo } from "react";
import { HeadHuntOrderDto, OrderStatus } from "../../../models/order.model.ts";
import {
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from "@mui/material";
import { isoStringToPrettyDateTime } from "../../../utils/time-utils.ts";
import { orderStatusToLabel } from "../../../utils/order-utils.ts";

type HeadHuntOrderManagementListComponentProps = {
  onOrderCompleted: (orderId: string) => void;
  orders: HeadHuntOrderDto[];
};

const HeadHuntOrderManagementListComponent: FC<
  HeadHuntOrderManagementListComponentProps
> = ({ onOrderCompleted, orders }) => {
  return (
    <>
      <TableContainer>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Id</TableCell>
              <TableCell>Status</TableCell>
              <TableCell>Target</TableCell>
              <TableCell>Customer</TableCell>
              <TableCell>Current price</TableCell>
              <TableCell>Succeeded killer</TableCell>
              <TableCell>Creation time</TableCell>
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
                <TableCell>{order.currentPrice}</TableCell>
                <TableCell>{order.succeededKiller?.displayName}</TableCell>
                <TableCell>
                  {isoStringToPrettyDateTime(order.createdTimestamp)}
                </TableCell>
                <TableCell>
                  {" "}
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
    </>
  );
};

export default memo(HeadHuntOrderManagementListComponent);
