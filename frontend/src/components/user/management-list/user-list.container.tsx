import { FC, memo, useEffect } from "react";
import UserListComponent from "./user-list.component.tsx";
import { userApi } from "../../../store/user/user.api.ts";
import { Box, CircularProgress, Stack } from "@mui/material";

type UserListContainerProps = {};

const UserListContainer: FC<UserListContainerProps> = () => {
  const { data, refetch, isLoading, isFetching } = userApi.useListUsersQuery(
    {},
  );

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

  return (
    <Stack sx={{ position: "relative" }}>
      <Box
        sx={() => (isFetching ? { opacity: 0.5, pointerEvents: "none" } : {})}
      >
        <UserListComponent users={data ?? []} />
      </Box>
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
    </Stack>
  );
};

export default memo(UserListContainer);
