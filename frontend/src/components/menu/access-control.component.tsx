import { FC, memo, ReactNode } from "react";
import { UserRole } from "../../models/user.model.ts";

type AccessControlComponentProps = {
  role?: UserRole;
  showFor: UserRole | UserRole[];
  children: ReactNode;
};

const AccessControlComponent: FC<AccessControlComponentProps> = ({
  role,
  showFor,
  children,
}) => {
  let allowedToShow = false;
  if (Array.isArray(showFor)) {
    allowedToShow = !!showFor.find((showForRole) => showForRole === role);
  } else {
    allowedToShow = role === showFor;
  }
  return allowedToShow ? children : null;
};

export default memo(AccessControlComponent);
