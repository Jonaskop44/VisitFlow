import axios from "axios";

export default class ApiClient {
    constructor() {


        axios.defaults.baseURL = "http://127.0.0.1:4000/api/v1/";
    }
}