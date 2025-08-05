import { Address } from "./address.types";
import { Customer } from "./customer.types";
import { Product } from "./product.types";

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
  customer: Customer;
  product: Product;
}
