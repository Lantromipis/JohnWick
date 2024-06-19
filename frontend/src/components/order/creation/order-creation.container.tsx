import { FC, memo, useCallback, useState } from "react";
import { userApi } from "../../../store/user/user.api.ts";
import { SubmitHandler } from "react-hook-form";
import {
  Alert,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Stack,
} from "@mui/material";
import { PostAdd } from "@mui/icons-material";
import { ORDER_CREATION_FORM_ID } from "../../../constants/form.constants.ts";
import {
  HeadHuntOrderDto,
  OrderCreationFormModel,
  OrderDtoModel,
  OrderType,
  PromissoryNoteOrderDto,
  RegularOrderDto,
} from "../../../models/order.model.ts";
import OrderCreationFrom from "./order-creation.from.tsx";
import { UserDtoModel, UserRole } from "../../../models/user.model.ts";
import { orderApi } from "../../../store/order/order.api.ts";

type OrderCreationContainerProps = {};

const OrderCreationContainer: FC<OrderCreationContainerProps> = () => {
  const [creationError, setCreationError] = useState<boolean>(false);
  const [dialogOpen, setDialogOpen] = useState<boolean>(false);
  const [createNewOrder, createNewOrderResponse] =
    orderApi.useCreateNewOrderMutation();

  const { data: users } = userApi.useGetUsersQuery();

  const handleDialogOpen = () => {
    setCreationError(false);
    setDialogOpen(true);
  };

  const handleDialogClose = () => {
    setCreationError(false);
    setDialogOpen(false);
  };

  const handleSubmit: SubmitHandler<OrderCreationFormModel> = useCallback(
    (formData) => {
      setCreationError(false);

      let newOrder: OrderDtoModel;
      switch (formData.type) {
        case OrderType.REGULAR:
          newOrder = {
            type: formData.type,
            description: formData.description,
            target: formData.target,
            price: formData.price,
            customer: formData.customer,
          } as RegularOrderDto;
          break;
        case OrderType.PROMISSORY_NOTE:
          newOrder = {
            type: formData.type,
            description: formData.description,
            target: formData.target,
            price: formData.price,
            customer: formData.customer,
            assignee: {
              displayName: "",
              username: formData.debtorUsername,
              role: UserRole.KILLER,
            },
            beneficiary: {
              displayName: "",
              username: formData.beneficiaryUsername,
              role: UserRole.KILLER,
            },
          } as PromissoryNoteOrderDto;
          break;
        case OrderType.HEAD_HUNT:
          newOrder = {
            type: OrderType.HEAD_HUNT,
            description: formData.description,
            target: formData.target,
            price: formData.price,
            customer: formData.customer,
          } as HeadHuntOrderDto;
          break;
      }

      createNewOrder(newOrder)
        .unwrap()
        .then(() => {
          handleDialogClose();
        })
        .catch(() => {
          setCreationError(true);
        });
    },
    [],
  );

  return (
    <>
      <Button
        variant="outlined"
        onClick={handleDialogOpen}
        startIcon={<PostAdd />}
        sx={{ width: "250px" }}
      >
        Create new order
      </Button>
      <Dialog open={dialogOpen} onClose={handleDialogClose}>
        <DialogTitle>Create new user</DialogTitle>
        <DialogContent>
          <Stack spacing={2} sx={{ paddingTop: "10px" }}>
            {creationError && (
              <Alert severity="error">
                Failed to create order. Please try again.
              </Alert>
            )}
            <OrderCreationFrom
              onSubmit={handleSubmit}
              killers={
                users
                  ? users.filter(
                      (u: UserDtoModel) => u.role === UserRole.KILLER,
                    )
                  : []
              }
            />
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button
            onClick={handleDialogClose}
            disabled={createNewOrderResponse.isLoading}
          >
            Cancel
          </Button>
          <Button
            type="submit"
            variant="contained"
            form={ORDER_CREATION_FORM_ID}
            disabled={createNewOrderResponse.isLoading}
          >
            Create
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
};

export default memo(OrderCreationContainer);
