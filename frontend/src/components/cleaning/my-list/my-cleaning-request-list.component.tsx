import { FC, memo } from "react";
import {
  CleaningRequestDtoModel,
  CleaningRequestStatus,
} from "../../../models/cleaning.model.ts";
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

type MyCleaningRequestListComponentProps = {
  onCleaningCompleted: (cleaningId: string) => void;
  cleanings: CleaningRequestDtoModel[];
};

const MyCleaningRequestListComponent: FC<
  MyCleaningRequestListComponentProps
> = ({ onCleaningCompleted, cleanings }) => {
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
            {cleaning.status === CleaningRequestStatus.IN_PROGRESS && (
              <Alert severity="info">
                You are making this cleaning. When you are done, please confirm.
              </Alert>
            )}
            {cleaning.status === CleaningRequestStatus.COMPLETED && (
              <Alert severity="success">
                Cleaning is completed! Visit nearest Continental to get your
                reward.
              </Alert>
            )}
          </CardContent>
          <CardActions>
            {cleaning.status === CleaningRequestStatus.IN_PROGRESS && (
              <Button
                variant="outlined"
                onClick={() => onCleaningCompleted(cleaning.id)}
              >
                Complete cleaning
              </Button>
            )}
          </CardActions>
        </Card>
      ))}
    </Stack>
  );
};

export default memo(MyCleaningRequestListComponent);
