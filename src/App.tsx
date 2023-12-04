import * as React from 'react';
import FlatsTable from './components/FlatsTable';
import Flat, { Furnish, View, Transport } from './model/Flat';
import { AppBar, Container, Toolbar } from '@material-ui/core';
import { DataGrid, GridToolbar } from '@mui/x-data-grid';
import { useDemoData } from '@mui/x-data-grid-generator';
import Box from '@mui/material/Box'
import styled from '@mui/styled-engine';

const VISIBLE_FIELDS = ['name', 'rating', 'country', 'dateCreated', 'isAdmin'];

export const StickyDataGrid = styled(DataGrid)(({theme}) => ({
    '& .MuiDataGrid-columnHeaders': {
        position: "sticky"
    },
    '& .MuiDataGrid-virtualScroller': {
        // Undo the margins that were added to push the rows below the previously fixed header
        marginTop: "0 !important"
    },
    '& .MuiDataGrid-main': {
        // Not sure why it is hidden by default, but it prevented the header from sticking
        overflow: "visible"
    }
}))

function QuickFilteringGrid() {
  const { data } = useDemoData({
    dataSet: 'Employee',
    visibleFields: VISIBLE_FIELDS,
    rowLength: 100,
  });

  // Otherwise filter will be applied on fields such as the hidden column id
  const columns = React.useMemo(
    () => data.columns.filter((column) => VISIBLE_FIELDS.includes(column.field)),
    [data.columns],
  );

  return (
    <Box sx={{ height: '75vh', mt: 10 }}>
      <DataGrid
        {...data}
        disableColumnSelector
        disableDensitySelector
        columns={columns}
        // slots={{ toolbar: GridToolbar }}
        // slotProps={{
        //   toolbar: {
        //     showQuickFilter: true,
        //   },
        // }}
      />
      </Box>
    
  );
}

const App: React.FC = () => (
  <>
    <AppBar position='fixed'>
      <Toolbar>

      </Toolbar>
    </AppBar>
    <main>

      <Container maxWidth='lg'>
        {QuickFilteringGrid()}
      </Container>
    </main>
  </>
);


export default App;
