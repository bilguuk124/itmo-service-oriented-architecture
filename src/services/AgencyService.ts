import axios from "axios"
import Flat from "../types"
import { buildSecondServicePath, parseXml } from "../utils"
import { mapToFlat } from "./FlatsService"

export const AgencyService = {
    async findWithBalcony(isCheapest: boolean, hasBalcony: boolean) {
        var { data } = await axios.get(buildSecondServicePath(`/find-with-balcony/${isCheapest ? 'cheapest' : 'expensive'}/${hasBalcony ? 'with-balcony' : 'without-balcony'}`))
        console.log(parseXml(data));
        return {}
    },
    async compareFlats(firstFlatId: number, secondFlatId: number) {
        var { data } = await axios.get(buildSecondServicePath(`/get-cheapest/${firstFlatId}/${secondFlatId}`))
        const a = mapToFlat(parseXml(data).flat)
        console.log(a);
        return a as Flat
    }
}