import React from 'react';
import Flat, { FilteringInfo, FlatBackend, SortingInfo } from '../types';
import { useQuery } from 'react-query';
import { FlatService } from '../services/FlatsService';
import { Box } from '@material-ui/core';
import { DataGrid, GridToolbar, GridRenderCellParams, GridColDef, GridSortModel, GridToolbarContainer, GridToolbarDensitySelector, GridToolbarColumnsButton, GridToolbarFilterButton } from '@mui/x-data-grid';
import { Button, Pagination, Stack } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
// import { DataGridPro, GridColDef, GridToolbar, GridSortModel } from '@mui/x-data-grid-pro';

const columns: GridColDef<Flat>[] = [{ field: 'id', width: 10 },
{ field: 'name', flex: 0.5 },
{ field: 'coordinateX', headerName: 'X', valueGetter: (a) => a.row.coordinates.x, width: 30, align: 'center', headerAlign: 'center', flex: 0.2 },
{ field: 'coordinateY', headerName: 'Y', valueGetter: (a) => a.row.coordinates.y, width: 30, align: 'center', headerAlign: 'center', flex: 0.2 },
{ field: 'creationDate', flex: 0.6, valueGetter: (a) => a.value.toLocaleString(), headerAlign: 'center', align: 'center' },
{ field: 'area', headerAlign: 'center', align: 'center', width: 70, flex: 0.5 },
{ field: 'roomsNumber', headerAlign: 'center', align: 'center', flex: 0.5 },
{ field: 'furnish', headerAlign: 'center', align: 'center', flex: 0.4 },
{ field: 'view', headerAlign: 'center', align: 'center', flex: 0.4 },
{ field: 'transport', headerAlign: 'center', align: 'center', flex: 0.4 },
{ field: 'houseName', headerName: 'name', valueGetter: (a) => a.row.house.name, headerAlign: 'center', align: 'center', flex: 0.6 },
{ field: 'houseYear', headerName: 'year', valueGetter: (a) => a.row.house.year, headerAlign: 'center', align: 'center', flex: 0.4 },
{ field: 'houseNumberOfFloors', headerName: 'numberOfFloors', valueGetter: (a) => a.row.house.numberOfFloors, headerAlign: 'center', align: 'center', flex: 0.6 }]

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
  const [paginationModel, setPaginationModel] = React.useState({
    page: 0,
    pageSize: 5,
  });

  const [queryOptions, setQueryOptions] = React.useState<Partial<{ sorting: SortingInfo<FlatBackend>, filtering: FilteringInfo<FlatBackend> }>>({
    sorting: undefined,
    filtering: undefined
  });

  const handleSortModelChange = React.useCallback((sortModel: GridSortModel) => {
    setQueryOptions((prev) => {
      return {
        ...prev,
        sorting: Object.fromEntries(sortModel.map((gridSortItem) => [gridSortItem.field, gridSortItem.sort])) as SortingInfo<FlatBackend>
      }
    }
    );
  }, []);


  const { isLoading, error, data: resp } = useQuery(
    ['flatsAll', queryOptions],
    () => FlatService.getAll({ ...paginationModel, pageNumber: paginationModel.page }, queryOptions.filtering, queryOptions.sorting)
  )

  const CustomFooter = () => {
    return (
      <Stack direction="row" spacing={2} alignItems="center" sx={{ display: 'inline-grid', alignItems: 'center', gridAutoFlow: 'column' }}>
        <Button variant="outlined" startIcon={<DeleteIcon />} sx={{ justifySelf: 'start' }}>
          Delete
        </Button>
        <Pagination count={10} showFirstButton showLastButton sx={{ justifySelf: 'end' }} />
      </Stack>
    )
  }

  const CustomToolbar = () => {
    return (
      <GridToolbarContainer>
        <GridToolbarColumnsButton />
        <GridToolbarFilterButton />
      </GridToolbarContainer>
    );
  }

  return (
    <Box sx={{ alignContent: 'center' }}>
      <DataGrid
        columns={columns}
        rows={resp ? resp : []}
        getRowId={(row) => row.id}
        density='compact'
        experimentalFeatures={{ columnGrouping: true }}
        columnGroupingModel={columnGroupingModel}
        // autoPageSize
        slots={{ toolbar: CustomToolbar, footer: CustomFooter }}
        rowSpacingType='border'
        showCellVerticalBorder

        // filtering
        filterMode="server"

        // sorting
        sortingMode="server"
        onSortModelChange={handleSortModelChange}
      />
    </Box>
  )

}
