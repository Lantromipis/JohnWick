import { FC, memo } from "react";
import {
  Card,
  CardContent,
  CardHeader,
  Divider,
  Typography,
} from "@mui/material";
import { RegularOrderDto } from "../../../models/order.model.ts";
import { orderTypeToLabel } from "../../../utils/order-utils.ts";

type MyRegularOrderCardProps = {
  order: RegularOrderDto;
};

const MyRegularOrderCardComponent: FC<MyRegularOrderCardProps> = ({
  order,
}) => {
  return (
    <Card variant="outlined">
      <CardHeader title={orderTypeToLabel(order.type) + " #" + order.id} />
      <CardContent>
        <Typography gutterBottom variant="h6" component="div">
          Target: {order.targetName}
        </Typography>
        <Typography gutterBottom variant="h6" component="div">
          Price: {order.price}
        </Typography>
        <Divider sx={{ marginTop: 1, marginBottom: 1 }} />
        <Typography variant="body2" color="text.secondary">
          {order.description}
        </Typography>
        <Divider sx={{ marginTop: 1, marginBottom: 1 }} />
      </CardContent>
    </Card>
  );
};

export default memo(MyRegularOrderCardComponent);
