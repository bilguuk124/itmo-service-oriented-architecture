import axios from "axios";
import { parseString } from 'xml2js';
import { FilteringInfo, House, PaginationInfo, SortingInfo } from "../types";
import { parseXml, genXml, buildSortingParams, buildFilteringParams } from "../utils";
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
                pageNumber: pagintion?.pageNumber,
                pageSize: pagintion?.pageSize,
                sort: sorting ? buildSortingParams(sorting) : undefined,
                filter: filtering ? buildFilteringParams(filtering) : undefined
            },
            headers: {
                'Content-Type': 'application/xml',
            },
        })
        if (headers["content-type"] === 'application/xml' || headers["Content-Type"] === 'application/xml') {
            var houses = parseXml(data, 'houses').house
            if (houses === undefined)
                return []
            if (houses?.length === undefined)
                houses = [houses]
            return houses
        }
        return data
    },

    async create(data: House) {
        return await axios.post('/houses', genXml(data, 'house'), { headers: { 'Content-Type': 'application/xml' } })
    }
}

const mapToHouse = (resp: any): House[] => {
    return (resp.map((container: any) => {
        // console.log(container.house)
        let house = container.house
        return (
            {
                name: house.name,
                year: house.year,
                numberOfFloors: house.numberOfFloors
            }
        )
    }))
}

