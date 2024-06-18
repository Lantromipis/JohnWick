import { FC, memo } from "react";
import { SubmitHandler } from "react-hook-form";
import { UserChangePasswordFormModel } from "../../../models/user.model.ts";

type OrderSelectExecutorComponentProps = {
  onSubmit: SubmitHandler<UserChangePasswordFormModel>;
};

const OrderSelectExecutorForm: FC<OrderSelectExecutorComponentProps> = () => {
  return <div>OrderSelectExecutorComponent</div>;
};

export default memo(OrderSelectExecutorForm);
