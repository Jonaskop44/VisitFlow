import { useMemo } from "react";
import dayjs from "dayjs";
import { AvailabilityData } from "@/types/availabilityData.types";

const dayOfWeekMap: Record<string, number> = {
  SUNDAY: 0,
  MONDAY: 1,
  TUESDAY: 2,
  WEDNESDAY: 3,
  THURSDAY: 4,
  FRIDAY: 5,
  SATURDAY: 6,
};

export const useCalendarData = (availabilityData: AvailabilityData) => {
  const minMaxTimes = useMemo(() => {
    const startTimes = availabilityData.workSchedules.map((ws) => ws.startTime);
    const endTimes = availabilityData.workSchedules.map((ws) => ws.endTime);

    const minStart = startTimes.reduce((min, current) =>
      min < current ? min : current
    );
    const maxEnd = endTimes.reduce((max, current) =>
      max > current ? max : current
    );

    return {
      minStartTime: minStart,
      maxEndTime: maxEnd,
    };
  }, [availabilityData]);

  const weekendsEnabled = useMemo(() => {
    return availabilityData.workSchedules.some(
      (ws) => ws.dayOfWeek === "SATURDAY" || ws.dayOfWeek === "SUNDAY"
    );
  }, [availabilityData]);

  const businessHours = useMemo(
    () =>
      availabilityData.workSchedules.map((schedule) => ({
        daysOfWeek: [dayOfWeekMap[schedule.dayOfWeek]],
        startTime: schedule.startTime,
        endTime: schedule.endTime,
      })),
    [availabilityData]
  );

  const orders = useMemo(
    () =>
      availabilityData.orders.map((order) => {
        const start = dayjs(order.requestedDateTime);
        const end = start.add(order.product.duration, "minute");

        return {
          title: order.product.name,
          start: start.toISOString(),
          end: end.toISOString(),
          backgroundColor: "#f87171",
          borderColor: "#f87171",
        };
      }),
    [availabilityData]
  );

  return {
    ...minMaxTimes,
    weekendsEnabled,
    businessHours,
    orders,
  };
};
