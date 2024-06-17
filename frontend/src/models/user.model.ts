export enum UserRole {
  ADMIN = "ADMIN",
  KILLER = "KILLER",
  TAILOR = "TAILOR",
  SOMMELIER = "SOMMELIER",
  CLEANER = "CLEANER",
}

// form
export interface UserLoginFormModel {
  username: string;
  password: string;
}

export interface UserCreateFormModel {
  displayName: string;
  username: string;
  password: string;
  retypedPassword: string;
  role: UserRole;
}

export interface UserChangePasswordFormModel {
  password: string;
  retypedPassword: string;
}

// dto
export interface UserDtoModel {
  displayName: string;
  username: string;
  role: UserRole;
}

export interface UserWithPasswordDtoModel extends UserDtoModel {
  password: string;
}

export interface UserChangePasswordDtoModel {
  username: string;
  password: string;
}

// state
export interface CurrentUserStateModel extends UserDtoModel {}

export interface UsersStateModel {
  users: UserDtoModel[];
}
