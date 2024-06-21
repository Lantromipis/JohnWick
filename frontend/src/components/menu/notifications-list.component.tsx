import { FC, memo } from "react";
import { NotificationDto } from "../../models/notification.model.ts";
import {
  Alert,
  Card,
  CardContent,
  CardHeader,
  Stack,
  Typography,
} from "@mui/material";

type NotificationListComponentProps = {
  notifications: NotificationDto[];
};

const NotificationListComponent: FC<NotificationListComponentProps> = ({
  notifications,
}) => {
  return (
    <Stack spacing={2}>
      {notifications.length == 0 && (
        <Alert severity="info">
          There are no notifications now. Come back later!
        </Alert>
      )}
      {notifications.map((notification) => (
        <Card variant="outlined">
          <CardHeader
            title={notification.title}
            subheader={notification.createdTimestamp}
          />
          <CardContent>
            {notification.content && (
              <Typography variant="body2" color="text.secondary">
                {notification.content}
              </Typography>
            )}
          </CardContent>
        </Card>
      ))}
    </Stack>
  );
};

export default memo(NotificationListComponent);
