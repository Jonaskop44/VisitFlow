import axios from "axios";

export class Helper {
  constructor() {}

  async getSummary(companyId: string) {
    return axios
      .get(`statistics/summary/${companyId}`)
      .then((response) => {
        if (response.status !== 200) return { data: null, status: false };

        const data = response.data;
        return { data: data, status: true };
      })
      .catch(() => {
        return { data: null, status: false };
      });
  }

  async getOrderRevenue(companyId: string) {
    return axios
      .get(`statistics/orders-revenue/${companyId}`)
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
