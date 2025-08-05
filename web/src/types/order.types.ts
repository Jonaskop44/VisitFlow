import { Address } from "./address.types";

enum OrderStatus {
  REQUESTED,
  CONFIRMED,
  CANCELLED,
}

export interface Order {
  id: number;
  requestedDateTime: Date;
  note: string;
  status: OrderStatus;
  createdAt: Date;
  updatedAt: Date;
  address: Address;
}
