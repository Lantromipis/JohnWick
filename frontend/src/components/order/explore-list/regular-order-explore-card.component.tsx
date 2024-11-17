import { FC, memo } from "react";
import {
  OrderType,
  RegularOrderApplicationDto,
  RegularOrderDto,
} from "../../../models/order.model.ts";
import {
  Alert,
  Button,
  Card,
  CardActions,
  CardContent,
  CardHeader,
  Divider,
  Typography,
} from "@mui/material";
import { orderTypeToLabel } from "../../../utils/order-utils.ts";

type RegularOrderExploreCardComponentProps = {
  regularOrder: RegularOrderDto;
  existingOrderApplicationsByOrderId: Record<
    string,
    RegularOrderApplicationDto
  >;
  onApplyForOrder: (orderId: string) => void;
};

const RegularOrderExploreCardComponent: FC<
  RegularOrderExploreCardComponentProps
> = ({ regularOrder, existingOrderApplicationsByOrderId, onApplyForOrder }) => {
  const appliedForOrder =
    regularOrder.id && existingOrderApplicationsByOrderId[regularOrder.id];

  return (
    <Card variant="outlined">
      <CardHeader
        title={orderTypeToLabel(OrderType.REGULAR) + " " + regularOrder.id}
      />
      <CardContent>
        <Typography gutterBottom variant="h6" component="div">
          Target: {regularOrder.targetName}
        </Typography>
        <Typography gutterBottom variant="h6" component="div">
          Price: {regularOrder.price}
        </Typography>
        <Divider sx={{ marginTop: 1, marginBottom: 1 }} />
        <Typography variant="body2" color="text.secondary">
          {regularOrder.description}
        </Typography>
        <Divider sx={{ marginTop: 1, marginBottom: 1 }} />
        {appliedForOrder ? (
          <Alert severity="success">
            You have already applied for this order. You will be notified in
            case you will be selected as an executor of this order.
          </Alert>
        ) : (
          <Alert severity="info">
            Apply for order if you like it. If you will be selected as executor,
            we will notify you and this order will appear in "My orders" page.
          </Alert>
        )}
      </CardContent>
      {!appliedForOrder && (
        <CardActions>
          <Button
            variant="outlined"
            onClick={() => {
              onApplyForOrder(regularOrder.id ?? "");
            }}
          >
            Apply
          </Button>
        </CardActions>
      )}
    </Card>
  );
};

export default memo(RegularOrderExploreCardComponent);
