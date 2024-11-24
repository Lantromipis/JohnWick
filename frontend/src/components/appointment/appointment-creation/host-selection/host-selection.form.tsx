import { FC, memo, useEffect } from "react";
import {
  FormControl,
  FormControlLabel,
  Radio,
  RadioGroup,
  Stack,
  Typography,
  useTheme,
} from "@mui/material";
import { Controller, SubmitHandler, useForm } from "react-hook-form";
import { HostSelectionFormModel } from "../../../../models/schedule.model.ts";
import { HOST_SELECTION_FORM_ID } from "../../../../constants/form.constants.ts";
import { UserDtoModel } from "../../../../models/user.model.ts";

type HostListContainerProps = {
  hosts: UserDtoModel[];
  onSubmit: SubmitHandler<HostSelectionFormModel>;
};

const HostSelectionForm: FC<HostListContainerProps> = ({ hosts, onSubmit }) => {
  const theme = useTheme();

  const { control, handleSubmit, setValue, getValues } =
    useForm<HostSelectionFormModel>({
      mode: "onBlur",
      reValidateMode: "onBlur",
      defaultValues: {
        hostId: hosts.length > 0 ? hosts[0].id : "",
      },
    });

  useEffect(() => {
    if (hosts.length > 0 && getValues().hostId === "" && hosts[0].id) {
      setValue("hostId", hosts[0].id);
    }
  }, [getValues, hosts, setValue]);

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      noValidate
      id={HOST_SELECTION_FORM_ID}
    >
      <Stack direction={"column"} spacing={3} sx={{ minWidth: "500px" }}>
        <Controller
          name="hostId"
          control={control}
          rules={{
            required: "host must be selected!",
          }}
          render={({ field }) => (
            <FormControl>
              <RadioGroup {...field}>
                {hosts?.map((host) => (
                  <FormControlLabel
                    key={host.id}
                    control={<Radio />}
                    value={host.id}
                    label={
                      <Stack>
                        <Typography
                          sx={{ color: theme.palette.text.secondary }}
                        >
                          {host.displayName}
                        </Typography>
                      </Stack>
                    }
                  />
                ))}
              </RadioGroup>
            </FormControl>
          )}
        />
      </Stack>
    </form>
  );
};

export default memo(HostSelectionForm);
