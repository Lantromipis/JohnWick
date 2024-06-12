import React, { FC, memo, useCallback } from "react";
import { Alert, Avatar, Paper, Stack, Typography } from "@mui/material";
import UserLoginForm from "./forms/user-login-from.component.tsx";
import { SubmitHandler } from "react-hook-form";
import { UserLoginFormModel } from "../../models/user.model.ts";
import { LockOutlined } from "@mui/icons-material";

type UserLoginContainerProps = {};

const UserLoginContainer: FC<UserLoginContainerProps> = () => {
  const [loginError, setLoginError] = React.useState<boolean>(false);

  const handleSubmit: SubmitHandler<UserLoginFormModel> = useCallback(
    (data) => {
      // TODO long
      setLoginError(true);
      console.log(data);
    },
    [],
  );

  return (
    <Paper
      sx={{
        width: 350,
        minHeight: 460,
        padding: "15px 20px",
      }}
    >
      <Stack
        direction="column"
        spacing={2}
        alignItems="center"
        justifyContent="center"
      >
        <Avatar sx={{ m: 1, bgcolor: "secondary.main" }}>
          <LockOutlined />
        </Avatar>
        <Typography sx={{ marginBottom: "20px" }} variant="h6">
          Enter your credentials
        </Typography>
        {loginError && (
          <Alert severity="error" sx={{ width: "270px" }}>
            Incorrect username or password provided
          </Alert>
        )}
        <UserLoginForm onSubmit={handleSubmit} loading={false} />
      </Stack>
    </Paper>
  );
};

export default memo(UserLoginContainer);
