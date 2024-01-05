import React from 'react';
import Flat, { FedbackableProps, Feedback, FilteringInfo, FlatBackend, Furnish, SortingInfo, Transport, View } from '../../types';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { FlatService } from '../../services/FlatsService';
import { Box } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import {
  DataGridPro,
  GridColDef,
  GridSortModel,
  GridToolbarContainer,
  GridToolbarColumnsButton,
  GridToolbarFilterButton,
  useGridApiRef,
  GridFilterModel,
  GridActionsCellItem,
  GridRowModes,
  GridEventListener,
  GridRowEditStopReasons,
  GridRowId,
  GridRowModesModel,
  getGridNumericOperators,
  GridOverlay,
} from '@mui/x-data-grid-pro';
import { reactQueryKeys, gridColumns } from '../../constants';
import { buildFilteringInfo, buildFeedback } from '../../utils';
import EditIcon from '@mui/icons-material/Edit';
import SaveIcon from '@mui/icons-material/Save';
import CancelIcon from '@mui/icons-material/Close';
import { AxiosError } from 'axios';

const PAGE_SIZE = 10

interface FlatsTableProps extends FedbackableProps {
}

const columnGroupingModel = [
  {
    groupId: 'coordinates',
    headerClassName: 'MuiDataGrid-columnHeader--alignCenterr',
    children: [{ field: 'coordinate_x' }, { field: 'coordinate_y' }],
  },
  {
    groupId: 'house',
    headerClassName: 'MuiDataGrid-columnHeader--alignCenter',
    children: [{ field: 'house.name' }, { field: 'house.year' }, { field: 'house.numberOfFloors' }],
  },
];

export const FlatsTable: React.FC<FlatsTableProps> = ({ setFeedback }) => {
  const queryClient = useQueryClient()
  const dataGridApiRef = useGridApiRef()
  const [paginationModel, setPaginationModel] = React.useState({
    page: 0,
    pageSize: 5,
  });
  const [queryOptions, setQueryOptions] = React.useState<Partial<{ sorting: SortingInfo<FlatBackend>, filtering: FilteringInfo<FlatBackend> }>>({
    sorting: undefined,
    filtering: undefined
  });
  const [rowModesModel, setRowModesModel] = React.useState<GridRowModesModel>({});
  const { mutate: deleteMutate, status: deletionStatus } = useMutation({
    mutationFn: (flat: Flat) => FlatService.delete(flat.id),
    mutationKey: [reactQueryKeys.deleteFlat],
    onSuccess: () => {
      setFeedback(buildFeedback('success', 'Flat deleted'))
      queryClient.invalidateQueries({ queryKey: [reactQueryKeys.getAllFlats] })
    },
    onError: (error: AxiosError) => {
      console.log(error);
      setFeedback(buildFeedback('error', undefined, error))
    }
  })

  const handleSortModelChange = React.useCallback((sortModel: GridSortModel) => {
    setQueryOptions((prev) => {
      return {
        ...prev,
        sorting: Object.fromEntries(sortModel.map((gridSortItem) => [gridSortItem.field, gridSortItem.sort])) as SortingInfo<FlatBackend>
      }
    }
    );
  }, []);

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
    queryKey: [reactQueryKeys.getAllFlats, queryOptions, paginationModel],
    queryFn: () => FlatService.getAll({ ...paginationModel, page: paginationModel.page }, queryOptions.filtering, queryOptions.sorting)
  })

  const CustomToolbar = () => {
    return (
      <GridToolbarContainer>
        <GridToolbarColumnsButton />
        <GridToolbarFilterButton />
      </GridToolbarContainer>
    );
  }

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
    deleteMutate(dataGridApiRef.current.getRow(id)!)
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

  const { mutateAsync, status: updateStatus } = useMutation({
    mutationKey: [reactQueryKeys.updateFlat],
    mutationFn: (newFlat: Flat) => FlatService.update(newFlat),
    onSuccess() {
      setFeedback(buildFeedback('success', 'Flat updated'))
    },
    onError(error: AxiosError) {
      console.log(error);
      setFeedback(buildFeedback('error', undefined, error))
    }
  }
  )

  const processRowUpdate = (newRow: Flat, old: Flat) => {
    if (old === newRow)
      return old
    console.log(newRow);
    console.log(old);
    
    return mutateAsync(newRow);
  };

  const columns: GridColDef<Flat>[] = [
    ...gridColumns,
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
    }
  ]


  return (
    <Box sx={{ alignContent: 'center' }}>
      <DataGridPro
        autoHeight
        columns={columns}
        rows={resp ? resp.data ? resp.data : [] : []}
        getRowId={(row) => row.id}
        density='compact'
        experimentalFeatures={{ columnGrouping: true }}
        slots={{ toolbar: CustomToolbar, noRowsOverlay: () => (<GridOverlay children="There are't flats id database" />) }}
        columnGroupingModel={columnGroupingModel}
        loading={isLoading}
        // autoPageSize
        rowSpacingType='border'
        showCellVerticalBorder

        //pagination
        pagination
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

        // editing data
        editMode="row"
        rowModesModel={rowModesModel}
        onRowModesModelChange={handleRowModesModelChange}
        processRowUpdate={processRowUpdate}
        onProcessRowUpdateError={(e) => console.log(e)}
        onRowEditStop={handleRowEditStop}


        apiRef={dataGridApiRef}
      />
    </Box>
  )

}
