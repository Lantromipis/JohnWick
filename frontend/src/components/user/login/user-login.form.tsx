import { FC, memo } from "react";
import { Controller, SubmitHandler, useForm } from "react-hook-form";
import { UserLoginFormModel } from "../../../models/user.model.ts";
import { Button, Stack, TextField, Typography } from "@mui/material";
import { USER_LOGIN_FORM_ID } from "../../../constants/form.constants.ts";

type UserLoginFormProps = {
  onSubmit: SubmitHandler<UserLoginFormModel>;
  loading: boolean;
};

const UserLoginForm: FC<UserLoginFormProps> = ({ onSubmit, loading }) => {
  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<UserLoginFormModel>({
    mode: "onBlur",
    reValidateMode: "onBlur",
    defaultValues: {
      username: "",
      password: "",
    },
  });

  return (
    <form onSubmit={handleSubmit(onSubmit)} noValidate id={USER_LOGIN_FORM_ID}>
      <Stack direction={"column"} spacing={2} sx={{ minWidth: "300px" }}>
        <Controller
          name="username"
          control={control}
          rules={{ required: "Username is required" }}
          render={({ field }) => (
            <TextField
              {...field}
              required
              variant="outlined"
              label="Username"
              error={!!errors.username}
              helperText={errors.username?.message}
            />
          )}
        />
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
        <Button
          type="submit"
          variant="contained"
          sx={{ height: 45 }}
          disabled={loading}
        >
          <Typography>Start killing</Typography>
        </Button>
      </Stack>
    </form>
  );
};

export default memo(UserLoginForm);
