import { FC, memo } from "react";
import { HeadHuntOrderDto } from "../../../models/order.model.ts";
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from "@mui/material";

type HeadHuntOrderManagementListComponentProps = {
  orders: HeadHuntOrderDto[];
};

const HeadHuntOrderManagementListComponent: FC<
  HeadHuntOrderManagementListComponentProps
> = ({ orders }) => {
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
                <TableCell> </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </>
  );
};

export default memo(HeadHuntOrderManagementListComponent);
