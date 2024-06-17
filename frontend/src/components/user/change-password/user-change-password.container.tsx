import { FC, memo, useCallback, useState } from "react";
import { userApi } from "../../../store/user/user.api.ts";
import { SubmitHandler } from "react-hook-form";
import { UserChangePasswordFormModel } from "../../../models/user.model.ts";
import {
  Alert,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Stack,
} from "@mui/material";
import { USER_CHANGE_PASSWORD_FORM_ID } from "../../../constants/form.constants.ts";
import UserChangePasswordForm from "./user-change-password.form.tsx";

type UserChangePasswordContainerProps = {
  username: string;
  open: boolean;
  onClose: () => void;
};

const UserChangePasswordContainer: FC<UserChangePasswordContainerProps> = ({
  username,
  open,
  onClose,
}) => {
  const [changeError, setChangeError] = useState<boolean>(false);
  const [changeUserPassword, changeUserPasswordResponse] =
    userApi.useChangeUserPasswordMutation();

  const handleSubmit: SubmitHandler<UserChangePasswordFormModel> = useCallback(
    (formData) => {
      setChangeError(false);
      changeUserPassword({
        password: formData.password,
        username: username,
      })
        .unwrap()
        .then(() => {
          onClose();
        })
        .catch(() => {
          setChangeError(true);
        });
    },
    [username],
  );

  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle>
        Change password for user <strong>{username}</strong>
      </DialogTitle>
      <DialogContent>
        <Stack spacing={2} sx={{ paddingTop: "10px" }}>
          {changeError && (
            <Alert severity="error">
              Failed to change password. Please try again.
            </Alert>
          )}
          <UserChangePasswordForm onSubmit={handleSubmit} />
        </Stack>
      </DialogContent>
      <DialogActions>
        <Button
          onClick={onClose}
          disabled={changeUserPasswordResponse.isLoading}
        >
          Cancel
        </Button>
        <Button
          type="submit"
          variant="contained"
          form={USER_CHANGE_PASSWORD_FORM_ID}
          disabled={changeUserPasswordResponse.isLoading}
        >
          Change password
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default memo(UserChangePasswordContainer);
