import { FC, memo } from "react";
import { Controller, SubmitHandler, useForm } from "react-hook-form";
import { UserCreateFormModel, UserRole } from "../../../models/user.model.ts";
import { USER_CREATION_FORM_ID } from "../../../constants/form.constants.ts";
import {
  FormControl,
  FormHelperText,
  InputLabel,
  MenuItem,
  Select,
  Stack,
  TextField,
} from "@mui/material";
import { userRoleToLabel } from "../../../utils/user-utils.ts";

type CreateUserFormProps = {
  onSubmit: SubmitHandler<UserCreateFormModel>;
};

const roles = [
  UserRole.ADMIN,
  UserRole.KILLER,
  UserRole.SOMMELIER,
  UserRole.TAILOR,
  UserRole.CLEANER,
];

const UserCreationForm: FC<CreateUserFormProps> = ({ onSubmit }) => {
  const {
    control,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm<UserCreateFormModel>({
    mode: "onBlur",
    reValidateMode: "onBlur",
    defaultValues: {
      username: "",
      password: "",
      retypedPassword: "",
      role: UserRole.KILLER,
      displayName: "",
    },
  });

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      noValidate
      id={USER_CREATION_FORM_ID}
    >
      <Stack direction={"column"} spacing={3} sx={{ minWidth: "400px" }}>
        <Controller
          name="displayName"
          control={control}
          rules={{ required: "Display name is required" }}
          render={({ field }) => (
            <TextField
              {...field}
              variant="outlined"
              label="Display name"
              error={!!errors.displayName}
              helperText={errors.displayName?.message}
            />
          )}
        />
        <Controller
          name="username"
          control={control}
          rules={{ required: "Username is required" }}
          render={({ field }) => (
            <TextField
              {...field}
              variant="outlined"
              label="Username"
              error={!!errors.username}
              helperText={errors.username?.message}
            />
          )}
        />
        <Controller
          name="role"
          rules={{ required: "User role is required" }}
          control={control}
          render={({ field }) => (
            <FormControl required error={!!errors.role}>
              <InputLabel id="role-label">Played role</InputLabel>
              <Select
                labelId="role-label"
                {...field}
                label="User role"
                required
                value={field.value}
              >
                {roles.map((role) => (
                  <MenuItem key={role} value={role}>
                    {userRoleToLabel(role)}
                  </MenuItem>
                ))}
              </Select>
              <FormHelperText>
                {!!errors.role ? errors.role.message : ""}
              </FormHelperText>
            </FormControl>
          )}
        />
        <Controller
          name="password"
          control={control}
          rules={{ required: "Password is required" }}
          render={({ field }) => (
            <TextField
              {...field}
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

export default memo(UserCreationForm);
