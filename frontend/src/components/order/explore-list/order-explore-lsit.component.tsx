import { FC, memo } from "react";
import { OrderDtoModel, OrderType } from "../../../models/order.model.ts";
import {
  Alert,
  Button,
  Card,
  CardActions,
  CardContent,
  CardHeader,
  Stack,
  Typography,
} from "@mui/material";
import { orderTypeToLabel } from "../../../utils/order-utils.ts";

type OrderExploreListComponentProps = {
  orders: OrderDtoModel[];
  onApplyForOrder: (orderId: string) => void;
};

const OrderExploreListComponent: FC<OrderExploreListComponentProps> = ({
  orders,
  onApplyForOrder,
}) => {
  return (
    <Stack spacing={2}>
      {orders.length == 0 && (
        <Alert severity="info">
          Sorry, currently there are no orders available. Please check later.
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
            <Typography variant="body2" color="text.secondary">
              {order.description}
            </Typography>
            {order.type === OrderType.HEAD_HUNT && (
              <Alert severity="info">
                This is free contract order and you can try to complete it. The
                reward is given only for the first one to eliminate target. The
                price for the order will constantly grow.
              </Alert>
            )}
            {order.type === OrderType.REGULAR && (
              <Alert severity="info">
                Apply for order if you like it. If you will be selected as
                executor, this order will appear in "My orders" page.
              </Alert>
            )}
          </CardContent>
          {order.type === OrderType.REGULAR && (
            <CardActions>
              <Button
                variant="outlined"
                onClick={() => {
                  onApplyForOrder(order.id ?? "");
                }}
              >
                Apply
              </Button>
            </CardActions>
          )}
        </Card>
      ))}
    </Stack>
  );
};

export default memo(OrderExploreListComponent);
