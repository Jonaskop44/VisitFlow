import { Product } from "@/types/product.types";
import axios from "axios";

export class Helper {
  constructor() {}

  async createProduct(product: Product, companyId: string) {
    axios
      .post(`product/${companyId}/create`, {
        name: product.name,
        price: product.price,
        duration: product.duration,
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

  async updateProduct(product: Product) {
    axios
      .patch(`product/${product.id}/update`, {
        name: product.name,
        price: product.price,
        duration: product.duration,
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

  async deleteProduct(productId: string) {
    return axios
      .delete(`product/${productId}/delete`)
      .then((response) => {
        if (response.status !== 200) return { data: null, status: false };

        const data = response.data;
        return { data: data, status: true };
      })
      .catch(() => {
        return { data: null, status: false };
      });
  }

  async getAllProducts(companyId: string) {
    return axios
      .get(`product/${companyId}`)
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
