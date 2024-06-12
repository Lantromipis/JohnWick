import { FC, memo } from "react";
import { Stack } from "@mui/material";
import UserLoginContainer from "../components/user/user-login.container.tsx";
import LoginLayout from "../layouts/login.layout.tsx";

type LoginPageProps = {};

const LoginPage: FC<LoginPageProps> = () => {
  return (
    <LoginLayout>
      <Stack
        alignItems="center"
        justifyContent="center"
        sx={{ height: "100%" }}
      >
        <UserLoginContainer />
      </Stack>
    </LoginLayout>
  );
};

export default memo(LoginPage);
