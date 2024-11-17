import { FC, memo } from "react";
import { NotificationDto } from "../../models/notification.model.ts";
import { Card, CardContent, CardHeader, Typography } from "@mui/material";

type NotificationCardComponentProps = {
  notification: NotificationDto;
};

const NotificationCardComponent: FC<NotificationCardComponentProps> = ({
  notification,
}) => {
  return (
    <Card variant="outlined">
      <CardHeader
        title={notification.title}
        subheader={notification.createdTimestamp}
      />
      <CardContent>
        {notification.message && (
          <Typography variant="body2" color="text.secondary">
            {notification.message}
          </Typography>
        )}
      </CardContent>
    </Card>
  );
};

export default memo(NotificationCardComponent);
