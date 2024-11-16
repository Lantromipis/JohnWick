import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { CurrentUserStateModel } from "../../models/user.model.ts";

const initialState = {} as CurrentUserStateModel;

export const userSlice = createSlice({
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

export const { setCurrentUser, clearCurrentUser } = userSlice.actions;

export const currentUserReducer = userSlice.reducer;
