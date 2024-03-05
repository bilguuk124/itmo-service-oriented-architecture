import axios from "axios";
import { parseString } from 'xml2js';
import { FilteringInfo, House, PageableResponse, PaginationInfo, SortingInfo } from "../types";
import { parseXml, genXml, buildSortingParams, buildFilteringParams, buildFSPath } from "../utils";
import { log } from "console";
import { firstServicePath } from "../constants";

axios.interceptors.request.use(request => {
    console.log('Starting Request', JSON.stringify(request, null, 2))
    return request
});
axios.interceptors.response.use(response => {
    console.log('Response:', JSON.stringify(response, null, 2))
    return response
})

function makeArrFromRespData(pageableResp: any) {
    if (pageableResp.data && pageableResp.data.house)
        if (pageableResp.data.house.length)
            return pageableResp.data.house
        else
            return [pageableResp.data.house]

}

export const HouseService = {
    async getAll(pagintion?: PaginationInfo, filtering?: FilteringInfo<House>, sorting?: SortingInfo<House>) {
        const { data, headers } = await axios.get(buildFSPath(`/houses`), {
            params: {
                page: pagintion ? pagintion.page! : undefined,
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
            if (pageableResp.numberOfEntries == 1)
                pageableResp.data.house = [pageableResp.data.house]
            if (pageableResp.numberOfEntries == 0)
                return { data: [], numberOfEntries: parseInt(pageableResp.numberOfEntries) } as PageableResponse<House>
            const result = { data: mapToHouseArr(pageableResp.data.house), numberOfEntries: parseInt(pageableResp.numberOfEntries) } as PageableResponse<House>
            console.log(result)
            return result
        }
        return
    },

    async create(house: House) {
        return await axios.post(buildFSPath('/houses'), genXml(house, 'house'), { headers: { 'Content-Type': 'application/xml' } })
    },

    async delete(name: string) {
        return await axios.delete(buildFSPath(`/houses/${name}`))
    },

    async update(house: House) {
        var res = (await axios.put(buildFSPath(`/houses/${house.name}?year=${house.year}&numberOfFloors=${house.numberOfFloors}`))).data
        console.log(res);
        res = parseXml(res).house
        return res
    }
}

export const mapToHouse = (house: any):House => {
    return (
        {
            name: house.name,
            year: house.year,
            numberOfFloors: house.numberOfFloors
        } as House
    )
}


export const mapToHouseArr = (resp: any): House[] => {
    return (resp.map(mapToHouse))
}

