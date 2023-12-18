import React from 'react';
import { useQuery } from 'react-query';
import { Box } from '@material-ui/core';
import { DataGrid, GridToolbar, GridRenderCellParams, GridSortModel, GridColDef, useGridApiRef, GridToolbarContainer, GridToolbarColumnsButton, GridToolbarFilterButton, useGridApiContext, useGridSelector, gridPageSelector, gridPageCountSelector, GridPagination, gridPaginationModelSelector } from '@mui/x-data-grid';
// import { DataGridPro, GridColDef, GridToolbar, GridSortModel, useGridApiRef} from '@mui/x-data-grid-pro';
import { HouseService } from '../services/HouseService';
import { FilteringInfo, House, PaginationInfo, SortingInfo } from "../types";
import { Button, Pagination, PaginationItem, Stack, TablePagination } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';

const PAGE_SIZE = 3

const columns: GridColDef<House>[] = [{ field: 'name', flex: 0.5 },
{ field: 'year', flex: 0.5 }, { field: 'numberOfFloors', flex: 0.5 }]

export const HousesTable = () => {
  const [paginationModel, setPaginationModel] = React.useState({
    page: 0,
    pageSize: PAGE_SIZE,
  });

  const dataGridRef = useGridApiRef()

  const [queryOptions, setQueryOptions] = React.useState<Partial<{ sorting: SortingInfo<House>, filtering: FilteringInfo<House> }>>({
    sorting: undefined,
    filtering: undefined
  });

  const handleSortModelChange = React.useCallback((sortModel: GridSortModel) => {
    setQueryOptions((prev) => {
      return {
        ...prev,
        sorting: Object.fromEntries(sortModel.map((gridSortItem) => [gridSortItem.field, gridSortItem.sort])) as SortingInfo<House>
      }
    }
    );
  }, []);

  // const handlePaginationModelChange = React.useCallback((event: React.ChangeEvent<unknown>, value: number) => {

  // }, [])


  const { isLoading, error, data: resp } = useQuery(
    ['getAllHouses', queryOptions, paginationModel],
    () => HouseService.getAll({ ...paginationModel, pageNumber: paginationModel.page }, queryOptions.filtering, queryOptions.sorting)
  )

  const [rowCountState, setRowCountState] = React.useState(
    resp?.length || 0,
  );
  React.useEffect(() => {
    setRowCountState((prevRowCountState: any) =>
      resp?.length !== undefined
        ? resp?.length 
        : prevRowCountState,
    );
  }, [resp?.length , setRowCountState]);

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
        getRowId={(row) => row.name}
        slots={{ toolbar: CustomToolbar }}
        loading={isLoading}
        density='compact'
        rowSpacingType='border'
        showCellVerticalBorder
        rowCount={rowCountState}

        // pagination
        paginationMode="server"
        pageSizeOptions={[PAGE_SIZE]}
        paginationModel={paginationModel}
        onPaginationModelChange={setPaginationModel}
        // autoPageSize

        // filtering
        filterMode="server"

        // sorting
        sortingMode="server"
        onSortModelChange={handleSortModelChange}
      // apiRef={dataGridRef}
      />
    </Box>
  )

}
