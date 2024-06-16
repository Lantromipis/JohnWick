import { FC, memo, ReactNode } from "react";
import { AppBar, Box, Toolbar, Typography } from "@mui/material";

type LoginLayoutProps = {
  children: ReactNode;
};

const LoginLayout: FC<LoginLayoutProps> = ({ children }) => {
  return (
    <Box sx={{ display: "flex", minHeight: "100vh" }}>
      <AppBar>
        <Toolbar>
          <Typography variant="h6" noWrap component="div">
            The Baba Yaga System
          </Typography>
        </Toolbar>
      </AppBar>
      <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
        {children}
      </Box>
    </Box>
  );
};

export default memo(LoginLayout);
