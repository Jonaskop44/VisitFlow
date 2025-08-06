import { WorkSchedule } from "@/types/workschedule.types";
import axios from "axios";

export class Helper {
  constructor() {}

  async createWorkSchedule(workSchedule: WorkSchedule, companyId: string) {
    return axios
      .post(`work-schedule/${companyId}/create`, {
        dayOfWeek: workSchedule.dayOfWeek,
        startTime: workSchedule.startTime,
        endTime: workSchedule.endTime,
        maxOrdersPerDay: workSchedule.maxOrdersPerDay,
        minMinutesBetweenOrders: workSchedule.minMinutesBetweenOrders,
      })
      .then((response) => {
        if (response.status !== 200) return { data: null, status: false };

        const data = response.data;
        return { data: data, status: true };
      })
      .catch(() => {
        return { data: null, status: false };
      });
  }

  async updateWorkSchedule(workSchedule: WorkSchedule) {
    return axios
      .patch(`work-schedule/${workSchedule.id}/update`, {
        dayOfWeek: workSchedule.dayOfWeek,
        startTime: workSchedule.startTime,
        endTime: workSchedule.endTime,
        maxOrdersPerDay: workSchedule.maxOrdersPerDay,
        minMinutesBetweenOrders: workSchedule.minMinutesBetweenOrders,
      })
      .then((response) => {
        if (response.status !== 200) return { data: null, status: false };

        const data = response.data;
        return { data: data, status: true };
      })
      .catch(() => {
        return { data: null, status: false };
      });
  }

  async deleteWorkSchedule(workScheduleId: string) {
    return axios
      .delete(`work-schedule/${workScheduleId}/delete`)
      .then((response) => {
        if (response.status !== 200) return { data: null, status: false };

        const data = response.data;
        return { data: data, status: true };
      })
      .catch(() => {
        return { data: null, status: false };
      });
  }

  async getAllWorkSchedules(companyId: string) {
    return axios
      .get(`work-schedule/${companyId}/all`)
      .then((response) => {
        if (response.status !== 200) return { data: null, status: false };

        const data = response.data;
        return { data: data, status: true };
      })
      .catch(() => {
        return { data: null, status: false };
      });
  }
}
