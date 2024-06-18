import { FC, memo } from "react";
import { Stack, TextField } from "@mui/material";
import { Controller, SubmitHandler, useForm } from "react-hook-form";
import { UserChangePasswordFormModel } from "../../../models/user.model.ts";
import { USER_CHANGE_PASSWORD_FORM_ID } from "../../../constants/form.constants.ts";

type UserChangePasswordFormProps = {
  onSubmit: SubmitHandler<UserChangePasswordFormModel>;
};

const UserChangePasswordForm: FC<UserChangePasswordFormProps> = ({
  onSubmit,
}) => {
  const {
    control,
    handleSubmit,
    formState: { errors },
    watch,
  } = useForm<UserChangePasswordFormModel>({
    mode: "onBlur",
    reValidateMode: "onBlur",
    defaultValues: {
      password: "",
      retypedPassword: "",
    },
  });

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      noValidate
      id={USER_CHANGE_PASSWORD_FORM_ID}
    >
      <Stack direction={"column"} spacing={2} sx={{ minWidth: "300px" }}>
        <Controller
          name="password"
          control={control}
          rules={{ required: "Password is required" }}
          render={({ field }) => (
            <TextField
              {...field}
              required
              variant="outlined"
              error={!!errors.password}
              helperText={errors.password?.message}
              label="Password"
              type="password"
            />
          )}
        />
        <Controller
          name="retypedPassword"
          control={control}
          rules={{
            required: "Retype password",
            validate: (value: string) => {
              if (watch("password") != value) {
                return "Passwords does not match!";
              }
            },
          }}
          render={({ field }) => (
            <TextField
              {...field}
              required
              variant="outlined"
              error={!!errors.retypedPassword}
              helperText={errors.retypedPassword?.message}
              label="Rereat rassword"
              type="password"
            />
          )}
        />
      </Stack>
    </form>
  );
};

export default memo(UserChangePasswordForm);
