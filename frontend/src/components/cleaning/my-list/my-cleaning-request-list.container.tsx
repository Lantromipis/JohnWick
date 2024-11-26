import { FC, memo, useEffect } from "react";
import { emit } from "@rsql/emitter";
import builder from "@rsql/builder";
import { cleaningApi } from "../../../store/cleaning/cleaning.api.ts";
import { Alert, Box, CircularProgress, Stack } from "@mui/material";
import { enqueueSnackbar } from "notistack";
import { CleaningRequestStatus } from "../../../models/cleaning.model.ts";
import MyCleaningRequestListComponent from "./my-cleaning-request-list.component.tsx";

type MyCleaningRequestListContainerProps = {};

const MyCleaningRequestListContainer: FC<
  MyCleaningRequestListContainerProps
> = () => {
  const {
    data: cleaningRequests,
    refetch,
    isLoading,
    isFetching,
  } = cleaningApi.useListCleaningsQuery({
    rsqlPredicate: emit(
      builder.or(
        builder.eq("status", "IN_PROGRESS"),
        builder.eq("status", "COMPLETED"),
      ),
    ),
  });

  const [updateCleaning, updateCleaningResponse] =
    cleaningApi.useUpdateCleaningMutation();

  const onCleaningCompleted = (cleaningId: string) => {
    updateCleaning({
      id: cleaningId,
      status: CleaningRequestStatus.COMPLETED,
    })
      .unwrap()
      .then(() => {
        enqueueSnackbar({
          variant: "success",
          message: `You completed cleaning`,
        });
      });
  };

  useEffect(() => {
    refetch();
  }, [refetch]);

  if (isLoading) {
    return (
      <Stack
        spacing={2}
        alignItems="center"
        justifyContent="center"
        display="flex"
      >
        <CircularProgress />
      </Stack>
    );
  }

  if (!cleaningRequests || cleaningRequests.length === 0) {
    return (
      <Alert severity="info">
        You have no cleaning requests. Apply for a new one!
      </Alert>
    );
  }

  const isListRefreshing = isFetching || updateCleaningResponse.isLoading;

  return (
    <Stack sx={{ position: "relative" }}>
      <Box
        sx={() =>
          isListRefreshing ? { opacity: 0.5, pointerEvents: "none" } : {}
        }
      >
        <MyCleaningRequestListComponent
          onCleaningCompleted={onCleaningCompleted}
          cleanings={cleaningRequests}
        />
      </Box>
      {isListRefreshing && (
        <CircularProgress
          sx={{
            position: "absolute",
            top: "20%",
            left: "50%",
            transform: "translate(-50%, 0)",
          }}
        />
      )}
    </Stack>
  );
};

export default memo(MyCleaningRequestListContainer);
