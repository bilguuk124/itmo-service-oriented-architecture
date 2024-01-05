import React from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
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
import { FilteringInfo, House, SortingInfo, Feedback, FedbackableProps } from "../../types";
import { buildFilteringInfo } from '../../utils';
import { reactQueryKeys } from '../../constants';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import SaveIcon from '@mui/icons-material/Save';
import DeleteSweep from '@mui/icons-material/DeleteSweep';
import CancelIcon from '@mui/icons-material/Close';
import FunctionsIcon from '@mui/icons-material/Functions';
import { buildFeedback } from '../../utils';
import { AxiosError } from 'axios';
import { FlatService } from '../../services/FlatsService';
import { Tooltip } from '@mui/material';

const PAGE_SIZE = 5


interface HouseTableProps extends FedbackableProps {
}


export const HousesTable: React.FC<HouseTableProps> = ({ setFeedback }) => {
  const queryClient = useQueryClient()
  const dataGridRef = useGridApiRef()
  const [paginationModel, setPaginationModel] = React.useState({
    page: 0,
    pageSize: PAGE_SIZE,
  });

  const [rowModesModel, setRowModesModel] = React.useState<GridRowModesModel>({});

  const { mutate: deleteMutate, status } = useMutation({
    mutationKey: [reactQueryKeys.deleteHouse],
    mutationFn: (data: House) => HouseService.delete(data.name),

    onSuccess() {
      setFeedback(buildFeedback('success', 'House deleted'))
      queryClient.invalidateQueries({ queryKey: [reactQueryKeys.getAllHouses] })
    },
    onError(error: AxiosError) {
      console.log(error);
      setFeedback(buildFeedback('error', undefined, error))
    }
  })

  const { mutate: deleteAllFlatsInHouseMutation } = useMutation({
    mutationKey: [reactQueryKeys.deleteAllFlatsInHouse],
    mutationFn: (house: House) => FlatService.deleteAllInHouse(house.name),

    onSuccess(_, house) {
      setFeedback(buildFeedback('success', `All flats in house '${house.name}' deleted`))
      queryClient.invalidateQueries({ queryKey: [reactQueryKeys.getAllFlats] })
    },
    onError(error: AxiosError) {
      console.log(error);
      setFeedback(buildFeedback('error', undefined, error))
    }
  })

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

  const handleDeleteAllFlatsInHouse = (id: GridRowId) => () => {
    deleteAllFlatsInHouseMutation(dataGridRef.current.getRow(id)!)
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

  const handleCountFlatsClick = (id: GridRowId) => () => {
    const house = dataGridRef.current.getRow(id) as House
    FlatService.countInHouse(house.name)
      .then(flatsNumber => `House ${house.name} has ${flatsNumber} flats`)
      .then(message => setFeedback(buildFeedback('info', message)))
      .catch(err => setFeedback(buildFeedback('info', undefined, err)))
  }

  const { mutateAsync } = useMutation({
    mutationKey: [reactQueryKeys.updateHouse],
    mutationFn: (newHouse: House) => HouseService.update(newHouse),
    onSuccess() {
      setFeedback(buildFeedback('success', 'House updated'))
    },
    onError(error: AxiosError) {
      console.log(error);
      setFeedback(buildFeedback('error', undefined, error))
    }
  })

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
      width: 160,
      cellClassName: 'actions',
      getActions: ({ id }) => {
        const isInEditMode = rowModesModel[id]?.mode === GridRowModes.Edit;

        if (isInEditMode) {
          return [
            <GridActionsCellItem
              icon={<Tooltip title='Save changes' ><SaveIcon /></Tooltip>}
              label="Save"
              onClick={handleSaveClick(id)} />,
            <GridActionsCellItem
              icon={<Tooltip title='Cansel Changes' ><CancelIcon /></Tooltip>}
              label="Cancel"
              onClick={handleCancelClick(id)}
              color="inherit" />,
          ];
        }

        return [
          <GridActionsCellItem
            icon={<Tooltip title='Edit' ><EditIcon /></Tooltip>}
            label="Edit"
            onClick={handleEditClick(id)}
            color="inherit" />,
          <GridActionsCellItem
            icon={<Tooltip title='Delete' ><DeleteIcon /></Tooltip>}
            label="Delete"
            onClick={handleDeleteClick(id)}
            color="inherit" />,
          <GridActionsCellItem
            icon={<Tooltip title='Delete all flats' ><DeleteSweep sx={{ fontSize: 24 }} /></Tooltip>}
            label="Delete all flats"
            onClick={handleDeleteAllFlatsInHouse(id)}
            color="inherit" />,
          <GridActionsCellItem
            icon={<Tooltip title='Count flats' ><FunctionsIcon sx={{ fontSize: 24}} /></Tooltip>}
            label="Count flats"
            onClick={handleCountFlatsClick(id)}
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

  const { isLoading, error, data: resp } = useQuery({
    queryKey: [reactQueryKeys.getAllHouses, queryOptions, paginationModel],
    queryFn: () => HouseService.getAll({ ...paginationModel, page: paginationModel.page }, queryOptions.filtering, queryOptions.sorting)
  })

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
    <Box sx={{ display: 'flex', alignContent: 'center', justifyContent: 'center' }}>
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
        sx={{ maxWidth: 1000 }}
      />
    </Box>
  )

}

