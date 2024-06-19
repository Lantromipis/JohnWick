import { FC, memo } from "react";
import { Controller, SubmitHandler, useForm } from "react-hook-form";
import { ORDER_CREATION_FORM_ID } from "../../../constants/form.constants.ts";
import {
  FormControl,
  FormHelperText,
  InputLabel,
  MenuItem,
  Select,
  Stack,
  TextField,
} from "@mui/material";
import {
  OrderCreationFormModel,
  OrderType,
} from "../../../models/order.model.ts";
import { orderTypeToLabel } from "../../../utils/order-utils.ts";
import { UserDtoModel } from "../../../models/user.model.ts";

type OrderCreationFormProps = {
  onSubmit: SubmitHandler<OrderCreationFormModel>;
  killers: UserDtoModel[];
};

const orderTypes = [
  OrderType.REGULAR,
  OrderType.HEAD_HUNT,
  OrderType.PROMISSORY_NOTE,
];

const OrderCreationForm: FC<OrderCreationFormProps> = ({
  onSubmit,
  killers,
}) => {
  const {
    control,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm<OrderCreationFormModel>({
    mode: "onBlur",
    reValidateMode: "onBlur",
    defaultValues: {
      type: OrderType.REGULAR,
      customer: "",
      target: "",
      description: "",
      price: 0,
      debtorUsername: "",
    },
  });

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      noValidate
      id={ORDER_CREATION_FORM_ID}
    >
      <Stack direction={"column"} spacing={3} sx={{ minWidth: "500px" }}>
        <Controller
          name="type"
          rules={{ required: "Order type is required" }}
          control={control}
          render={({ field }) => (
            <FormControl required error={!!errors.type}>
              <InputLabel id="type-label">Order type</InputLabel>
              <Select
                labelId="type-label"
                {...field}
                label="Order type"
                required
                value={field.value}
              >
                {orderTypes.map((role) => (
                  <MenuItem key={role} value={role}>
                    {orderTypeToLabel(role)}
                  </MenuItem>
                ))}
              </Select>
              <FormHelperText>
                {!!errors.type ? errors.type.message : ""}
              </FormHelperText>
            </FormControl>
          )}
        />
        <Controller
          name="customer"
          control={control}
          rules={{ required: "Customer name is required" }}
          render={({ field }) => (
            <TextField
              {...field}
              required
              variant="outlined"
              label="Customer name"
              error={!!errors.customer}
              helperText={errors.customer?.message}
            />
          )}
        />
        <Controller
          name="target"
          control={control}
          rules={{ required: "Target name is required" }}
          render={({ field }) => (
            <TextField
              {...field}
              required
              variant="outlined"
              label="Target name"
              error={!!errors.target}
              helperText={errors.target?.message}
            />
          )}
        />
        <Controller
          name="description"
          control={control}
          rules={{ required: "Description is required" }}
          render={({ field }) => (
            <TextField
              {...field}
              required
              multiline
              variant="outlined"
              error={!!errors.description}
              helperText={errors.description?.message}
              label="Descrtiption"
            />
          )}
        />
        <Controller
          name="price"
          control={control}
          rules={{ required: "Price is required" }}
          render={({ field }) => (
            <TextField
              {...field}
              required
              type={"number"}
              variant="outlined"
              label="Price"
              error={!!errors.price}
              helperText={errors.price?.message}
            />
          )}
        />
        {watch("type") == OrderType.PROMISSORY_NOTE && (
          <>
            <Controller
              name="beneficiaryUsername"
              rules={{ required: "Beneficiary is required" }}
              control={control}
              render={({ field }) => (
                <FormControl required error={!!errors.beneficiaryUsername}>
                  <InputLabel id="type-label">Beneficiary</InputLabel>
                  <Select
                    labelId="type-label"
                    {...field}
                    label="Beneficiary"
                    required
                    value={field.value}
                  >
                    {killers.map((killer) => (
                      <MenuItem key={killer.username} value={killer.username}>
                        {killer.displayName}
                      </MenuItem>
                    ))}
                  </Select>
                  <FormHelperText>
                    {!!errors.beneficiaryUsername
                      ? errors.beneficiaryUsername.message
                      : ""}
                  </FormHelperText>
                </FormControl>
              )}
            />
            <Controller
              name="debtorUsername"
              rules={{
                required: "Debtor is required",
                validate: (value: string) => {
                  if (watch("beneficiaryUsername") === value) {
                    return "Debtor can not be beneficiary!";
                  }
                },
              }}
              control={control}
              render={({ field }) => (
                <FormControl required error={!!errors.debtorUsername}>
                  <InputLabel id="type-label">Debtor</InputLabel>
                  <Select
                    labelId="type-label"
                    {...field}
                    label="Deptor"
                    required
                    value={field.value}
                  >
                    {killers.map((killer) => (
                      <MenuItem key={killer.username} value={killer.username}>
                        {killer.displayName}
                      </MenuItem>
                    ))}
                  </Select>
                  <FormHelperText>
                    {!!errors.debtorUsername
                      ? errors.debtorUsername.message
                      : ""}
                  </FormHelperText>
                </FormControl>
              )}
            />
          </>
        )}
      </Stack>
    </form>
  );
};

export default memo(OrderCreationForm);
