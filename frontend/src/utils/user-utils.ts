import { UserRole } from "../models/user.model.ts";

export function userRoleToLabel(role: UserRole): string {
  switch (role) {
    case UserRole.ADMIN:
      return "Admin";
    case UserRole.KILLER:
      return "Killer";
    case UserRole.TAILOR:
      return "Tailor";
    case UserRole.SOMMELIER:
      return "Sommelier";
    case UserRole.CLEANER:
      return "Cleaner";
  }
}
