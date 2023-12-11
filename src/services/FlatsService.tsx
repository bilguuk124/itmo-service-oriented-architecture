import axios from "axios";
import { FlatCreate } from "../model/Flat";
import { parseXml, genXml } from "../utils";
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
            let obj = parseXml(data)
            console.log('new sata received /api/flats')
            if (obj.flats === '')
                obj = []
            console.log(obj)
            return mapRespToFlat(obj)
        }
        return data
    },

    async create(data: FlatCreate) {
        return await axios.post('/flats', genXml(data, 'newFlatRequest'), { headers: { 'Content-Type': 'application/xml' } })
    }


}

const mapRespToFlat = (resp: any) => {
    return resp.map((container: any) => {
        let flat = container.flat
        return ({
            id: flat.$.id,
            coordinates: {
                x: flat.coordinates.coordinate_x,
                y: flat.coordinates.coordinate_y
            },
            creationDate: flat.creationDate,
            house:{
                year: flat.house.yeer,
                ...flat.house
            },
            ...flat
        })
    })
}


