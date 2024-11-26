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
import { orderTypeToLabel } from "../../../utils/order-utils.ts";
import { HeadHuntOrderDto, OrderStatus } from "../../../models/order.model.ts";
import dayjs from "dayjs";

type HeadHauntOrderExploreCardComponentProps = {
  onTargetEliminated: (orderId: string) => void;
  headHauntOrders: HeadHuntOrderDto[];
};

const HeadHauntOrderExploreListComponent: FC<
  HeadHauntOrderExploreCardComponentProps
> = ({ onTargetEliminated, headHauntOrders }) => {
  return (
    <Stack
      direction={"row"}
      sx={{ flexWrap: "wrap" }}
      useFlexGap
      spacing={{ xs: 1, sm: 2 }}
    >
      {headHauntOrders?.map((headHauntOrder) => (
        <Card variant="outlined" sx={{ minWidth: "350px", flex: "1" }}>
          <CardHeader
            title={
              orderTypeToLabel(headHauntOrder.type) +
              " from " +
              dayjs(headHauntOrder.createdTimestamp).format(
                "DD.MM.YYYY HH:mm:ss",
              )
            }
          />
          <CardContent>
            <Typography gutterBottom variant="h6" component="div">
              Target: {headHauntOrder.targetName}
            </Typography>
            <Typography gutterBottom variant="h6" component="div">
              Current price: {headHauntOrder.currentPrice}
            </Typography>
            <Divider sx={{ marginTop: 1, marginBottom: 1 }} />
            <Typography
              variant="body2"
              color="text.secondary"
              sx={{ marginBottom: "10px" }}
            >
              {headHauntOrder.description}
            </Typography>
            {headHauntOrder.status === OrderStatus.AWAITING_SUBMISSION && (
              <Alert severity="info">
                This is free contract order and you can try to complete it. The
                reward is given only for the first one to eliminate target. The
                price for the order will constantly grow.
              </Alert>
            )}
            {headHauntOrder.status === OrderStatus.AWAITING_CLEANING && (
              <Alert severity="info">
                Cleaners are working hard to clean the crime scene. When
                cleaning is done, administrator will review your order.
              </Alert>
            )}
            {headHauntOrder.status === OrderStatus.AWAITING_APPROVAL && (
              <Alert severity="info">
                Administrator is reviewing your order. You will be notified when
                review is completed.
              </Alert>
            )}
            {headHauntOrder.status === OrderStatus.COMPLETED && (
              <Alert severity="success">
                Congratulations! Your order is marked as completed. Your debt is
                paid.
              </Alert>
            )}
          </CardContent>
          <CardActions>
            {headHauntOrder.status === OrderStatus.AWAITING_SUBMISSION && (
              <Button
                variant={"outlined"}
                onClick={() => onTargetEliminated(headHauntOrder.id ?? "")}
              >
                I have eliminated target
              </Button>
            )}
          </CardActions>
        </Card>
      ))}
    </Stack>
  );
};

export default memo(HeadHauntOrderExploreListComponent);
