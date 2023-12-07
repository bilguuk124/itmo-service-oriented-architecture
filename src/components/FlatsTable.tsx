import React from 'react';
import Flat from '../model/Flat';
import { useQuery } from 'react-query';
import { FlatService } from '../services/FlatsService';
import { Box } from '@material-ui/core';
import { DataGrid, GridToolbar, GridRenderCellParams, GridColDef } from '@mui/x-data-grid';


const columns_names = ['id', 'name', 'coordinateX', 'coordinateY', 'creationDate', 'area', 'roomsNumber', 'furnish', 'view', 'transport', 'houseName', 'houseYear', 'houseNumberOfFloors'];

const columns: GridColDef[] = [{ field: 'id', width: 10 },
{ field: 'name', flex: 0.5},
{ field: 'coordinateX', headerName: 'X', valueGetter: (a) => a.row.coordinates.x, width: 30, align: 'center', headerAlign: 'center', flex: 0.2 },
{ field: 'coordinateY', headerName: 'Y', valueGetter: (a) => a.row.coordinates.y, width: 30, align: 'center', headerAlign: 'center', flex: 0.2 },
{ field: 'creationDate', flex: 0.6, valueGetter: (a) => a.value.toLocaleString(), headerAlign:'center', align: 'center' },
{ field: 'area', headerAlign:'center', align: 'center', width: 70, flex: 0.5 },
{ field: 'roomsNumber', headerAlign:'center', align: 'center', flex: 0.5 },
{ field: 'furnish', headerAlign:'center', align: 'center', flex: 0.4 },
{ field: 'view', headerAlign:'center', align: 'center', flex: 0.4 },
{ field: 'transport', headerAlign:'center', align: 'center', flex: 0.4 },
{ field: 'houseName', headerName: 'name', valueGetter: (a) => a.row.house.name, headerAlign:'center', align: 'center', flex: 0.6 },
{ field: 'houseYear', headerName: 'year', valueGetter: (a) => a.row.house.year, headerAlign:'center', align: 'center', flex: 0.4 },
{ field: 'houseNumberOfFloors', headerName: 'numberOfFloors', valueGetter: (a) => a.row.house.numberOfFloors, headerAlign:'center', align: 'center', flex: 0.6 }]

const columnGroupingModel = [
  {
    groupId: 'coordinates',
    headerClassName: 'MuiDataGrid-columnHeader--alignCenterr',
    children: [{ field: 'coordinateX' }, { field: 'coordinateY' }],
  },
  {
    groupId: 'house',
    headerClassName: 'MuiDataGrid-columnHeader--alignCenter',
    children: [{ field: 'houseName' }, { field: 'houseYear' }, { field: 'houseNumberOfFloors' }],
  },
];

export const FlatsTable = () => {


  const { isLoading, error, data: resp } = useQuery(
    'flatsAll',
    () => FlatService.getAll()
  )

  return (
    <Box sx={{ height: '75vh', alignContent: 'center' }}>
      <DataGrid<Flat>
        columns={columns}
        rows={resp ? resp : []}
        getRowId={(row) => row.id}
        disableColumnSelector
        disableColumnMenu
        disableDensitySelector
        disableColumnFilter
        density='compact'
        experimentalFeatures={{ columnGrouping: true }}
        columnGroupingModel={columnGroupingModel}
        // paginationModel={{page: 1, pageSize: 50}}
        autoPageSize
        slots={{ toolbar: GridToolbar }}
        slotProps={{
          toolbar: {
            showQuickFilter: true,
          }
        }}
        rowSpacingType='border'
        showCellVerticalBorder
      />
    </Box>
  )

}
