import { FC, memo, ReactNode } from "react";
import { UserRole } from "../../models/user.model.ts";

type AccessControlComponentProps = {
  role: UserRole;
  showFor: UserRole;
  children: ReactNode;
};

const AccessControlComponent: FC<AccessControlComponentProps> = ({
  role,
  showFor,
  children,
}) => {
  const allowedToShow = role === showFor;
  return allowedToShow ? children : null;
};

export default memo(AccessControlComponent);
