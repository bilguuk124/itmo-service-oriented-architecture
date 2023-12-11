import axios from "axios";
import { parseString } from 'xml2js';
import { House } from "../model/Flat";
import { parseXml, genXml } from "../utils";
// axios.defaults.baseURL = "http://localhost:9000"
axios.defaults.baseURL = "http://localhost:8080/api"

export const HouseService = {
    async getAll() {
        const { data, headers } = await axios.get("/houses", {
            headers: {
                'Content-Type': 'application/xml',
            },
        })
        if (headers["content-type"] === 'application/xml' || headers["Content-Type"] === 'application/xml') {
            return parseXml(data)
        }
        return data
    },

    async create(data: House) {
        return await axios.post('/houses', genXml(data, 'house'), { headers: { 'Content-Type': 'application/xml' } })
    }


}


