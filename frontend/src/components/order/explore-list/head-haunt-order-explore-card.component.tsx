import { FC, memo } from "react";
import {
  Alert,
  Card,
  CardContent,
  CardHeader,
  Divider,
  Typography,
} from "@mui/material";
import { orderTypeToLabel } from "../../../utils/order-utils.ts";
import { HeadHuntOrderDto, OrderType } from "../../../models/order.model.ts";

type HeadHauntOrderExploreCardComponentProps = {
  headHauntOrder: HeadHuntOrderDto;
};

const HeadHauntOrderExploreCardComponent: FC<
  HeadHauntOrderExploreCardComponentProps
> = ({ headHauntOrder }) => {
  return (
    <Card variant="outlined">
      <CardHeader
        title={orderTypeToLabel(OrderType.HEAD_HUNT) + " #" + headHauntOrder.id}
      />
      <CardContent>
        <Typography gutterBottom variant="h6" component="div">
          Target: {headHauntOrder.targetName}
        </Typography>
        <Typography gutterBottom variant="h6" component="div">
          Current price: {headHauntOrder.currentPrice}
        </Typography>
        <Divider sx={{ marginTop: 1, marginBottom: 1 }} />
        <Typography variant="body2" color="text.secondary">
          {headHauntOrder.description}
        </Typography>
        <Divider sx={{ marginTop: 1, marginBottom: 1 }} />
        <Alert severity="info">
          This is free contract order and you can try to complete it. The reward
          is given only for the first one to eliminate target. The price for the
          order will constantly grow.
        </Alert>
      </CardContent>
    </Card>
  );
};

export default memo(HeadHauntOrderExploreCardComponent);
