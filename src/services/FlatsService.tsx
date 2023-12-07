import axios from "axios";
import { parseString } from 'xml2js';
import Flat from "../model/Flat";

axios.defaults.baseURL = "http://localhost:9000"

export const FlatService = {
    async getAll() {
        const { data, headers } = await axios.get("/flats", {
            headers: {
                'Content-Type': 'application/xml',
            },
        })
        let jsonData = data
        if (headers["Content-Type"] === 'application/xml') {
            parseString(data, { explicitArray: false }, (err: any, result: any) => {
                if (err) {
                    throw err
                }
                jsonData = result
            })
        }
        console.log(jsonData)
        return jsonData
    }
}