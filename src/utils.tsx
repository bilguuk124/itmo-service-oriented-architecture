import { create } from 'xmlbuilder2'
import { BadResponse, ComparisonAlias, Feedback, FilteringInfo, FlatBackend, SortingInfo } from './types'
import { parseString } from 'xml2js';
import { GridFilterModel } from '@mui/x-data-grid-pro';
import { AxiosError } from 'axios';
import { MutationStatus } from 'react-query';
import { firstServicePath as firstServiceRootPath, reactQueryKeys, secondServicePath as secondServiceRootPath } from './constants';
import { useQuery } from '@tanstack/react-query';
import { FlatService } from './services/FlatsService';

export const buildFilteringInfo = (filterModel: GridFilterModel): FilteringInfo<any> => Object.fromEntries(filterModel.items.map((val) => [val.field, {
    operation: getComparisonAliasByMathOperator(val.operator)!,
    value: val.value
}]))

export const getComparisonAliasByMathOperator = (oper: string): ComparisonAlias | undefined => {
    if ('=' === oper || 'is' === oper) return 'eq'
    if ('!=' === oper || 'is not' === oper) return 'neq'
    if ('>' === oper || 'is after' === oper) return 'gt'
    if ('>=' === oper || 'is on or after' === oper) return 'gte'
    if ('<' === oper || 'is before' === oper) return 'lt'
    if ('<=' === oper || 'is on or before' === oper) return 'lte'
}

export const genXml = (target: any, rootKey?: string): string => {
    // create(flat)
    console.log(target);
    
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
    console.log(res)
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
};

export const buildFeedback = (status: 'error' | 'success'  | 'info', msg?: string, error?: AxiosError) => {
    console.log(error)
    return {
        status: status == 'error' || status == 'success' ? status : 'info',
        message: error ? error.response ? (parseXml(error.response.data, 'ErrorBody') as BadResponse).details : error.message : msg
    } as Feedback;
};

export const buildFSPath = (path: string): string => {
    return `${firstServiceRootPath}${path}`
}

export const buildSecondServicePath = (path: string): string => {
    return `${secondServiceRootPath}${path}`
}

export const isFlatExist = (flatId: number): boolean => {
    var isSuccess = false
    FlatService.get(flatId).then()
    return isSuccess
}
