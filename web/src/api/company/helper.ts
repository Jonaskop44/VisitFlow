import { Company } from "@/types/company.types";
import axios from "axios";

export class Helper {
  constructor() {}

  async getCompanyInfo(companyId: string) {
    return axios
      .get(`company/${companyId}/info`)
      .then((response) => {
        if (response.status !== 200) return { data: null, status: false };

        const data = response.data;
        return { data: data, status: true };
      })
      .catch(() => {
        return { data: null, status: false };
      });
  }

  async getAllCompanies() {
    return axios
      .get("company")
      .then((response) => {
        if (response.status !== 200) return { data: null, status: false };

        const data = response.data;
        return { data: data, status: true };
      })
      .catch(() => {
        return { data: null, status: false };
      });
  }

  async createCompany(company: Company) {
    return axios
      .post("company/create", {
        name: company.name,
        description: company.description,
        domain: company.domain,
        street: company.address.street,
        city: company.address.city,
        postalCode: company.address.postalCode,
        country: company.address.country,
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

  async updateCompany(company: Company) {
    return axios
      .patch(`company/${company.id}/update`, {
        name: company.name,
        description: company.description,
        domain: company.domain,
        street: company.address.street,
        city: company.address.city,
        postalCode: company.address.postalCode,
        country: company.address.country,
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

  async deleteCompany(companyId: string) {
    return axios
      .delete(`company/${companyId}/delete`)
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
