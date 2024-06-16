import { FC, memo } from "react";
import { Stack, Typography, useTheme } from "@mui/material";
import UserLoginContainer from "../components/user/user-login.container.tsx";
import LoginLayout from "../layouts/login.layout.tsx";

type LoginPageProps = {};

const LoginPage: FC<LoginPageProps> = () => {
  const theme = useTheme();

  return (
    <LoginLayout>
      <Stack
        alignItems="center"
        justifyContent="center"
        sx={{ height: "100%" }}
        spacing={2}
      >
        <UserLoginContainer />
        <Typography
          sx={{ width: "330px", color: theme.palette.text.secondary }}
          variant="caption"
          textAlign={"justify"}
        >
          Forgot password or don't have an account? Please contact
          Administration or nearest Continental's receptionist in order to gain
          access to the System.
        </Typography>
      </Stack>
    </LoginLayout>
  );
};

export default memo(LoginPage);
