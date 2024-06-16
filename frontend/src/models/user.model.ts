export interface UserLoginFormModel {
  username: string;
  password: string;
}

export interface UserDtoModel {
  username: string;
  role: string;
}

export interface UserStateModel extends UserDtoModel {}
