import { FC, memo } from "react";
import {
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
  Stack,
  Typography,
} from "@mui/material";
import { orderTypeToLabel } from "../../../utils/order-utils.ts";
import dayjs from "dayjs";

type RegularOrderExploreCardComponentProps = {
  regularOrders: RegularOrderDto[];
  existingOrderApplicationsByOrderId: Record<
    string,
    RegularOrderApplicationDto
  >;
  onApplyForOrder: (orderId: string) => void;
};

const RegularOrderExploreListComponent: FC<
  RegularOrderExploreCardComponentProps
> = ({
  regularOrders,
  existingOrderApplicationsByOrderId,
  onApplyForOrder,
}) => {
  return (
    <Stack
      direction={"row"}
      sx={{ flexWrap: "wrap" }}
      useFlexGap
      spacing={{ xs: 1, sm: 2 }}
    >
      {regularOrders?.map((regularOrder) => {
        const appliedForOrder =
          regularOrder.id &&
          existingOrderApplicationsByOrderId[regularOrder.id];
        return (
          <Card variant="outlined" sx={{ minWidth: "350px", flex: "1" }}>
            <CardHeader
              title={
                orderTypeToLabel(regularOrder.type) +
                " from " +
                dayjs(regularOrder.createdTimestamp).format(
                  "DD.MM.YYYY HH:mm:ss",
                )
              }
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
                  You have already applied for this order. You will be notified
                  in case you will be selected as an executor of this order.
                </Alert>
              ) : (
                <Alert severity="info">
                  Apply for order if you like it. If you will be selected as
                  executor, we will notify you and this order will appear in "My
                  orders" page.
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
      })}
    </Stack>
  );
};

export default memo(RegularOrderExploreListComponent);
