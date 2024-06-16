import { combineReducers, configureStore } from "@reduxjs/toolkit";
import { commonApi } from "./common.api.ts";
import { setupListeners } from "@reduxjs/toolkit/query";
import { currentUserReducer as currentUser } from "./current-user/current-user.slice.ts";
import storage from "redux-persist/lib/storage";
import {
  FLUSH,
  PAUSE,
  PERSIST,
  persistReducer,
  persistStore,
  PURGE,
  REGISTER,
  REHYDRATE,
} from "redux-persist";

const persistConfig = {
  key: "root",
  version: 1,
  storage,
  whiteList: ["currentUser"],
};

const persistedReducer = persistReducer(
  persistConfig,
  combineReducers({
    [commonApi.reducerPath]: commonApi.reducer,
    currentUser,
  }),
);

export const store = configureStore({
  reducer: persistedReducer,
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: {
        ignoredActions: [FLUSH, REHYDRATE, PAUSE, PERSIST, PURGE, REGISTER],
      },
    }).concat(commonApi.middleware),
  devTools: process.env.NODE_ENV !== "production",
});

setupListeners(store.dispatch);

export type RootState = ReturnType<typeof store.getState>;
export const persistor = persistStore(store);
