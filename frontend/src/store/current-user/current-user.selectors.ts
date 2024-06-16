import { UserStateModel } from "../../models/user.model.ts";
import { RootState } from "../store.tsx";

const selectCurrentUser: (state: RootState) => UserStateModel = (
  state: RootState,
) => state.currentUser;

export const selectCurrentUserRole = (state: RootState) =>
  selectCurrentUser(state).role;
