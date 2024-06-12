import { CssBaseline } from "@mui/material";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import LoginPage from "./pages/login.page.tsx";
import HomePage from "./pages/home.page.tsx";
import MangeUsersPage from "./pages/manage-users.page.tsx";
import {
  LOGIN_PAGE_PATH,
  MANAGE_USERS_PAGE_PATH,
} from "./constants/route.constants.ts";

function App() {
  return (
    <>
      <CssBaseline />
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path={LOGIN_PAGE_PATH} element={<LoginPage />} />
          <Route path={MANAGE_USERS_PAGE_PATH} element={<MangeUsersPage />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </BrowserRouter>
    </>
  );
}

export default App;
