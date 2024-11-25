import { FC, memo, useEffect } from "react";
import { emit } from "@rsql/emitter";
import builder from "@rsql/builder";
import { cleaningApi } from "../../../store/cleaning/cleaning.api.ts";
import { Alert, Stack } from "@mui/material";
import { enqueueSnackbar } from "notistack";
import { CleaningRequestStatus } from "../../../models/cleaning.model.ts";
import MyCleaningRequestListComponent from "./my-cleaning-request-list.component.tsx";

type MyCleaningRequestListContainerProps = {};

const MyCleaningRequestListContainer: FC<
  MyCleaningRequestListContainerProps
> = () => {
  const { data: cleaningRequests, refetch } = cleaningApi.useListCleaningsQuery(
    {
      rsqlPredicate: emit(
        builder.or(
          builder.eq("status", "IN_PROGRESS"),
          builder.eq("status", "COMPLETED"),
        ),
      ),
    },
  );

  const [updateCleaning] = cleaningApi.useUpdateCleaningMutation();

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

  return (
    <Stack spacing={2}>
      {cleaningRequests?.length === 0 && (
        <Alert severity="info">
          Sorry, currently there are no cleaning requests. Please apply for a
          new one.
        </Alert>
      )}
      {cleaningRequests && (
        <MyCleaningRequestListComponent
          onCleaningCompleted={onCleaningCompleted}
          cleanings={cleaningRequests}
        />
      )}
    </Stack>
  );
};

export default memo(MyCleaningRequestListContainer);
