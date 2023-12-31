import axios from "axios"
import Flat from "../types"
import { parseXml } from "../utils"

const rootPath = "http://localhost:8080/agency"

export const AgencyService = {
    async findWithBalcony(isCheapest: boolean, hasBalcony: boolean) {
        var { data } = await axios.get(`${rootPath}/find-with-balcony/${isCheapest ? 'cheapest' : 'expensive'}/${hasBalcony ? 'with-balcony' : 'without-balcony'}`)
        console.log(parseXml(data));
        return {}
    },
    async compareFlats(firstFlatId: number, secondFlatId: number) {
        var { data } = await axios.get(`${rootPath}/get-cheapest/${firstFlatId}/${secondFlatId}`)
        console.log(parseXml(data));
        return parseXml(data) as Flat
    }
}