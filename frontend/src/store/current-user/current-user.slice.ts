import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { CurrentUserStateModel } from "../../models/user.model.ts";

const initialState = {} as CurrentUserStateModel;

export const currentUserSlice = createSlice({
  name: "currentUser",
  initialState: initialState,
  reducers: {
    setCurrentUser: (
      state,
      { payload }: PayloadAction<CurrentUserStateModel>,
    ) => {
      return { ...state, ...payload };
    },
    clearCurrentUser: () => initialState,
  },
});

export const { setCurrentUser, clearCurrentUser } = currentUserSlice.actions;

export const currentUserReducer = currentUserSlice.reducer;
