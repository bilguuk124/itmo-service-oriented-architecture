import React from 'react';
import Flat, { Feedback, FilteringInfo, FlatBackend, SortingInfo } from '../types';
import { useMutation, useQuery } from 'react-query';
import { FlatService } from '../services/FlatsService';
import { Box } from '@material-ui/core';
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
  GridRowModesModel
} from '@mui/x-data-grid-pro';
import { reactQueryKeys } from '../constants';
import { buildFilteringInfo } from '../utils';
import { buildFeedback, queryClient } from '../App';
import EditIcon from '@mui/icons-material/Edit';
import SaveIcon from '@mui/icons-material/Save';
import CancelIcon from '@mui/icons-material/Close';

const PAGE_SIZE = 5

interface FlatsTableProps {
  setFeedback: React.Dispatch<React.SetStateAction<Feedback>>
}


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

export const FlatsTable: React.FC<FlatsTableProps> = ({setFeedback}) => {
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
  const { mutate: deleteMutate, status: deletionStatus } = useMutation([reactQueryKeys.deleteFlat],
    (flat: Flat) => FlatService.delete(flat.id),
    {
      onSuccess() {
        setFeedback(buildFeedback(deletionStatus, 'Flat deleted'))
        queryClient.invalidateQueries(reactQueryKeys.getAllFlats)
      },
      onError(error: any) {
        console.log(error);
        setFeedback(buildFeedback(deletionStatus, error.details))
      }
    }
  )

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

  const { isLoading, error, data: resp } = useQuery(
    [reactQueryKeys.getAllFlats, queryOptions, paginationModel],
    () => FlatService.getAll({ ...paginationModel, page: paginationModel.page }, queryOptions.filtering, queryOptions.sorting)
  )

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

  const { mutateAsync, status: updateStatus } = useMutation([reactQueryKeys.updateFlat],
    (newFlat: Flat) => FlatService.update(newFlat),
    {
      onSuccess() {
        setFeedback(buildFeedback(updateStatus, 'Falt updated'))
      },
      onError(error: any) {
        console.log(error);
        setFeedback(buildFeedback(updateStatus, error.details))
      }
    }
  )

  const processRowUpdate = (newRow: Flat, old: Flat) => {
    if (old === newRow)
      return old
    return mutateAsync(newRow);
  };

  const columns: GridColDef<Flat>[] = [{ field: 'id', width: 10 },
  { field: 'name', flex: 0.5, editable: true },
  { field: 'coordinateX', headerName: 'X', valueGetter: (a) => a.row.coordinates.x, width: 30, align: 'center', headerAlign: 'center', flex: 0.2, editable: true },
  { field: 'coordinateY', headerName: 'Y', valueGetter: (a) => a.row.coordinates.y, width: 30, align: 'center', headerAlign: 'center', flex: 0.2, editable: true },
  { field: 'creationDate', flex: 0.6, valueGetter: (a) => a.value.toLocaleString(), headerAlign: 'center', align: 'center' },
  { field: 'area', headerAlign: 'center', align: 'center', width: 70, flex: 0.5, editable: true },
  { field: 'roomsNumber', headerAlign: 'center', align: 'center', flex: 0.5, editable: true },
  { field: 'furnish', headerAlign: 'center', align: 'center', flex: 0.4, editable: true },
  { field: 'view', headerAlign: 'center', align: 'center', flex: 0.4, editable: true },
  { field: 'transport', headerAlign: 'center', align: 'center', flex: 0.4, editable: true },
  { field: 'houseName', headerName: 'name', valueGetter: (a) => a.row.house.name, headerAlign: 'center', align: 'center', flex: 0.6 },
  { field: 'houseYear', headerName: 'year', valueGetter: (a) => a.row.house.year, headerAlign: 'center', align: 'center', flex: 0.4, editable: true },
  { field: 'houseNumberOfFloors', headerName: 'numberOfFloors', valueGetter: (a) => a.row.house.numberOfFloors, headerAlign: 'center', align: 'center', flex: 0.6, editable: true },
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
  },]

  return (
    <Box sx={{ alignContent: 'center' }}>
      <DataGridPro
        columns={columns}
        rows={resp.data ? resp.data : []}
        getRowId={(row) => row.id}
        density='compact'
        experimentalFeatures={{ columnGrouping: true }}
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
        slotProps={{
          noRowsOverlay: { sx: { display: 'flex', height: '300px' } },
        }}
      />
    </Box>
  )

}
