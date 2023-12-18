import React from 'react';
import { useQuery } from 'react-query';
import { Box } from '@material-ui/core';
// import { DataGrid, GridToolbar, GridRenderCellParams, GridSortModel, GridColDef, useGridApiRef, GridToolbarContainer, GridToolbarColumnsButton, GridToolbarFilterButton, useGridApiContext, useGridSelector, gridPageSelector, gridPageCountSelector, GridPagination, gridPaginationModelSelector } from '@mui/x-data-grid';
import {
  DataGridPro,
  GridSortModel,
  GridColDef,
  GridToolbarContainer,
  GridToolbarColumnsButton,
  GridToolbarFilterButton,
  useGridApiRef,
  GridFilterModel,
  getGridNumericOperators
} from '@mui/x-data-grid-pro';
import { HouseService } from '../services/HouseService';
import { FilteringInfo, House, PaginationInfo, SortingInfo, ComparisonAlias, ComparisonInfo } from "../types";
import { Button, Pagination, PaginationItem, Stack, TablePagination } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import { getComparisonAliasByMathOperator } from '../utils';

const PAGE_SIZE = 10

const buildFilteringInfo = (filterModel: GridFilterModel): FilteringInfo<any> => Object.fromEntries(filterModel.items.map((val) => [val.field, {
  operation: getComparisonAliasByMathOperator(val.operator)!,
  value: val.value
}]))

const columns: GridColDef<House>[] = [
  { field: 'name', flex: 0.5, filterOperators: getGridNumericOperators().slice(0, 6) },
  { field: 'year', flex: 0.5, filterOperators: getGridNumericOperators().slice(0, 6) },
  { field: 'numberOfFloors', flex: 0.5, filterOperators: getGridNumericOperators().slice(0, 6) }
]

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

  const onFilterChange = React.useCallback((filterModel: GridFilterModel) => {
    if (filterModel.items.filter(val => val.value !== '' && val.value !== undefined).length === 0)
      setQueryOptions((prev) => { return { ...prev, filtering: undefined } })
    setQueryOptions((prev) => {
      return {
        ...prev,
        filtering: { ...prev.filtering!, ...buildFilteringInfo(filterModel) }
      }
    })
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
  }, [resp?.length, setRowCountState]);

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
      <DataGridPro
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
        onFilterModelChange={onFilterChange}
        // filterModel={queryOptions.filtering}

        // sorting
        sortingMode="server"
        onSortModelChange={handleSortModelChange}
        apiRef={dataGridRef}
        slotProps={{
          noRowsOverlay: { sx: { display: 'flex', height: '300px' } },
        }}
      // sx={{'--DataGrid-overlayHeight': '3000px'}}
      />
    </Box>
  )

}
