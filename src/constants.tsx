import {
  GridCellParams,
  GridColDef,
  GridFilterItem,
  GridFilterOperator,
  GridValueSetterParams,
  getGridDateOperators,
  getGridNumericOperators,
  getGridStringOperators
} from "@mui/x-data-grid-pro"
import Flat,
{
  Furnish,
  Transport,
  View
} from "./types"
import { Input, TextField } from "@mui/material";
import { SyntheticEvent } from "react";

export const reactQueryKeys = {
  getAllFlats: 'flatsAll',
  createFlat: 'createFlat',
  updateFlat: 'updateFlat',
  getFlat: 'getHouse',
  deleteFlat: 'deleteFlat',
  getAllHouses: 'getAllHouses',
  createHouse: 'createHouse',
  updateHouse: 'updateHouse',
  getHouse: 'getHouse',
  deleteHouse: 'deleteHouse',
  findCheapest: 'findCheapest',
  compareFlats: 'compareFlats',
  deleteAllFlatsInHouse: 'deleteAllFlatsInHouse'
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

const customNumberOperators = getGridNumericOperators().slice(0, 6)

const customStringOperators: GridFilterOperator[] = [
  {
    ...customNumberOperators.find((val) => val.value === '=')!,
    getApplyFilterFn: (filterItem: GridFilterItem) => {
      if (!filterItem.field || !filterItem.value || !filterItem.operator) {
        return null;
      }

      return (params: GridCellParams): boolean => {
        return params.value === filterItem.value;
      };
    },
    InputComponentProps: { ...customNumberOperators.find((val) => val.value === '=')!.InputComponentProps, type: 'string', autoComplete: 'off' }
  },
  {
    ...customNumberOperators.find((val) => val.value === '<')!,
    getApplyFilterFn: (filterItem: GridFilterItem) => {
      if (!filterItem.field || !filterItem.value || !filterItem.operator) {
        return null;
      }

      return (params: GridCellParams): boolean => {
        return String(params.value) < filterItem.value;
      };
    },
    InputComponentProps: { ...customNumberOperators.find((val) => val.value === '<')!.InputComponentProps, type: 'string', autoComplete: 'off' }
  },
  {
    ...customNumberOperators.find((val) => val.value === '>')!,
    getApplyFilterFn: (filterItem: GridFilterItem) => {
      if (!filterItem.field || !filterItem.value || !filterItem.operator) {
        return null;
      }

      return (params: GridCellParams): boolean => {
        return String(params.value) > filterItem.value;
      };
    },
    InputComponentProps: { ...customNumberOperators.find((val) => val.value === '>')!.InputComponentProps, type: 'string', autoComplete: 'off' }
  },
  {
    ...customNumberOperators.find((val) => val.value === '<=')!,
    getApplyFilterFn: (filterItem: GridFilterItem) => {
      if (!filterItem.field || !filterItem.value || !filterItem.operator) {
        return null;
      }

      return (params: GridCellParams): boolean => {
        return String(params.value) <= filterItem.value;
      };
    },
    InputComponentProps: {
      ...customNumberOperators.find((val) => val.value === '<=')!.InputComponentProps, type: 'string', autoComplete: 'off'
    }
  },
  {
    ...customNumberOperators.find((val) => val.value === '>=')!,
    getApplyFilterFn: (filterItem: GridFilterItem) => {
      if (!filterItem.field || !filterItem.value || !filterItem.operator) {
        return null;
      }

      return (params: GridCellParams): boolean => {
        return String(params.value) >= filterItem.value;
      };
    },
    InputComponentProps: { ...customNumberOperators.find((val) => val.value === '<=')!.InputComponentProps, type: 'string', autoComplete: 'off' }
  },
];


export const gridColumns: GridColDef<Flat>[] = [
  {
    field: 'id',
    width: 10,
    filterOperators: customNumberOperators
  },
  {
    field: 'name',
    flex: 0.5,
    editable: true,
    filterOperators: customStringOperators
  },
  {
    field: 'coordinates.x',
    headerName: 'X',
    valueGetter: (a) => a.row.coordinates.x,
    valueSetter: (a: GridValueSetterParams) => { a.row.coordinates.x = parseInt(a.value); return { ...a.row, } },
    width: 30,
    align: 'center',
    headerAlign: 'center',
    flex: 0.2,
    editable: true,
    filterOperators: customNumberOperators
  },
  {
    field: 'coordinates.y',
    headerName: 'Y',
    valueGetter: (a) => a.row.coordinates.y,
    valueSetter: (a: GridValueSetterParams) => { a.row.coordinates.y = parseInt(a.value); return { ...a.row, } },
    width: 30,
    align: 'center',
    headerAlign: 'center',
    flex: 0.2,
    editable: true,
    filterOperators: customNumberOperators
  },
  {
    field: 'creationDate',
    flex: 0.6,
    valueGetter: (a) => a.value,
    headerAlign: 'center',
    align: 'center',
    type: 'date',
    filterOperators: getGridDateOperators().slice(0, 6)
  },
  {
    field: 'area',
    headerAlign: 'center',
    align: 'center',
    width: 70,
    flex: 0.5,
    editable: true,
    filterOperators: customNumberOperators
  },
  {
    field: 'numberOfRooms',
    headerAlign: 'center',
    align: 'center',
    flex: 0.5,
    editable: true,
    filterOperators: customNumberOperators
  },
  {
    field: 'furnish',
    type: 'singleSelect',
    valueOptions: Object.keys(Furnish),
    headerAlign: 'center',
    align: 'center',
    flex: 0.4,
    editable: true
  },
  {
    field: 'view',
    type: 'singleSelect',
    valueOptions: Object.keys(View),
    headerAlign: 'center',
    align: 'center',
    flex: 0.4,
    editable: true
  },
  {
    field: 'transport',
    type: 'singleSelect',
    valueOptions: Object.keys(Transport),
    headerAlign: 'center',
    align: 'center',
    flex: 0.4,
    editable: true,
  },
  {
    field: 'house.name',
    headerName: 'name',
    valueGetter: (a) => a.row.house.name,
    valueSetter: (a: GridValueSetterParams) => { a.row.house.name = parseInt(a.value); return { ...a.row, } },
    headerAlign: 'center',
    align: 'center',
    flex: 0.6,
    filterOperators: customStringOperators
  },
  {
    field: 'house.year',
    headerName: 'year',
    valueGetter: (a) => a.row.house.year,
    valueSetter: (a: GridValueSetterParams) => { a.row.house.year = parseInt(a.value); return { ...a.row, } },
    headerAlign: 'center',
    align: 'center',
    flex: 0.4,
    editable: true,
    filterOperators: customNumberOperators
  },
  {
    field: 'house.numberOfFloors',
    headerName: 'numberOfFloors',
    valueGetter: (a) => a.row.house.numberOfFloors,
    valueSetter: (a: GridValueSetterParams) => { a.row.house.numberOfFloors = parseInt(a.value); return { ...a.row, } },
    headerAlign: 'center',
    align: 'center',
    flex: 0.6,
    editable: true,
    filterOperators: customNumberOperators
  },
]
