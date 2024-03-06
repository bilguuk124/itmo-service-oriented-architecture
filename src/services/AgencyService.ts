import axios from "axios"
import Flat, { House } from "../types"
import { buildSecondServicePath as buildSecondServiceUrl, genXml, parseXml } from "../utils"
import { mapToFlat } from "./FlatsService"
import { mapToHouse } from "./HouseService"

export const AgencyService = {
    async findWithBalcony(isCheapest: boolean, hasBalcony: boolean) {
        var { data } = await axios.get(buildSecondServiceUrl(`/find-with-balcony/${isCheapest ? 'cheapest' : 'expensive'}/${hasBalcony ? 'with-balcony' : 'without-balcony'}`))
        const a = mapToFlat(parseXml(data).Flat)
        console.log(a);
        return a as Flat
    },

    async compareFlats(firstFlatId: number, secondFlatId: number) {
        var { data } = await axios.get(buildSecondServiceUrl(`/get-cheapest/${firstFlatId}/${secondFlatId}`))
        const a = mapToFlat(parseXml(data).Flat)
        console.log(a);
        return a as Flat
    },

    async testPost(house: House){
        var { data } = await axios.post(buildSecondServiceUrl('/test'), genXml(house, 'house'), { headers: { 'Content-Type': 'application/xml' } })
        return mapToHouse(parseXml(data).House) as House
    }
}