import axios from "axios";
import Flat from "../model/Flat";

axios.defaults.baseURL = "http://localhost:9000"

export const FlatService = {
    async getAll(){
        return axios.get<Flat[]>("/flats")
    }
}