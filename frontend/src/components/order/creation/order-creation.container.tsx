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
  OrderCreationFormModel,
  OrderDtoModel,
  OrderStatus,
  OrderType,
} from "../../../models/order.model.ts";
import OrderCreationFrom from "./order-creation.from.tsx";
import { orderApi } from "../../../store/order/order.api.ts";

type OrderCreationContainerProps = {};

const OrderCreationContainer: FC<OrderCreationContainerProps> = () => {
  const [creationError, setCreationError] = useState<boolean>(false);
  const [dialogOpen, setDialogOpen] = useState<boolean>(false);
  const [createNewOrder, createNewOrderResponse] =
    orderApi.useCreateNewOrderMutation();

  const { data: users } = userApi.useListUsersQuery({});

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
            type: OrderType.REGULAR,
            status: OrderStatus.CREATED,
            description: formData.description,
            targetName: formData.target,
            price: formData.price,
            customerName: formData.customer,
          };
          break;
        case OrderType.PROMISSORY_NOTE:
          newOrder = {
            type: OrderType.PROMISSORY_NOTE,
            status: OrderStatus.CREATED,
            description: formData.description,
            targetName: formData.target,
            debtor: {
              id: formData.debtorId,
            },
            beneficiary: {
              id: formData.beneficiaryId,
            },
          };
          break;
        case OrderType.HEAD_HUNT:
          newOrder = {
            type: OrderType.HEAD_HUNT,
            status: OrderStatus.CREATED,
            description: formData.description,
            targetName: formData.target,
            currentPrice: formData.price,
            customerName: formData.customer,
          };
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
        <DialogTitle>Create new order</DialogTitle>
        <DialogContent>
          <Stack spacing={2} sx={{ paddingTop: "10px" }}>
            {creationError && (
              <Alert severity="error">
                Failed to create order. Please try again.
              </Alert>
            )}
            <OrderCreationFrom onSubmit={handleSubmit} killers={users ?? []} />
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
