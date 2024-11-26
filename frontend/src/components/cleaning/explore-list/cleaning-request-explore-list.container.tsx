import { FC, memo, useEffect } from "react";
import { emit } from "@rsql/emitter";
import builder from "@rsql/builder";
import { cleaningApi } from "../../../store/cleaning/cleaning.api.ts";
import { Alert, Stack } from "@mui/material";
import CleaningRequestExploreListComponent from "./cleaning-request-explore-list.component.tsx";
import { enqueueSnackbar } from "notistack";
import { CleaningRequestStatus } from "../../../models/cleaning.model.ts";

type CleaningRequestExploreListContainerProps = {};

const CleaningRequestExploreListContainer: FC<
  CleaningRequestExploreListContainerProps
> = () => {
  const { data: cleaningRequests, refetch } = cleaningApi.useListCleaningsQuery(
    {
      rsqlPredicate: emit(builder.eq("status", "CREATED")),
    },
  );

  const [updateCleaning] = cleaningApi.useUpdateCleaningMutation();

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

  return (
    <Stack spacing={2}>
      {cleaningRequests?.length === 0 && (
        <Alert severity="info">
          Sorry, currently there are no cleaning requests. Please check later.
        </Alert>
      )}
      {cleaningRequests && (
        <CleaningRequestExploreListComponent
          onCleaningApplied={onCleaningApplied}
          cleanings={cleaningRequests}
        />
      )}
    </Stack>
  );
};

export default memo(CleaningRequestExploreListContainer);
