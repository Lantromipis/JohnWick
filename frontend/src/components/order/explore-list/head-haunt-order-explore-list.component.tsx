import { FC, memo } from "react";
import {
  Alert,
  Card,
  CardContent,
  CardHeader,
  Divider,
  Stack,
  Typography,
} from "@mui/material";
import { orderTypeToLabel } from "../../../utils/order-utils.ts";
import { HeadHuntOrderDto } from "../../../models/order.model.ts";
import dayjs from "dayjs";

type HeadHauntOrderExploreCardComponentProps = {
  headHauntOrders: HeadHuntOrderDto[];
};

const HeadHauntOrderExploreListComponent: FC<
  HeadHauntOrderExploreCardComponentProps
> = ({ headHauntOrders }) => {
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

            <Alert severity="info">
              This is free contract order and you can try to complete it. The
              reward is given only for the first one to eliminate target. The
              price for the order will constantly grow.
            </Alert>
          </CardContent>
        </Card>
      ))}
    </Stack>
  );
};

export default memo(HeadHauntOrderExploreListComponent);
