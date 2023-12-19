import { create } from 'xmlbuilder2'
import { ComparisonAlias, FilteringInfo, FlatBackend, SortingInfo } from './types'
import { parseString } from 'xml2js';

export const getComparisonAliasByMathOperator = (oper: string): ComparisonAlias | undefined => {
    if ('=' === oper) return 'eq'
    if ('!=' === oper) return 'neq'
    if ('>' === oper) return 'gt'
    if ('>=' === oper) return 'gte'
    if ('<' === oper) return 'lt'
    if ('<=' === oper) return 'lte'
}

export const genXml = (target: any, rootKey?: string): string => {
    // create(flat)
    let obj = { ...target }

    if (rootKey)
        obj = {
            [rootKey]: { ...target }
        }
    const doc = create({ version: '1.0', encoding: "UTF-8", standalone: true }, obj);
    console.log(doc.toString());
    return doc.toString()
}

export const parseXml = (xmlReq: any, root?: string) => {
    let res: any
    parseString(xmlReq, { explicitArray: false }, (err: any, result: any) => {
        if (err) {
            throw err
        }
        if (root)
            res = result[root]
        else res = result
    })
    return res
}


export const buildSortingParams = (sorting: SortingInfo<any>): string => {
    return Object.entries(sorting) //getting keys of type
        .filter(([key, val]) => val ? true : false) // filtering undefined properties
        .map(([key, val]) => val === 'desc' ? `-${key}` : key) // 'desc' -> -val , 'asc' -> val
        .join(',')
}

export const buildFilteringParams = (filteringInfo: FilteringInfo<any>): string | undefined => {
    if (Object.values(filteringInfo).filter((val) => val.value !== undefined && val.value !== '').length === 0)
        return undefined
    return Object.entries(filteringInfo) //getting keys of type
        .map(([key, val]) => `${key}[${val.operation}]=${val.value}`)
        .join(',')
}