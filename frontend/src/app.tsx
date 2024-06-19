import { CssBaseline } from "@mui/material";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import LoginPage from "./pages/login.page.tsx";
import HomePage from "./pages/home.page.tsx";
import MangeUsersPage from "./pages/manage-users.page.tsx";
import {
  EXPLORE_ORDERS_PAGE_PATH,
  LOGIN_PAGE_PATH,
  MANAGE_ORDERS_PAGE_PATH,
  MANAGE_USERS_PAGE_PATH,
  MY_ORDERS_PAGE_PATH,
} from "./constants/route.constants.ts";
import { Provider } from "react-redux";
import { persistor, store } from "./store/store.tsx";
import ManageOrdersPage from "./pages/manage-orders.page.tsx";
import { PersistGate } from "redux-persist/integration/react";
import ExploreOrdersPage from "./pages/explore-orders.page.tsx";
import MyOrdersPage from "./pages/my-orders-page.tsx";

function App() {
  return (
    <>
      <CssBaseline />
      <Provider store={store}>
        <PersistGate loading={null} persistor={persistor}>
          <BrowserRouter>
            <Routes>
              <Route path="/" element={<HomePage />} />
              <Route path={LOGIN_PAGE_PATH} element={<LoginPage />} />
              <Route
                path={MANAGE_USERS_PAGE_PATH}
                element={<MangeUsersPage />}
              />
              <Route
                path={MANAGE_ORDERS_PAGE_PATH}
                element={<ManageOrdersPage />}
              />
              <Route
                path={EXPLORE_ORDERS_PAGE_PATH}
                element={<ExploreOrdersPage />}
              />
              <Route path={MY_ORDERS_PAGE_PATH} element={<MyOrdersPage />} />
              <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
          </BrowserRouter>
        </PersistGate>
      </Provider>
    </>
  );
}

export default App;
