import React, { FC, memo, ReactNode, useState } from "react";
import {
  AppBar,
  Box,
  Divider,
  Drawer,
  IconButton,
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Menu,
  MenuItem,
  Toolbar,
  Typography,
  useTheme,
} from "@mui/material";
import SupervisedUserCircleIcon from "@mui/icons-material/SupervisedUserCircle";
import AssignmentIcon from "@mui/icons-material/Assignment";
import { Link, useNavigate } from "react-router-dom";
import {
  LOGIN_PAGE_PATH,
  MANAGE_USERS_PAGE_PATH,
} from "../constants/route.constants.ts";
import { AccountCircle } from "@mui/icons-material";

type MainLayoutProps = {
  children: ReactNode;
};

const drawerWidth = 250;

const MainLayout: FC<MainLayoutProps> = ({ children }) => {
  const navigate = useNavigate();
  const theme = useTheme();
  const [accountMenuAnchorEl, setAccountMenuAnchorAnchorEl] =
    useState<null | HTMLElement>(null);

  const handleAccountMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
    setAccountMenuAnchorAnchorEl(event.currentTarget);
  };

  const handleAccountMenuClose = () => {
    setAccountMenuAnchorAnchorEl(null);
  };

  const handleAccountMenuLogout = () => {
    handleAccountMenuClose();
    //TODO logout
    navigate(LOGIN_PAGE_PATH);
  };

  return (
    <Box sx={{ display: "flex" }}>
      <AppBar
        position="fixed"
        sx={{ zIndex: (theme) => theme.zIndex.drawer + 1 }}
      >
        <Toolbar>
          <Box sx={{ flexGrow: 1 }}>
            <Link
              to={"/"}
              style={{
                textDecoration: "none",
                boxShadow: "none",
              }}
            >
              <Typography
                variant="h6"
                noWrap
                component="div"
                sx={{ color: theme.palette.primary.contrastText }}
              >
                The Baba Yaga System
              </Typography>
            </Link>
          </Box>
          <IconButton color="inherit" onClick={handleAccountMenuOpen}>
            <AccountCircle />
          </IconButton>
          <Menu
            id="menu-appbar"
            anchorEl={accountMenuAnchorEl}
            anchorOrigin={{
              vertical: "top",
              horizontal: "right",
            }}
            keepMounted
            transformOrigin={{
              vertical: "top",
              horizontal: "right",
            }}
            open={Boolean(accountMenuAnchorEl)}
            onClose={handleAccountMenuClose}
          >
            <MenuItem onClick={handleAccountMenuLogout}>Logout</MenuItem>
          </Menu>
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
        <List>
          <ListItem>
            <ListItemButton
              onClick={() => {
                navigate(MANAGE_USERS_PAGE_PATH);
              }}
            >
              <ListItemIcon>
                <SupervisedUserCircleIcon />
              </ListItemIcon>
              <ListItemText>Manage users</ListItemText>
            </ListItemButton>
          </ListItem>
          <ListItem>
            <ListItemButton>
              <ListItemIcon>
                <AssignmentIcon />
              </ListItemIcon>
              <ListItemText>Manage orders</ListItemText>
            </ListItemButton>
          </ListItem>
        </List>
      </Drawer>
      <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
        <Toolbar />
        {children}
      </Box>
    </Box>
  );
};

export default memo(MainLayout);
