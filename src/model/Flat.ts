export default interface Flat {
    id:number;
    name: String;
    coordinates: Coordinates;
    creationDate: Date;
    area: number;
    roomsNumber: number;
    furnish: Furnish;
    view: View;
    transport: Transport;
    house: House;
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
    NONE, FINE, BAD, LITTLE
}

export enum View {
    STREET, YARD, BAD, NORMAL, TERRIBLE
}

export enum Transport {
    FEW, NONE, LITTLE, NORMAL, ENOUGH
}
