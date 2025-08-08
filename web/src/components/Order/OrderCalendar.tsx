import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";
import { AvailabilityData } from "@/types/availabilityData.types";
import { FC } from "react";
import { useCalendarData } from "@/hooks/useCalendarData";

interface OrderCalendarProps {
  availabilityData: AvailabilityData;
}

const OrderCalendar: FC<OrderCalendarProps> = ({ availabilityData }) => {
  const {
    minStartTime,
    maxEndTime,
    weekendsEnabled,
    businessHours,
    events,
    selectAllow,
  } = useCalendarData(availabilityData);

  return (
    <div className="p-4 bg-white rounded shadow">
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
        height="auto"
      />
    </div>
  );
};

export default OrderCalendar;
