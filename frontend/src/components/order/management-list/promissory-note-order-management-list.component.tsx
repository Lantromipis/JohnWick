import { FC, memo } from "react";
import {
  OrderStatus,
  PromissoryNoteOrderDto,
} from "../../../models/order.model.ts";
import {
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from "@mui/material";
import { orderStatusToLabel } from "../../../utils/order-utils.ts";
import { isoStringToPrettyDateTime } from "../../../utils/time-utils.ts";

type PromissoryNoteOrderManagementListComponentProps = {
  onOrderCompleted: (orderId: string) => void;
  orders: PromissoryNoteOrderDto[];
};

const PromissoryNoteOrderManagementListComponent: FC<
  PromissoryNoteOrderManagementListComponentProps
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
              <TableCell>Debtor</TableCell>
              <TableCell>Beneficiary</TableCell>
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
                <TableCell>{order.debtor.displayName}</TableCell>
                <TableCell>{order.beneficiary.displayName}</TableCell>
                <TableCell>
                  {isoStringToPrettyDateTime(order.createdTimestamp)}
                </TableCell>
                <TableCell>
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

export default memo(PromissoryNoteOrderManagementListComponent);
