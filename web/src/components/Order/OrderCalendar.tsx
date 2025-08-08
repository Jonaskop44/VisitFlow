import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";
import { AvailabilityData } from "@/types/availabilityData.types";
import { FC } from "react";
import { useCalendarData } from "@/hooks/useCalendarData";
import dayjs from "dayjs";

interface OrderCalendarProps {
  availabilityData: AvailabilityData;
  onDateSelect?: (startDateISO: string) => void;
}

const OrderCalendar: FC<OrderCalendarProps> = ({
  availabilityData,
  onDateSelect,
}) => {
  const {
    minStartTime,
    maxEndTime,
    weekendsEnabled,
    businessHours,
    events,
    selectAllow,
  } = useCalendarData(availabilityData);

  return (
    <div>
      <FullCalendar
        plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin]}
        initialView="timeGridWeek"
        headerToolbar={{
          left: "prev,next today",
          right: "title",
        }}
        weekends={weekendsEnabled}
        selectable={true}
        slotMinTime={minStartTime}
        slotMaxTime={maxEndTime}
        businessHours={businessHours}
        events={events}
        selectAllow={selectAllow}
        select={(selectInfo) => {
          if (onDateSelect) {
            const localISOString = dayjs(selectInfo.start).format(
              "YYYY-MM-DDTHH:mm:ss"
            );
            onDateSelect(localISOString);
          }
        }}
        height="auto"
      />
    </div>
  );
};

export default OrderCalendar;
