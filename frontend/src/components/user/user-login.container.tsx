import React, { FC, memo, useCallback } from "react";
import { Alert, Avatar, Paper, Stack, Typography } from "@mui/material";
import UserLoginForm from "./forms/user-login.form.tsx";
import { SubmitHandler } from "react-hook-form";
import {
  CurrentUserStateModel,
  UserLoginFormModel,
} from "../../models/user.model.ts";
import { LockOutlined } from "@mui/icons-material";
import { currentUserApi } from "../../store/current-user/current-user.api.ts";
import { AUTHORIZATION_HEADER_STORAGE_KEY } from "../../constants/local-storage.constant.ts";
import { useNavigate } from "react-router-dom";
import { HOME_PAGE_PATH } from "../../constants/route.constants.ts";
import { useDispatch } from "react-redux";
import { setCurrentUser } from "../../store/current-user/current-user.slice.ts";

type UserLoginContainerProps = {};

const UserLoginContainer: FC<UserLoginContainerProps> = () => {
  const [loginError, setLoginError] = React.useState<boolean>(false);
  const [getCurrentUser, getCurrenUserResponse] =
    currentUserApi.useLazyGetCurrentUserInfoQuery();
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const handleSubmit: SubmitHandler<UserLoginFormModel> = useCallback(
    (formData) => {
      setLoginError(false);
      let auth = btoa(`${formData.username}:${formData.password}`);
      localStorage.setItem(AUTHORIZATION_HEADER_STORAGE_KEY, auth);
      getCurrentUser()
        .unwrap()
        .then((response) => {
          let model = response as CurrentUserStateModel;
          console.log(getCurrenUserResponse);
          dispatch(setCurrentUser(model));
          navigate(HOME_PAGE_PATH);
        })
        .catch(() => {
          localStorage.removeItem(AUTHORIZATION_HEADER_STORAGE_KEY);
          setLoginError(true);
        });
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
        <UserLoginForm
          onSubmit={handleSubmit}
          loading={getCurrenUserResponse.isFetching}
        />
      </Stack>
    </Paper>
  );
};

export default memo(UserLoginContainer);
