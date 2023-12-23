import axios from "axios";
import Flat, { FilteringInfo, FlatBackend, PageableResponse, PaginationInfo, SortingInfo } from "../types";
import { parseXml, genXml, buildSortingParams, buildFilteringParams } from "../utils";
// axios.defaults.baseURL = "http://localhost:9000"
axios.defaults.baseURL = "http://localhost:8080/api"

export const FlatService = {
    async getAll(pagintion?: PaginationInfo, filtering?: FilteringInfo<FlatBackend>, sorting?: SortingInfo<FlatBackend>) {
        const { data, headers } = await axios.get("/flats", {
            params: {
                page: pagintion?.page! + 1,
                pageSize: pagintion?.pageSize,
                sort: sorting ? buildSortingParams(sorting) : undefined,
                filter: filtering ? buildFilteringParams(filtering) : undefined
            },
            headers: {
                'Content-Type': 'application/xml',
            },
        })
        if (headers["content-type"] === 'application/xml' || headers["Content-Type"] === 'application/xml') {
            let resp = parseXml(data).PageableResponse
            if (resp.data != undefined && resp.data.flat != undefined && resp.data.flat.length == undefined)
                resp.data.flat = [resp.data.flat]
            if (resp.data.flat == undefined)
                return { data: [], numberOfEntries: Number(resp.numberOfEntries) } as PageableResponse<Flat>
            const result = { data: resp.data ? mapRespToFlat(resp.data.flat) : [], numberOfEntries: Number(resp.numberOfEntries) } as PageableResponse<Flat>
            console.log(result)
            return result
        }
        return data
    },

    async create(flat: FlatBackend) {
        return await axios.post('/flats', genXml(flat, 'newFlatRequest'), { headers: { 'Content-Type': 'application/xml' } })
    },

    async delete(flatId: number) {
        return await axios.delete(`/flats/${flatId}`)
    },

    async update(flat: Flat) {
        var res = (await axios.put(`/flats/${flat.id}`, genXml(flat, 'flat'), { headers: { 'Content-Type': 'application/xml' } })).data
        console.log(res);
        res = mapRespToFlat(parseXml(res))
        return res
    }

}

const mapRespToFlat = (resp: any): Flat[] => {
    return resp.map((container: any) => {
        let flat = container
        console.log(flat);

        return (
            {
                id: flat.$.id,
                name: flat.name,
                coordinates: {
                    x: flat.coordinates.coordinate_x,
                    y: flat.coordinates.coordinate_y
                },
                creationDate: new Date(flat.creationDate),
                area: flat.area,
                numberOfRooms: flat.numberOfRooms,
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
            } as Flat
        )
    })
}


