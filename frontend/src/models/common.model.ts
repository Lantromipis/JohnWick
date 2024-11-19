export interface ListEntitiesRequest {
  rsqlPredicate?: string;
  limit?: number;
  offset?: number;
}

// dto
export interface ErrorResponseDtoModel {
  code?: number;
  message?: string;
  timestamp?: string;
}
