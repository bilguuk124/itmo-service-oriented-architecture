import axios from "axios";
import Flat, { FilteringInfo, FlatBackend, PageableResponse, PaginationInfo, SortingInfo } from "../types";
import { parseXml, genXml, buildSortingParams, buildFilteringParams, buildFSPath } from "../utils";

export const FlatService = {
    async getAll(pagintion?: PaginationInfo, filtering?: FilteringInfo<FlatBackend>, sorting?: SortingInfo<FlatBackend>) {
        const { data, headers } = await axios.get(buildFSPath(`/flats`), {
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
            const result = { data: resp.data ? mapRespToFlats(resp.data.flat) : [], numberOfEntries: Number(resp.numberOfEntries) } as PageableResponse<Flat>
            console.log(result)
            return result
        }
        return data
    },

    async create(flat: FlatBackend) {
        return await axios.post(buildFSPath(`/flats`), genXml(flat, 'newFlatRequest'), { headers: { 'Content-Type': 'application/xml' } })
    },

    async delete(flatId: number) {
        return await axios.delete(buildFSPath(`/flats/${flatId}`))
    },

    async update(flat: Flat) {
        var res = (await axios.put(buildFSPath(`/flats/${flat.id}`), genXml(mapToBakendFlat(flat), 'newFlatRequest'), { headers: { 'Content-Type': 'application/xml' } })).data
        console.log(res);
        var a = parseXml(res)
        console.log(a);
        res = mapToFlat(a.flat)
        console.log(res);
        return res
    },

    async deleteAllInHouse(houseName: string) {
        return await axios.delete(buildFSPath(`/flats/${houseName}`))
    },

    async get(flatId: number) {
        return mapToFlat(parseXml((await axios.get(buildFSPath(`/flats/${flatId}`))).data).flat) as Flat
    },

    async isExist(flatId: number) {
        return (await axios.get(buildFSPath(`/flats/${flatId}`))).status == 200
    }

}


export const mapToFlat = (container: any): Flat => {
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
}

export const mapToBakendFlat = (flat: Flat): FlatBackend => {
    return (
        {
            ...flat,
            coordinates: {
                coordinate_x: flat.coordinates.x,
                coordinate_y: flat.coordinates.y
            }
        } as FlatBackend
    )
}

export const mapRespToFlats = (resp: any): Flat[] => {
    return resp.map(mapToFlat)
}


