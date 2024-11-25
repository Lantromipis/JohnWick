import { CssBaseline } from "@mui/material";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import LoginPage from "./pages/login.page.tsx";
import HomePage from "./pages/home.page.tsx";
import MangeUsersPage from "./pages/manage-users.page.tsx";
import {
  EXPLORE_CLEANINGS_PAGE_PATH,
  EXPLORE_ORDERS_PAGE_PATH,
  LOGIN_PAGE_PATH,
  MANAGE_ORDERS_PAGE_PATH,
  MANAGE_USERS_PAGE_PATH,
  MY_APPOINTMENTS_PAGE_PATH,
  MY_CLEANINGS_PAGE_PATH,
  MY_ORDERS_PAGE_PATH,
  MY_SCHEDULE_PAGE_PATH,
  PLAN_APPOINTMENT_PAGE_PATH,
} from "./constants/route.constants.ts";
import { Provider } from "react-redux";
import { persistor, store } from "./store/store.tsx";
import ManageOrdersPage from "./pages/manage-orders.page.tsx";
import { PersistGate } from "redux-persist/integration/react";
import ExploreOrdersPage from "./pages/explore-orders.page.tsx";
import MyOrdersPage from "./pages/my-orders-page.tsx";
import MySchedulePage from "./pages/my-schedule-page.tsx";
import { SnackbarProvider } from "notistack";
import dayjs from "dayjs";
import "dayjs/locale/en-gb";
import PlanAppointmentPage from "./pages/plan-appointmen.page.tsx";
import MyAppointmentsPage from "./pages/my-appointments.page.tsx";
import ExploreCleaningRequestsPage from "./pages/explore-cleaning-requests.page.tsx";
import MyCleaningRequestsPage from "./pages/my-cleaning-requests.page.tsx";

function App() {
  dayjs.locale("en-gb");
  return (
    <>
      <CssBaseline />
      <SnackbarProvider
        autoHideDuration={10000}
        anchorOrigin={{ vertical: "bottom", horizontal: "left" }}
      >
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
                <Route
                  path={MY_SCHEDULE_PAGE_PATH}
                  element={<MySchedulePage />}
                />
                <Route
                  path={PLAN_APPOINTMENT_PAGE_PATH}
                  element={<PlanAppointmentPage />}
                />
                <Route
                  path={MY_APPOINTMENTS_PAGE_PATH}
                  element={<MyAppointmentsPage />}
                />
                <Route
                  path={EXPLORE_CLEANINGS_PAGE_PATH}
                  element={<ExploreCleaningRequestsPage />}
                />
                <Route
                  path={MY_CLEANINGS_PAGE_PATH}
                  element={<MyCleaningRequestsPage />}
                />
                <Route path="*" element={<Navigate to="/" replace />} />
              </Routes>
            </BrowserRouter>
          </PersistGate>
        </Provider>
      </SnackbarProvider>
    </>
  );
}

export default App;
