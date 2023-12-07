import axios from "axios";
import { parseString } from 'xml2js';
import Flat from "../model/Flat";

axios.defaults.baseURL = "http://localhost:9000"

export const FlatService = {
    async getAll() {
        const { data, headers } = await axios.get("http://localhost:8080/api/flats/", {
            headers: {
                'Content-Type': 'application/xml',
            },
        })
        if (headers["content-type"] === 'application/xml') {
            return parseXml(data)
        }
        return data
    }
}

const mapRespToFlat = (resp: any) => {
    return resp.map((container: any) => {
        let flat = container.flat
        return ({
            id: flat.$.id,
            name: flat.name,
            coordinates: {
                x: flat.coordinates.coordinate_x,
                y: flat.coordinates.coordinate_y
            },
            creationDate: flat.creationDate,
            area: flat.area,
            roomsNumber: flat.numberOfRooms,
            furnish: flat.furnish,
            view: flat.view,
            transport: flat.transport,
            price: flat.price,
            hasBalcony: flat.hasBalcony,
            house: {
                name: flat.house.name,
                year: flat.house.yeer,
                numberOfFloors: flat.house.numberOfFloors
            }
        })
    })
}

const parseXml = (xmlReq: any) => {
    let res: any
    parseString(xmlReq, { explicitArray: false }, (err: any, result: any) => {
        if (err) {
            throw err
        }
        res = result
    })
    if (!(res instanceof Array))
        res = [res]
    return mapRespToFlat(res)
}
