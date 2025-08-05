import { Address } from "./address.types";

export interface Company {
  id: string;
  name: string;
  description: string;
  domain: string;
  userId: string;
  createdAt: Date;
  updatedAt: Date;
  address: Address;
}
