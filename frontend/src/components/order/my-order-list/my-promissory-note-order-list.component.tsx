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
import {
  OrderStatus,
  PromissoryNoteOrderDto,
} from "../../../models/order.model.ts";
import { orderTypeToLabel } from "../../../utils/order-utils.ts";
import dayjs from "dayjs";

type MyPromissoryNoteOrderListProps = {
  onOrderAction: (orderId: string | undefined, newStatus: OrderStatus) => void;
  orders: PromissoryNoteOrderDto[];
};

const MyPromissoryNoteOrderListComponent: FC<
  MyPromissoryNoteOrderListProps
> = ({ onOrderAction, orders }) => {
  return (
    <Stack
      direction={"row"}
      sx={{ flexWrap: "wrap" }}
      useFlexGap
      spacing={{ xs: 1, sm: 2 }}
    >
      {orders?.map((order) => (
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
              Beneficiary: {order.beneficiary.displayName}
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
                You are now a debtor. Your debt will be paid once you complete
                this order.
              </Alert>
            )}
            {order.status === OrderStatus.AWAITING_SUIT && (
              <Alert severity="info">
                You will need a suit to complete order. If you have no suit,
                please make an appointment to tailor and then confirm when your
                suit is ready. If you already have one, please confirm now.
              </Alert>
            )}
            {order.status === OrderStatus.AWAITING_DEGUSTATION && (
              <Alert severity="info">
                You will need some guns to complete order. If you have no guns
                please make an appointment to sommelier and confirm after that.
                If you already have guns, please confirm now.
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
                Administrator is reviewing your order. You will be notified when
                review is completed.
              </Alert>
            )}
            {order.status === OrderStatus.AWAITING_APPROVAL && (
              <Alert severity="success">
                Congratulations! Your order is marked as completed. Your debt is
                paid.
              </Alert>
            )}
          </CardContent>
          <CardActions>
            {order.status === OrderStatus.AWAITING_ASSIGNEE && (
              <Button
                variant="outlined"
                onClick={() =>
                  onOrderAction(order.id, OrderStatus.AWAITING_SUIT)
                }
              >
                I am ready to pay my debt
              </Button>
            )}
            {order.status === OrderStatus.AWAITING_SUIT && (
              <Button
                variant="outlined"
                onClick={() =>
                  onOrderAction(order.id, OrderStatus.AWAITING_DEGUSTATION)
                }
              >
                I have my suit
              </Button>
            )}
            {order.status === OrderStatus.AWAITING_DEGUSTATION && (
              <Button
                variant="outlined"
                onClick={() =>
                  onOrderAction(order.id, OrderStatus.AWAITING_SUBMISSION)
                }
              >
                I have my guns
              </Button>
            )}
            {order.status === OrderStatus.AWAITING_SUBMISSION && (
              <Button
                variant="outlined"
                onClick={() =>
                  onOrderAction(order.id, OrderStatus.AWAITING_CLEANING)
                }
              >
                Target is eliminated
              </Button>
            )}
          </CardActions>
        </Card>
      ))}
    </Stack>
  );
};

export default memo(MyPromissoryNoteOrderListComponent);
