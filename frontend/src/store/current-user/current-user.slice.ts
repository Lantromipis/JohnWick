import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { UserStateModel } from "../../models/user.model.ts";

const initialState = {} as UserStateModel;

export const currentUserSlice = createSlice({
  name: "currentUser",
  initialState: initialState,
  reducers: {
    setCurrentUser: (state, { payload }: PayloadAction<UserStateModel>) => {
      return { ...state, ...payload };
    },
    clearCurrentUser: () => initialState,
  },
});

export const { setCurrentUser, clearCurrentUser } = currentUserSlice.actions;

export const currentUserReducer = currentUserSlice.reducer;
