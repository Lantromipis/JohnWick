import React, { FC, memo, useEffect, useState } from "react";
import { userApi } from "../../../../store/user/user.api.ts";
import { emit } from "@rsql/emitter";
import builder from "@rsql/builder";
import { Box, CircularProgress, Stack, Tab, Tabs } from "@mui/material";
import HostSelectionForm from "./host-selection.form.tsx";
import { SubmitHandler } from "react-hook-form";
import { HostSelectionFormModel } from "../../../../models/schedule.model.ts";

type HostSelectionContainerProps = {
  onSubmit: SubmitHandler<HostSelectionFormModel>;
};

const HostSelectionContainer: FC<HostSelectionContainerProps> = ({
  onSubmit,
}) => {
  const [tabNum, setTabNum] = useState(0);

  const handleTabChange = (_event: React.SyntheticEvent, newValue: number) => {
    setTabNum(newValue);
  };

  const {
    data: tailors,
    refetch: refetchTailors,
    isFetching: isTailorsFetching,
  } = userApi.useListUsersQuery({
    rsqlPredicate: emit(builder.eq("role", "TAILOR")),
  });

  const {
    data: sommeliers,
    refetch: refetchSommeliers,
    isFetching: isSommeliersFetching,
  } = userApi.useListUsersQuery({
    rsqlPredicate: emit(builder.eq("role", "SOMMELIER")),
  });

  useEffect(() => {
    refetchTailors();
  }, [refetchTailors]);

  useEffect(() => {
    refetchSommeliers();
  }, [refetchSommeliers]);

  const isListRefreshing = isSommeliersFetching || isTailorsFetching;

  return (
    <Stack spacing={2} sx={{ position: "relative" }}>
      <Tabs value={tabNum} onChange={handleTabChange} variant={"fullWidth"}>
        <Tab label="Tailor" />
        <Tab label="Sommelier" />
      </Tabs>
      <Box
        sx={() =>
          isListRefreshing ? { opacity: 0.5, pointerEvents: "none" } : {}
        }
      >
        {tabNum === 0 && (
          <HostSelectionForm hosts={tailors ?? []} onSubmit={onSubmit} />
        )}
        {tabNum === 1 && (
          <HostSelectionForm hosts={sommeliers ?? []} onSubmit={onSubmit} />
        )}
      </Box>
      {isListRefreshing && (
        <CircularProgress
          sx={{
            position: "absolute",
            top: "10%",
            left: "50%",
            transform: "translate(-50%, 0)",
          }}
        />
      )}
    </Stack>
  );
};

export default memo(HostSelectionContainer);
