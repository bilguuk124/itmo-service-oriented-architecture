import { Furnish, Transport, View } from "./types"

export const reactQueryKeys = {
    getAllFlats: 'flatsAll',
    createFlat: 'createFlat',
    updateFlat: '',
    getFlat: '',
    deleteFlat: '',
    getAllHouses: 'getAllHouses',
    createHouse: 'createHouse',
    updateHouse: '',
    getHouse: '', 
    deleteHouse: '',
}

export const flatInitState = {
    name: '',
    coordinates: {
        coordinate_x: 0,
        coordinate_y: 0,
    },
    area: 1,
    numberOfRooms: 1,
    furnish: Furnish.NONE,
    hasBalcony: false,
    price: 1,
    view: View.NORMAL,
    transport: Transport.NORMAL,
    house: {
        name: '',
        year: 1,
        numberOfFloors: 1,
    }
}