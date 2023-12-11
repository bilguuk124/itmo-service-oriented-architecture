import { create } from 'xmlbuilder2'
import { FlatCreate } from './model/Flat'
import { parseString } from 'xml2js';

export const genXml = (target: any, rootKey?: string): string => {
    // create(flat)
    let obj = { ...target }

    if (rootKey)
        obj = {
            [rootKey]: { ...target }
        }


    const doc = create({ version: '1.0', encoding: "UTF-8", standalone: true }, obj);
    // const xml = doc.end({ prettyPrint: true });
    console.log(doc.toString());
    return doc.toString()
}

export const parseXml = (xmlReq: any) => {
    let res: any
    parseString(xmlReq, { explicitArray: false }, (err: any, result: any) => {
        if (err) {
            throw err
        }
        res = result
    })
    return res
}