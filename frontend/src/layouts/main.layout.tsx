import { FC, memo, ReactNode } from "react";
import {
  AppBar,
  Box,
  Divider,
  Drawer,
  Toolbar,
  Typography,
} from "@mui/material";

type MainLayoutProps = {
  children: ReactNode;
};

const drawerWidth = 250;

const MainLayout: FC<MainLayoutProps> = ({ children }) => {
  return (
    <Box sx={{ display: "flex" }}>
      <AppBar
        position="fixed"
        sx={{ zIndex: (theme) => theme.zIndex.drawer + 1 }}
      >
        <Toolbar>
          <Typography variant="h6" noWrap component="div">
            The Baba Yaga System
          </Typography>
        </Toolbar>
      </AppBar>
      <Drawer
        variant="permanent"
        sx={{
          width: drawerWidth,
          flexShrink: 0,
          [`& .MuiDrawer-paper`]: {
            width: drawerWidth,
            boxSizing: "border-box",
          },
        }}
      >
        <Toolbar />
        <Divider />
      </Drawer>
      <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
        <Toolbar />
        {children}
      </Box>
    </Box>
  );
};

export default memo(MainLayout);
