import { Product } from "./product.types";

export interface AvailabilityData {
  orders: { requestedDateTime: string; product: Product }[];
  vacationDays: { date: string }[];
  workSchedules: {
    dayOfWeek: string;
    maxOrdersPerDay: number;
    minMinutesBetweenOrders: number;
    startTime: string;
    endTime: string;
  }[];
}
