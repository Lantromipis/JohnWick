import { FC, memo } from "react";
import {
  Alert,
  Button,
  Card,
  CardActions,
  CardContent,
  CardHeader,
  Divider,
  Stack,
  Typography,
} from "@mui/material";
import { OrderStatus, RegularOrderDto } from "../../../models/order.model.ts";
import { orderTypeToLabel } from "../../../utils/order-utils.ts";
import dayjs from "dayjs";

function getOrderActionButtonProps(orderStatus?: OrderStatus) {
  switch (orderStatus) {
    case OrderStatus.AWAITING_ASSIGNEE:
      return {
        nextStatus: OrderStatus.AWAITING_SUIT,
        buttonLabel: "I am ready to eliminate target",
      };
    case OrderStatus.AWAITING_SUIT:
      return {
        nextStatus: OrderStatus.AWAITING_DEGUSTATION,
        buttonLabel: "I have my suit",
      };
    case OrderStatus.AWAITING_DEGUSTATION:
      return {
        nextStatus: OrderStatus.AWAITING_SUBMISSION,
        buttonLabel: "I have my guns",
      };
    case OrderStatus.AWAITING_SUBMISSION:
      return {
        nextStatus: OrderStatus.AWAITING_CLEANING,
        buttonLabel: "Target is eliminated",
      };
    default:
      return undefined;
  }
}

type MyRegularOrderListProps = {
  onOrderAction: (orderId: string | undefined, newStatus: OrderStatus) => void;
  orders: RegularOrderDto[];
};

const MyRegularOrderListComponent: FC<MyRegularOrderListProps> = ({
  onOrderAction,
  orders,
}) => {
  return (
    <Stack
      direction={"row"}
      sx={{ flexWrap: "wrap" }}
      useFlexGap
      spacing={{ xs: 1, sm: 2 }}
    >
      {orders.map((order) => {
        const actionButtonProps = getOrderActionButtonProps(order.status);
        console.log(order.status, actionButtonProps);
        return (
          <Card variant="outlined" sx={{ minWidth: "350px", flex: "1" }}>
            <CardHeader
              title={
                orderTypeToLabel(order.type) +
                " from " +
                dayjs(order.createdTimestamp).format("DD.MM.YYYY HH:mm:ss")
              }
            />
            <CardContent>
              <Typography gutterBottom variant="h6" component="div">
                Target: {order.targetName}
              </Typography>
              <Typography gutterBottom variant="h6" component="div">
                Price: {order.price}
              </Typography>
              <Divider sx={{ marginTop: 1, marginBottom: 1 }} />
              <Typography
                variant="body2"
                color="text.secondary"
                sx={{ marginBottom: "10px" }}
              >
                {order.description}
              </Typography>
              {order.status === OrderStatus.AWAITING_ASSIGNEE && (
                <Alert severity="info">
                  You were selected as assignee for this order. Please confirm
                  that you received this order.
                </Alert>
              )}
              {order.status === OrderStatus.AWAITING_SUIT && (
                <Alert severity="info">
                  You will need a suit to complete order. If you have no suit,
                  please make an appointment to tailor and then confirm when
                  your suit is ready. If you already have one, please confirm
                  now.
                </Alert>
              )}
              {order.status === OrderStatus.AWAITING_DEGUSTATION && (
                <Alert severity="info">
                  You will need some guns to complete order. If you have no guns
                  please make an appointment to sommelier and confirm after
                  that. If you already have guns, please confirm now.
                </Alert>
              )}
              {order.status === OrderStatus.AWAITING_SUBMISSION && (
                <Alert severity="info">
                  You are ready to complete order. When you eliminate target
                  please confirm.
                </Alert>
              )}
              {order.status === OrderStatus.AWAITING_CLEANING && (
                <Alert severity="info">
                  Cleaners are working hard to clean the crime scene. When
                  cleaning is done, administrator will review your order.
                </Alert>
              )}
              {order.status === OrderStatus.AWAITING_APPROVAL && (
                <Alert severity="info">
                  Administrator is reviewing your order. You will be notified
                  when review is completed.
                </Alert>
              )}
              {order.status === OrderStatus.COMPLETED && (
                <Alert severity="success">
                  Congratulations! Your order is marked as completed. Please
                  contact nearest Continental to gain your reward.
                </Alert>
              )}
            </CardContent>
            <CardActions>
              {actionButtonProps && (
                <Button
                  variant="outlined"
                  onClick={() =>
                    onOrderAction(order.id, actionButtonProps.nextStatus)
                  }
                >
                  {actionButtonProps.buttonLabel}
                </Button>
              )}
            </CardActions>
          </Card>
        );
      })}
    </Stack>
  );
};

export default memo(MyRegularOrderListComponent);
