import { Order } from "@/types/order.types";
import axios from "axios";

export class Helper {
  constructor() {}

  async createOrder(order: Order, companyId: string) {
    axios
      .post(`/order/${companyId}/submit`, {
        firstName: order.customer.firstName,
        lastName: order.customer.lastName,
        email: order.customer.email,
        phoneNumber: order.customer.phoneNumber,
        requestedDateTime: order.requestedDateTime,
        note: order.note,
        street: order.address.street,
        productId: order.product.id,
        city: order.address.city,
        postalCode: order.address.postalCode,
        country: order.address.country,
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

  async updateOrderStatus(order: Order) {
    axios
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

  

}
