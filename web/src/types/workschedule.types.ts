export interface WorkSchedule {
  id: number;
  dayOfWeek: string;
  maxOrdersPerDay: number;
  minMinutesBetweenOrders: number;
  startTime: Date;
  endTime: Date;
  createdAt: Date;
  updatedAt: Date;
}
