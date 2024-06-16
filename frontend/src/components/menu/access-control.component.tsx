import { FC, memo, ReactNode } from "react";

type AccessControlComponentProps = {
  role: string;
  showFor: string;
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
