import { FC, memo, useEffect } from "react";
import { emit } from "@rsql/emitter";
import builder from "@rsql/builder";
import { cleaningApi } from "../../../store/cleaning/cleaning.api.ts";
import { Alert, Box, CircularProgress, Stack } from "@mui/material";
import CleaningRequestExploreListComponent from "./cleaning-request-explore-list.component.tsx";
import { enqueueSnackbar } from "notistack";
import { CleaningRequestStatus } from "../../../models/cleaning.model.ts";

type CleaningRequestExploreListContainerProps = {};

const CleaningRequestExploreListContainer: FC<
  CleaningRequestExploreListContainerProps
> = () => {
  const {
    data: cleaningRequests,
    refetch,
    isLoading,
    isFetching,
  } = cleaningApi.useListCleaningsQuery({
    rsqlPredicate: emit(builder.eq("status", "CREATED")),
  });

  const [updateCleaning, updateCleaningResponse] =
    cleaningApi.useUpdateCleaningMutation();

  const onCleaningApplied = (cleaningId: string) => {
    updateCleaning({
      id: cleaningId,
      status: CleaningRequestStatus.IN_PROGRESS,
    })
      .unwrap()
      .then(() => {
        enqueueSnackbar({
          variant: "success",
          message: `You applied for cleaning`,
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
        Sorry, currently there are no cleaning requests. Please check later.
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
        <CleaningRequestExploreListComponent
          onCleaningApplied={onCleaningApplied}
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

export default memo(CleaningRequestExploreListContainer);
