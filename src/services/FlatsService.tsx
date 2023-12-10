import axios from "axios";
import { parseString } from 'xml2js';
import * as React from 'react';
import Flat, { FlatCreate } from "../model/Flat";

// axios.defaults.baseURL = "http://localhost:9000"
axios.defaults.baseURL = "http://localhost:8080/api"

export const FlatService = {
    async getAll() {
        const { data, headers } = await axios.get("/flats", {
            headers: {
                'Content-Type': 'application/xml',
            },
        })
        if (headers["content-type"] === 'application/xml' || headers["Content-Type"] === 'application/xml') {
            return parseXml(data)
        }
        return data
    },

    async create(data: FlatCreate) {
        return await axios.post('/flats', data, { headers: { 'Content-Type': 'application/xml' } })
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
    if ((res as []).length === 0)
        return []
    return mapRespToFlat(res)
}
