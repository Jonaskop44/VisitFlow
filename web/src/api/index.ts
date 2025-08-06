import axios from "axios";
import { Company } from "./company";
import { Customer } from "./customer";
import { Invoice } from "./invoice";
import { Order } from "./order";
import { Pdf } from "./pdf";
import { Product } from "./product";
import { VacationDay } from "./vacationday";
import { WorkSchedule } from "./workschedule";
import { useUserStore } from "@/data/user-store";

export default class ApiClient {
  company: Company;
  customer: Customer;
  invoice: Invoice;
  order: Order;
  pdf: Pdf;
  product: Product;
  vacationDay: VacationDay;
  workSchedule: WorkSchedule;
  constructor() {
    this.company = new Company();
    this.customer = new Customer();
    this.invoice = new Invoice();
    this.order = new Order();
    this.pdf = new Pdf();
    this.product = new Product();
    this.vacationDay = new VacationDay();
    this.workSchedule = new WorkSchedule();

    axios.defaults.baseURL = "http://127.0.0.1:4000/api/v1/";
  }
}
