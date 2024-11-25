import { FC, memo } from "react";
import { NotificationDto } from "../../models/notification.model.ts";
import { Card, CardContent, CardHeader, Typography } from "@mui/material";
import dayjs from "dayjs";

type NotificationCardComponentProps = {
  notification: NotificationDto;
};

const NotificationCardComponent: FC<NotificationCardComponentProps> = ({
  notification,
}) => {
  return (
    <Card variant="outlined" key={notification.id}>
      <CardHeader
        title={notification.title}
        subheader={dayjs(notification.createdTimestamp).format(
          "DD.MM.YYYY HH:mm:ss",
        )}
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
