import axios from "axios";
import Flat, { FilteringInfo, FlatBackend, PaginationInfo, SortingInfo } from "../types";
import { parseXml, genXml, buildSortingParams, buildFildetingParams } from "../utils";
// axios.defaults.baseURL = "http://localhost:9000"
axios.defaults.baseURL = "http://localhost:8080/api"

export const FlatService = {
    async getAll(pagintion?: PaginationInfo, filtering?: FilteringInfo<FlatBackend>, sorting?: SortingInfo<FlatBackend>) {
        const { data, headers } = await axios.get("/flats", {
            params: {
                pageNumber: pagintion?.pageNumber,
                pageSize: pagintion?.pageSize,
                sort: sorting ? buildSortingParams(sorting) : undefined,
                filter: filtering ? buildFildetingParams(filtering) : undefined
            },
            headers: {
                'Content-Type': 'application/xml',
            },
        })
        if (headers["content-type"] === 'application/xml' || headers["Content-Type"] === 'application/xml') {
            let flatArr = parseXml(data, 'flats')
            if (flatArr === '')
                flatArr = []
            else flatArr = [flatArr]
            const res = mapRespToFlat(flatArr)
            return res
        }
        return data
    },

    async create(data: FlatBackend) {
        return await axios.post('/flats', genXml(data, 'newFlatRequest'), { headers: { 'Content-Type': 'application/xml' } })
    }


}

const mapRespToFlat = (resp: any): Flat[] => {
    return resp.map((container: any) => {
        let flat = container.flat
        return (
            {
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
                    year: flat.house.year,
                    numberOfFloors: flat.house.numberOfFloors
                }
            }
        )
    })
}


