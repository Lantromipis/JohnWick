import React, { FC, memo, ReactNode, useState } from "react";
import {
  AppBar,
  Box,
  Divider,
  Drawer,
  IconButton,
  List,
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
  MANAGE_ORDERS_PAGE_PATH,
  MANAGE_USERS_PAGE_PATH,
} from "../constants/route.constants.ts";
import { AccountCircle } from "@mui/icons-material";
import AccessControlComponent from "../components/menu/access-control.component.tsx";
import DrawerPageLinkListItem from "../components/menu/drawer-page-link-list-item.componetn.tsx";
import { useDispatch, useSelector } from "react-redux";
import { selectCurrentUserRole } from "../store/current-user/current-user.selectors.ts";
import { AUTHORIZATION_HEADER_STORAGE_KEY } from "../constants/local-storage.constant.ts";
import { clearCurrentUser } from "../store/current-user/current-user.slice.ts";
import { UserRole } from "../models/user.model.ts";

type MainLayoutProps = {
  children: ReactNode;
};

const drawerWidth = 250;

const MainLayout: FC<MainLayoutProps> = ({ children }) => {
  const navigate = useNavigate();
  const theme = useTheme();
  const [accountMenuAnchorEl, setAccountMenuAnchorAnchorEl] =
    useState<null | HTMLElement>(null);

  const currentUserRole: UserRole = useSelector(selectCurrentUserRole);
  const dispatch = useDispatch();

  const handleAccountMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
    setAccountMenuAnchorAnchorEl(event.currentTarget);
  };

  const handleAccountMenuClose = () => {
    setAccountMenuAnchorAnchorEl(null);
  };

  const handleAccountMenuLogout = () => {
    handleAccountMenuClose();
    localStorage.removeItem(AUTHORIZATION_HEADER_STORAGE_KEY);
    dispatch(clearCurrentUser());
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
        <AccessControlComponent showFor={UserRole.ADMIN} role={currentUserRole}>
          <List>
            <DrawerPageLinkListItem
              label="Manage users"
              pageLink={MANAGE_USERS_PAGE_PATH}
              icon={<SupervisedUserCircleIcon />}
            />
            <DrawerPageLinkListItem
              label="Manage orders"
              pageLink={MANAGE_ORDERS_PAGE_PATH}
              icon={<AssignmentIcon />}
            />
          </List>
        </AccessControlComponent>
      </Drawer>
      <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
        <Toolbar />
        {children}
      </Box>
    </Box>
  );
};

export default memo(MainLayout);
