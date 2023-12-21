import axios from "axios";
import { parseString } from 'xml2js';
import { FilteringInfo, House, PageableResponse, PaginationInfo, SortingInfo } from "../types";
import { parseXml, genXml, buildSortingParams, buildFilteringParams } from "../utils";
import { log } from "console";
// axios.defaults.baseURL = "http://localhost:9000"
axios.defaults.baseURL = "http://localhost:8080/api"

axios.interceptors.request.use(request => {
    console.log('Starting Request', JSON.stringify(request, null, 2))
    return request
});
axios.interceptors.response.use(response => {
    console.log('Response:', JSON.stringify(response, null, 2))
    return response
})

export const HouseService = {
    async getAll(pagintion?: PaginationInfo, filtering?: FilteringInfo<House>, sorting?: SortingInfo<House>) {
        const { data, headers } = await axios.get(`/houses`, {
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
            var pageableResp = parseXml(data).PageableResponse
            console.log(pageableResp)
            // if (houses === undefined)
            //     return []
            // if (houses?.length === undefined)
            //     houses = [houses]
            const result = { data: mapToHouse(pageableResp.data.house), numberOfEntries: Number(pageableResp.numberOfEntries) } as PageableResponse<House>
            console.log(result)
            return result
        }
        return
    },

    async create(house: House) {
        return await axios.post('/houses', genXml(house, 'house'), { headers: { 'Content-Type': 'application/xml' } })
    },

    async delete(name: string) {
        return await axios.delete(`/houses/${name}`)
    },

    async update(house: House) {
        var res = (await axios.put(`/houses/${house.name}`, genXml(house, 'house'), { headers: { 'Content-Type': 'application/xml' } })).data
        console.log(res);
        res = parseXml(res).house
        return res
    }
}

const mapToHouse = (resp: any): House[] => {
    return (resp.map((container: any) => {
        // console.log(container.house)
        let house = container
        return (
            {
                name: house.name,
                year: house.year,
                numberOfFloors: house.numberOfFloors
            }
        )
    }))
}

