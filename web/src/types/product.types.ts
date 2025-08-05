export interface Product {
  id: number;
  name: string;
  price: number;
  duration: number;
  stripeProductId: string;
  stripePriceId: string;
  createdAt: Date;
  updatedAt: Date;
}
