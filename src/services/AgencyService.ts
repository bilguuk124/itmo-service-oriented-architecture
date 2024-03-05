import axios from "axios"
import Flat, { House } from "../types"
import { buildSecondServicePath as buildSecondServiceUrl, parseXml } from "../utils"
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

    async testPost(){
        var { data } = await axios.post(buildSecondServiceUrl('/test'))
        return mapToHouse(parseXml(data).House) as House
    }
}