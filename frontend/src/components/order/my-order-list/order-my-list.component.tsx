import { FC, memo } from "react";
import {
  Alert,
  Card,
  CardContent,
  CardHeader,
  Stack,
  Typography,
} from "@mui/material";
import {
  OrderDtoModel,
  OrderType,
  PromissoryNoteOrderDto,
} from "../../../models/order.model.ts";
import { orderTypeToLabel } from "../../../utils/order-utils.ts";

type MyOrdersComponentProps = {
  orders: OrderDtoModel[];
};

function getOrderBeneficiary(order: OrderDtoModel): string {
  const promissoryOrder = order as PromissoryNoteOrderDto;
  return promissoryOrder.beneficiary.displayName;
}

const MyOrdersComponent: FC<MyOrdersComponentProps> = ({ orders }) => {
  return (
    <Stack spacing={2}>
      {orders.length == 0 && (
        <Alert severity="info">
          You have no active orders. Apply for a new one using "Explore orders"
          page!
        </Alert>
      )}
      {orders.map((order) => (
        <Card variant="outlined">
          <CardHeader title={orderTypeToLabel(order.type)} />
          <CardContent>
            <Typography gutterBottom variant="h6" component="div">
              Target: {order.target}
            </Typography>
            <Typography gutterBottom variant="h6" component="div">
              Price: {order.price}
            </Typography>
            {order.type === OrderType.PROMISSORY_NOTE && (
              <Typography gutterBottom variant="h6" component="div">
                Beneficiary: {getOrderBeneficiary(order)}
              </Typography>
            )}
            <Typography variant="body2" color="text.secondary">
              {order.description}
            </Typography>
          </CardContent>
        </Card>
      ))}
    </Stack>
  );
};

export default memo(MyOrdersComponent);
