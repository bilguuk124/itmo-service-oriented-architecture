import React from 'react';
import Flat from '../model/Flat';
import { useQuery } from 'react-query';
import { FlatService } from '../services/FlatsService';
import { Box } from '@material-ui/core';
import { DataGrid, GridToolbar, GridRenderCellParams } from '@mui/x-data-grid';


const columns_names = ['id', 'name', 'coordinateX', 'coordinateY', 'creationDate', 'area', 'roomsNumber', 'furnish', 'view', 'transport', 'houseName', 'houseYear', 'houseNumberOfFloors'];

const columns = [{ field: 'id', width: 5 },
{ field: 'name' },
{ field: 'coordinateX' },
{ field: 'coordinateY' },
{ field: 'creationDate', width: 150, renderCell: (params: GridRenderCellParams<any, Date>) => params.value?.toLocaleString()},
{ field: 'area' },
{ field: 'roomsNumber' },
{ field: 'furnish' },
{ field: 'view' },
{ field: 'transport' },
{ field: 'houseName' },
{ field: 'houseYear' },
{ field: 'houseNumberOfFloors' }]

const columnGroupingModel = [
  {
    groupId: 'coordinates',
    children: [{ field: 'coordinateX' }, { field: 'coordinateY' }],
  },
  {
    groupId: 'house',
    children: [{ field: 'houseName' }, { field: 'houseYear' }, { field: 'houseNumberOfFloors' }],
  },
];

export const FlatsTable = () => {


  const { isLoading, error, data: resp } = useQuery(
    'flatsAll',
    () => FlatService.getAll()
  )

  return (
    <Box sx={{ height: '75vh', mt: 10 }}>
      <DataGrid<Flat>
        columns={columns}
        rows={resp ? resp : []}
        getRowId={(row) => row.id}
        disableColumnSelector
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
