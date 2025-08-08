import { OrderFormData } from "@/lib/order-validation";
import { Order } from "@/types/order.types";
import axios from "axios";

export class Helper {
  constructor() {}

  async createOrder(order: OrderFormData, companyId: string) {
    return axios
      .post(`/order/${companyId}/submit`, {
        firstName: order.firstName,
        lastName: order.lastName,
        email: order.email,
        phoneNumber: order.phoneNumber,
        requestedDateTime: order.requestedDateTime,
        note: order.note,
        street: order.street,
        productId: order.productId,
        city: order.city,
        postalCode: order.postalCode,
        country: order.country,
      })
      .then((response) => {
        if (response.status !== 200) return { data: null, status: false };

        const data = response.data;
        return { data: data, status: true };
      })
      .catch((error) => {
        return {
          data: {
            message: error.response.data.message,
          },
          status: false,
        };
      });
  }

  async updateOrderStatus(order: Order) {
    return axios
      .patch(`order/${order.id}/update`, {
        status: order.status,
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

  async getAllOrders(companyId: string, startDate?: Date, endDate?: Date) {
    if (!startDate || !endDate) {
      return axios
        .get(`order/${companyId}/all`)
        .then((response) => {
          if (response.status !== 200) return { data: null, status: false };

          const data = response.data;
          return { data: data, status: true };
        })
        .catch(() => {
          return { data: null, status: false };
        });
    } else {
      return axios
        .get(`order/${companyId}/all`, {
          params: {
            start: startDate.toISOString(),
            end: endDate.toISOString(),
          },
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
  }
}
