import { FC, memo, useState } from "react";
import {
  Alert,
  Box,
  Button,
  CircularProgress,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  IconButton,
  Stack,
} from "@mui/material";
import { Notifications } from "@mui/icons-material";
import { notificationApi } from "../../store/notifications/notification.api.ts";
import NotificationCardComponent from "./notification-card.component.tsx";

type NotificationsListContainerProps = {};

const NotificationsListContainer: FC<NotificationsListContainerProps> = () => {
  const {
    data: notifications,
    refetch,
    isLoading,
    isFetching,
  } = notificationApi.useGetNotificationsQuery();
  const [dialogOpen, setDialogOpen] = useState<boolean>(false);

  const handleDialogOpen = () => {
    refetch();
    setDialogOpen(true);
  };

  const handleDialogClose = () => {
    setDialogOpen(false);
  };

  return (
    <>
      <IconButton color="inherit" onClick={handleDialogOpen}>
        <Notifications />
      </IconButton>
      <Dialog open={dialogOpen} onClose={handleDialogClose}>
        <DialogTitle>Notifications</DialogTitle>
        <DialogContent sx={{ width: "350px" }}>
          {isLoading && <CircularProgress />}
          <Box
            sx={() =>
              isFetching ? { opacity: 0.5, pointerEvents: "none" } : {}
            }
          >
            <Stack spacing={2}>
              {notifications?.length === 0 && (
                <Alert severity="info" key={"alert"}>
                  There are no notifications now. Come back later!
                </Alert>
              )}
              {notifications?.map((notification) => (
                <NotificationCardComponent notification={notification} />
              ))}
            </Stack>
            {isFetching && (
              <CircularProgress
                sx={{
                  position: "absolute",
                  top: "20%",
                  left: "50%",
                  transform: "translate(-50%, 0)",
                }}
              />
            )}
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleDialogClose}>Close</Button>
        </DialogActions>
      </Dialog>
    </>
  );
};

export default memo(NotificationsListContainer);
