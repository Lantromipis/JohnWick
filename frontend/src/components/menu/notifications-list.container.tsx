import { FC, memo, useEffect, useState } from "react";
import NotificationsListComponent from "./notifications-list.component.tsx";
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  IconButton,
  Stack,
} from "@mui/material";
import { Notifications } from "@mui/icons-material";
import { notificationApi } from "../../store/notifications/notification.api.ts";

type NotificationsListContainerProps = {};

const NotificationsListContainer: FC<NotificationsListContainerProps> = () => {
  const { data, refetch } = notificationApi.useGetNotificationsQuery();
  const [dialogOpen, setDialogOpen] = useState<boolean>(false);

  const handleDialogOpen = () => {
    refetch();
    setDialogOpen(true);
  };

  const handleDialogClose = () => {
    setDialogOpen(false);
  };

  useEffect(() => {
    refetch();
  }, [refetch]);

  return (
    <>
      <IconButton color="inherit" onClick={handleDialogOpen}>
        <Notifications />
      </IconButton>
      <Dialog open={dialogOpen} onClose={handleDialogClose}>
        <DialogTitle>Notifications</DialogTitle>
        <DialogContent sx={{ width: "350px" }}>
          <Stack spacing={2} sx={{ paddingTop: "10px" }}>
            <NotificationsListComponent notifications={data ?? []} />
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleDialogClose}>Close</Button>
        </DialogActions>
      </Dialog>
    </>
  );
};

export default memo(NotificationsListContainer);
