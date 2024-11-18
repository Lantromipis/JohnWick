import { FC, memo, useState } from "react";
import { UserDtoModel } from "../../../models/user.model.ts";
import {
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from "@mui/material";
import { userRoleToLabel } from "../../../utils/user-utils.ts";
import UserChangePasswordContainer from "../change-password/user-change-password.container.tsx";

type UserListComponentProps = {
  users: UserDtoModel[];
};

const UserListComponent: FC<UserListComponentProps> = ({ users }) => {
  const [currentChangePasswordUser, setCurrentChangePasswordUser] =
    useState<UserDtoModel>({});
  const [changePasswordDialogOpen, setChangePasswordDialogOpen] =
    useState<boolean>(false);

  const handleChangePasswordDialogOpen = () => {
    setChangePasswordDialogOpen(true);
  };

  const handleChangePasswordDialogClose = () => {
    setChangePasswordDialogOpen(false);
  };

  return (
    <>
      <TableContainer>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Username</TableCell>
              <TableCell>Display name</TableCell>
              <TableCell>Role</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {users.map((user) => (
              <TableRow key={user.id}>
                <TableCell>{user.username}</TableCell>
                <TableCell>{user.displayName}</TableCell>
                <TableCell>{userRoleToLabel(user?.role)}</TableCell>
                <TableCell>
                  <Button
                    variant="outlined"
                    onClick={() => {
                      setCurrentChangePasswordUser(user);
                      handleChangePasswordDialogOpen();
                    }}
                  >
                    Change password
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
      <UserChangePasswordContainer
        userId={currentChangePasswordUser.id}
        username={currentChangePasswordUser.username}
        onClose={handleChangePasswordDialogClose}
        open={changePasswordDialogOpen}
      />
    </>
  );
};

export default memo(UserListComponent);
