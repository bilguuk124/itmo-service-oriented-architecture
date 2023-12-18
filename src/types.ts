
export type ComparisonAlias = "eq" | "lt" | 'gt' | 'lte' | 'gte' | 'neq'
export type ComparisonInfo = { operation: ComparisonAlias, value: any }
export type SortingDirection = 'asc' | 'desc' | null
export type FilteringInfo<T> = Record<keyof Partial<T>, ComparisonInfo>
export type SortingInfo<T> = Record<keyof Partial<House>, SortingDirection>
export type PaginationInfo = { pageNumber: number, pageSize: number }

export default interface Flat {
    id: number;
    name: string;
    coordinates: Coordinates;
    creationDate: Date;
    area: number;
    numberOfRooms: number;
    furnish: Furnish;
    hasBalcony: boolean;
    price: number;
    view: View;
    transport: Transport;
    house: House;
}

export interface BadResponse {
    code: number;
    message: string;
    details: string;
    timestamp: Date;
}

export interface FlatBackend extends Omit<Flat, 'id' | 'creationDate' | 'coordinates'> {
    coordinates: {
        coordinate_x: number;
        coordinate_y: number;
    }
}

export type House = {
    name: string;
    year: number;
    numberOfFloors: number;
}

export interface Coordinates {
    x: number;
    y: number;
}

export enum Furnish {
    NONE = "NONE",
    FINE = "FINE",
    BAD = "BAD",
    LITTLE = "LITTLE"
}

export enum View {
    STREET = "STREET",
    YARD = "YARD",
    BAD = "BAD",
    NORMAL = "NORMAL",
    TERRIBLE = "TERRIBLE"
}

export enum Transport {
    FEW = "FEW",
    NONE = "NONE",
    LITTLE = "LITTLE",
    NORMAL = "NORMAL",
    ENOUGH = "ENOUGH"
}
