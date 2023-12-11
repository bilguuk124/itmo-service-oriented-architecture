export default interface Flat {
    id: number;
    name: String;
    coordinates: Coordinates;
    creationDate: Date;
    area: number;
    numberOfRooms: number;
    furnish: Furnish;
    hasBalcany: boolean;
    price: number;
    view: View;
    transport: Transport;
    house: House;
}

export interface FlatCreate extends Omit<Flat, 'id' | 'creationDate' | 'coordinates'>{
    // price: number;
    coordinates: {
        coordinate_x: number;
        coordinate_y: number;
    }
}

export interface House {
    name: String;
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
