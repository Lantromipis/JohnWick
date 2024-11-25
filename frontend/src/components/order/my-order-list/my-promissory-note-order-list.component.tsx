import { FC, memo } from "react";
import {
  Card,
  CardContent,
  CardHeader,
  Divider,
  Stack,
  Typography,
} from "@mui/material";
import { PromissoryNoteOrderDto } from "../../../models/order.model.ts";
import { orderTypeToLabel } from "../../../utils/order-utils.ts";
import dayjs from "dayjs";

type MyPromissoryNoteOrderListProps = {
  orders: PromissoryNoteOrderDto[];
};

const MyPromissoryNoteOrderListComponent: FC<
  MyPromissoryNoteOrderListProps
> = ({ orders }) => {
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
            <Typography variant="body2" color="text.secondary">
              {order.description}
            </Typography>
          </CardContent>
        </Card>
      ))}
    </Stack>
  );
};

export default memo(MyPromissoryNoteOrderListComponent);
