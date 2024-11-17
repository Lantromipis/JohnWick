import { FC, memo } from "react";
import { Controller, SubmitHandler, useForm } from "react-hook-form";
import { ORDER_SELECT_EXECUTOR_FORM_ID } from "../../../constants/form.constants.ts";
import {
  FormControl,
  FormHelperText,
  InputLabel,
  MenuItem,
  Select,
  Stack,
} from "@mui/material";
import {
  OrderSelectExecutorFormModel,
  RegularOrderApplicationDto,
} from "../../../models/order.model.ts";

type OrderSelectExecutorComponentProps = {
  onSubmit: SubmitHandler<OrderSelectExecutorFormModel>;
  applications: RegularOrderApplicationDto[];
};

const OrderSelectExecutorForm: FC<OrderSelectExecutorComponentProps> = ({
  onSubmit,
  applications,
}) => {
  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<OrderSelectExecutorFormModel>({
    mode: "onBlur",
    reValidateMode: "onBlur",
    defaultValues: {
      selectedKillerId: "",
    },
  });

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      noValidate
      id={ORDER_SELECT_EXECUTOR_FORM_ID}
    >
      <Stack direction={"column"} spacing={2} sx={{ minWidth: "300px" }}>
        <Controller
          name="selectedKillerId"
          rules={{ required: "Executor is required" }}
          control={control}
          render={({ field }) => (
            <FormControl required error={!!errors.selectedKillerId}>
              <InputLabel id="type-label">Selected executor</InputLabel>
              <Select
                labelId="type-label"
                {...field}
                label="Selected executor"
                required
                value={field.value}
              >
                {applications.map((application) => (
                  <MenuItem key={application.id} value={application.killer.id}>
                    {application.killer.displayName}
                  </MenuItem>
                ))}
              </Select>
              <FormHelperText>
                {!!errors.selectedKillerId
                  ? errors.selectedKillerId.message
                  : ""}
              </FormHelperText>
            </FormControl>
          )}
        />
      </Stack>
    </form>
  );
};

export default memo(OrderSelectExecutorForm);
