import { FC, memo, useCallback, useEffect, useState } from "react";
import { orderApi } from "../../../store/order/order.api.ts";
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
import { ORDER_SELECT_EXECUTOR_FORM_ID } from "../../../constants/form.constants.ts";
import OrderSelectExecutorForm from "./order-select-executor.form.tsx";
import {
  OrderSelectExecutorFormModel,
  OrderType,
} from "../../../models/order.model.ts";

type OrderSelectExecutorContainerProps = {
  orderId: string;
  open: boolean;
  onClose: () => void;
};

const OrderSelectExecutorContainer: FC<OrderSelectExecutorContainerProps> = ({
  orderId,
  open,
  onClose,
}) => {
  const [getRegularOrder, getRegularOrderResponse] =
    orderApi.useLazyGetRegularOrderQuery();
  const [selectError, setSelectError] = useState<boolean>(false);
  const [pathOrder, pathOrderResponse] = orderApi.usePatchOrderMutation();

  useEffect(() => {
    if (orderId.length !== 0) {
      getRegularOrder(orderId);
    }
  }, [getRegularOrder, orderId]);

  const handleSubmit: SubmitHandler<OrderSelectExecutorFormModel> = useCallback(
    (formData) => {
      setSelectError(false);
      pathOrder({
        id: orderId,
        type: OrderType.REGULAR,
        assignee: {
          id: formData.selectedKillerId,
        },
      })
        .unwrap()
        .then(() => {
          onClose();
        })
        .catch(() => {
          setSelectError(true);
        });
    },
    [onClose, orderId, pathOrder],
  );

  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle>Select executor for order</DialogTitle>
      <DialogContent>
        <Stack spacing={2} sx={{ paddingTop: "10px" }}>
          {selectError && (
            <Alert severity="error">
              Failed to select executor for order. Please try again.
            </Alert>
          )}
          {(getRegularOrderResponse.data?.applications?.length === 0 ||
            getRegularOrderResponse.error) && (
            <Alert severity="warning">
              There are no applications for this order yet. Please try again
              later.
            </Alert>
          )}
          <OrderSelectExecutorForm
            onSubmit={handleSubmit}
            applications={getRegularOrderResponse.data?.applications ?? []}
          />
        </Stack>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} disabled={pathOrderResponse.isLoading}>
          Cancel
        </Button>
        <Button
          type="submit"
          variant="contained"
          form={ORDER_SELECT_EXECUTOR_FORM_ID}
          disabled={pathOrderResponse.isLoading}
        >
          Select executor
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default memo(OrderSelectExecutorContainer);
