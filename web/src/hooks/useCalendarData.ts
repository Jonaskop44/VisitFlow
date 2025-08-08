import { useCallback, useMemo } from "react";
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

        const dayOfWeek = start.day();
        const schedule = availabilityData.workSchedules.find(
          (ws) => dayOfWeekMap[ws.dayOfWeek] === dayOfWeek
        );

        const minGap = schedule?.minMinutesBetweenOrders || 0;
        const end = start
          .add(order.product.duration, "minute")
          .add(minGap, "minute");

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

  const vacationDaysSet = useMemo(() => {
    return new Set(
      availabilityData.vacationDays.map((vac) =>
        dayjs(vac.date).format("YYYY-MM-DD")
      )
    );
  }, [availabilityData]);

  const selectAllow = useCallback(
    (selectInfo: { start: Date; end: Date }) => {
      const selectStart = dayjs(selectInfo.start);
      const selectEnd = dayjs(selectInfo.end);

      //No appointments allowed on vacation days
      if (vacationDaysSet.has(selectStart.format("YYYY-MM-DD"))) {
        return false;
      }

      // Selection must be within working hours on weekdays.
      const dayOfWeek = selectStart.day();
      const schedule = availabilityData.workSchedules.find(
        (ws) => dayOfWeekMap[ws.dayOfWeek] === dayOfWeek
      );
      if (!schedule) {
        return false;
      }

      // Working hours start/end as dayjs on the same day
      const workStart = dayjs(
        selectStart.format("YYYY-MM-DD") + "T" + schedule.startTime
      );
      const workEnd = dayjs(
        selectStart.format("YYYY-MM-DD") + "T" + schedule.endTime
      );

      if (selectStart.isBefore(workStart) || selectEnd.isAfter(workEnd)) {
        return false;
      }

      //No overlap with existing appointments (orders)
      const hasConflict = orders.some((event) => {
        const eventStart = dayjs(event.start);
        const eventEnd = dayjs(event.end);

        return selectStart.isBefore(eventEnd) && selectEnd.isAfter(eventStart);
      });

      if (hasConflict) return false;

      return true;
    },
    [availabilityData.workSchedules, orders, vacationDaysSet]
  );

  return {
    ...minMaxTimes,
    weekendsEnabled,
    businessHours,
    orders,
    selectAllow,
  };
};
