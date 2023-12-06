import React from 'react';
import Flat from '../model/Flat';
import { useQuery } from 'react-query';
import { FlatService }from '../services/FlatsService';
import { Box } from '@material-ui/core'; 
import { DataGrid, GridToolbar } from '@mui/x-data-grid';

interface MyProps {
  flats: Flat[]
}

const VISIBLE_FIELDS = ['name', 'rating', 'country', 'dateCreated', 'isAdmin'];

export default class FlatsTable extends React.Component<MyProps, any, any> {\

  constructor(public data: Flat[]) {
    super({ flats: data });
  }

  render(): React.ReactNode {
    const { isLoading, error, data: resp } = useQuery(
      'repoData',
      () => FlatService.getAll()
    )

    let a = resp?.data

    if (isLoading) return a = new Array<Flat>()!;

    if (error) return a = new Array<Flat>()!;
    return (
      <Box sx={{ height: '75vh', mt: 10, width: 1 }}>
        <DataGrid<Flat>
          columns={VISIBLE_FIELDS.map((e) => {
            return { field: e, width: 100 }
          })}
          rows={[]}
          getRowId={(row) => row.id}
          disableColumnSelector
          disableDensitySelector
          disableColumnFilter
          density='compact'
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

}
