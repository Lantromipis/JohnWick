import { FC, memo } from "react";
import { CleaningRequestDtoModel } from "../../../models/cleaning.model.ts";
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
import dayjs from "dayjs";
import { orderTypeToLabel } from "../../../utils/order-utils.ts";

type CleaningRequestExploreListComponentProps = {
  onCleaningApplied: (cleaningId: string) => void;
  cleanings: CleaningRequestDtoModel[];
};

const CleaningRequestExploreListComponent: FC<
  CleaningRequestExploreListComponentProps
> = ({ onCleaningApplied, cleanings }) => {
  return (
    <Stack
      direction={"row"}
      sx={{ flexWrap: "wrap" }}
      useFlexGap
      spacing={{ xs: 1, sm: 2 }}
    >
      {cleanings?.map((cleaning) => (
        <Card variant="outlined" sx={{ minWidth: "350px", flex: "1" }}>
          <CardHeader
            title={
              "Cleaning from " +
              dayjs(cleaning.createdTimestamp).format("DD.MM.YYYY HH:mm:ss")
            }
          />
          <CardContent>
            <Typography gutterBottom variant="h6" component="div">
              Order target: {cleaning.order.targetName}
            </Typography>
            <Typography gutterBottom variant="h6" component="div">
              Order type: {orderTypeToLabel(cleaning.order.type)}
            </Typography>
            <Typography
              gutterBottom
              variant="h6"
              component="div"
              sx={{ marginBottom: "10px" }}
            >
              Requested by: {cleaning.requestedBy.displayName}
            </Typography>

            <Alert severity="info">
              Only the first cleaner will be able to have this job. Be first to
              apply!
            </Alert>
          </CardContent>
          <CardActions>
            <Button
              variant="outlined"
              onClick={() => onCleaningApplied(cleaning.id)}
            >
              Make this cleaning
            </Button>
          </CardActions>
        </Card>
      ))}
    </Stack>
  );
};

export default memo(CleaningRequestExploreListComponent);
