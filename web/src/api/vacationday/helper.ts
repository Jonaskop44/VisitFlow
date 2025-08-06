import axios from "axios";
import { VacationDay } from "@/types/vacationday.types";

export class Helper {
  constructor() {}

  async createVacationDay(vacationDay: VacationDay, companyId: string) {
    return axios
      .post(`vacation-day/${companyId}/create`, {
        data: vacationDay.date,
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

  async updateVacationDay(vacationDay: VacationDay) {
    return axios
      .patch(`vacation-day/${vacationDay.id}/update`, {
        data: vacationDay.date,
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

  async deleteVacationDay(vacationDayId: string) {
    return axios
      .delete(`vacation-day/${vacationDayId}/delete`)
      .then((response) => {
        if (response.status !== 200) return { data: null, status: false };

        const data = response.data;
        return { data: data, status: true };
      })
      .catch(() => {
        return { data: null, status: false };
      });
  }

  async getAllVacationDays(companyId: string) {
    return axios
      .get(`vacation-day/${companyId}/all`)
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
