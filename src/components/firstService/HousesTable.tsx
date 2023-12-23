import React from 'react';
import { useMutation, useQuery } from 'react-query';
import { Box } from '@material-ui/core';
import {
  DataGridPro,
  GridSortModel,
  GridColDef,
  GridToolbarContainer,
  GridToolbarColumnsButton,
  GridToolbarFilterButton,
  useGridApiRef,
  GridFilterModel,
  getGridNumericOperators,
  GridRowModes,
  GridActionsCellItem,
  GridRowModesModel,
  GridEventListener,
  GridRowEditStopReasons,
  GridRowId,
  GridRowModel,
  getGridSingleSelectOperators,
  GridOverlay,
  GridOverlayProps
} from '@mui/x-data-grid-pro';
import { HouseService } from '../../services/HouseService';
import { FilteringInfo, House, PaginationInfo, SortingInfo, ComparisonAlias, ComparisonInfo, Feedback } from "../../types";
import { Button, Pagination, PaginationItem, Stack, TablePagination, styled } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import { buildFilteringInfo, getComparisonAliasByMathOperator } from '../../utils';
import { reactQueryKeys } from '../../constants';
import EditIcon from '@mui/icons-material/Edit';
import SaveIcon from '@mui/icons-material/Save';
import CancelIcon from '@mui/icons-material/Close';
import { queryClient } from '../../App';
import { buildFeedback } from '../../utils';
import { AxiosError } from 'axios';

const PAGE_SIZE = 5


interface HouseTableProps {
  setFeedback: React.Dispatch<React.SetStateAction<Feedback>>
}


export const HousesTable: React.FC<HouseTableProps> = ({ setFeedback }) => {
  const [paginationModel, setPaginationModel] = React.useState({
    page: 0,
    pageSize: PAGE_SIZE,
  });

  const [rowModesModel, setRowModesModel] = React.useState<GridRowModesModel>({});

  const { mutate: deleteMutate, status } = useMutation([reactQueryKeys.deleteHouse],
    (data: House) => HouseService.delete(data.name),
    {
      onSuccess() {
        setFeedback(buildFeedback('success', 'House deleted'))
        queryClient.invalidateQueries(reactQueryKeys.getAllHouses)
      },
      onError(error: AxiosError) {
        console.log(error);
        setFeedback(buildFeedback('error', undefined, error))
      }
    }
  )

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

  const handleRowEditStop: GridEventListener<'rowEditStop'> = (params, event) => {
    if (params.reason === GridRowEditStopReasons.rowFocusOut) {
      event.defaultMuiPrevented = true;
    }
  };

  const handleEditClick = (id: GridRowId) => () => {
    setRowModesModel({ ...rowModesModel, [id]: { mode: GridRowModes.Edit } });
    console.log('handel edit')
    console.log(id)
  };

  const handleSaveClick = (id: GridRowId) => () => {
    setRowModesModel({ ...rowModesModel, [id]: { mode: GridRowModes.View } });
  };

  const handleDeleteClick = (id: GridRowId) => () => {
    deleteMutate(dataGridRef.current.getRow(id)!)
  };

  const handleCancelClick = (id: GridRowId) => () => {
    setRowModesModel({
      ...rowModesModel,
      [id]: { mode: GridRowModes.View, ignoreModifications: true },
    });
  };

  const handleRowModesModelChange = (newRowModesModel: GridRowModesModel) => {
    setRowModesModel(newRowModesModel);
  };

  const { mutateAsync, status: updateStatus } = useMutation([reactQueryKeys.updateHouse],
    (newHouse: House) => HouseService.update(newHouse),
    {
      onSuccess() {
        setFeedback(buildFeedback('success', 'House updated'))
      },
      onError(error: AxiosError) {
        console.log(error);
        setFeedback(buildFeedback('error', undefined, error))
      }
    }
  )

  const processRowUpdate = (newRow: House, old: House) => {
    if (old === newRow)
      return old

    return mutateAsync(newRow);
  };

  const columns: GridColDef<House>[] = [
    { field: 'name', flex: 0.5, filterOperators: getGridSingleSelectOperators() },
    { field: 'year', flex: 0.5, filterOperators: getGridNumericOperators().slice(0, 6), editable: true },
    { field: 'numberOfFloors', flex: 0.5, filterOperators: getGridNumericOperators().slice(0, 6), editable: true },
    {
      field: 'actions',
      type: 'actions',
      headerName: 'Actions',
      width: 100,
      cellClassName: 'actions',
      getActions: ({ id }) => {
        const isInEditMode = rowModesModel[id]?.mode === GridRowModes.Edit;

        if (isInEditMode) {
          return [
            <GridActionsCellItem
              icon={<SaveIcon />}
              label="Save"
              sx={{
                color: 'primary.main',
              }}
              onClick={handleSaveClick(id)} />,
            <GridActionsCellItem
              icon={<CancelIcon />}
              label="Cancel"
              className="textPrimary"
              onClick={handleCancelClick(id)}
              color="inherit" />,
          ];
        }

        return [
          <GridActionsCellItem
            icon={<EditIcon />}
            label="Edit"
            className="textPrimary"
            onClick={handleEditClick(id)}
            color="inherit" />,
          <GridActionsCellItem
            icon={<DeleteIcon />}
            label="Delete"
            onClick={handleDeleteClick(id)}
            color="inherit" />,
        ];
      }
    },
  ]

  const onFilterChange = React.useCallback((filterModel: GridFilterModel) => {
    if (filterModel.items.filter(val => val.value !== '' && val.value !== undefined).length === 0)
      setQueryOptions((prev) => {
        return { ...prev, filtering: undefined }
      })
    setQueryOptions((prev) => {
      return {
        ...prev,
        filtering: { ...prev.filtering!, ...buildFilteringInfo(filterModel) }
      }
    })
  }, []);

  const { isLoading, error, data: resp } = useQuery(
    [reactQueryKeys.getAllHouses, queryOptions, paginationModel],
    () => HouseService.getAll({ ...paginationModel, page: paginationModel.page }, queryOptions.filtering, queryOptions.sorting)
  )

  const [rowCountState, setRowCountState] = React.useState(
    resp?.numberOfEntries || 0,
  );

  React.useEffect(() => {
    setRowCountState((prevRowCountState: any) =>
      resp?.numberOfEntries !== undefined
        ? resp?.numberOfEntries
        : prevRowCountState,
    );
  }, [resp?.numberOfEntries, setRowCountState]);

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
        autoHeight
        columns={columns}
        rows={resp?.data ? resp.data : []}
        getRowId={(row) => row.name}
        slots={{ toolbar: CustomToolbar, noRowsOverlay: () => (<GridOverlay children="There are't flats id database" />) }}
        loading={isLoading}
        density='compact'
        rowSpacingType='border'
        showCellVerticalBorder
        rowCount={rowCountState}

        // pagination
        pagination
        paginationMode="server"
        pageSizeOptions={[PAGE_SIZE]}
        paginationModel={paginationModel}
        onPaginationModelChange={setPaginationModel}
        // autoPageSize

        // filtering
        filterMode="server"
        onFilterModelChange={onFilterChange}

        // sorting
        sortingMode="server"
        onSortModelChange={handleSortModelChange}

        // editing data
        editMode="row"
        rowModesModel={rowModesModel}
        onRowModesModelChange={handleRowModesModelChange}
        processRowUpdate={processRowUpdate}
        onProcessRowUpdateError={(e) => console.log(e)}
        onRowEditStop={handleRowEditStop}

        apiRef={dataGridRef}
      />
    </Box>
  )

}

