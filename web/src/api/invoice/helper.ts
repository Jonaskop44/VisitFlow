import axios from "axios";

export class Helper {
  constructor() {}

  async createInvoice(orderId: number) {
    return axios
      .post(`invoice/${orderId}/create`)
      .then((response) => {
        if (response.status !== 200) return { data: null, status: false };

        const data = response.data;
        return { data: data, status: true };
      })
      .catch(() => {
        return { data: null, status: false };
      });
  }

  async getAllInvoices(companyId: string) {
    return axios
      .get(`invoice/${companyId}/all`)
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
