import * as React from 'react';
import Flat, { Furnish, View, Transport } from './model/Flat';
import { AppBar, Container, Toolbar } from '@material-ui/core';
import styled from '@mui/styled-engine';
import { FlatsTable } from './components/FlatsTable';
import { QueryClient, QueryClientProvider } from 'react-query';

const queryClient = new QueryClient();

const App: React.FC = () => (
  <QueryClientProvider client={queryClient}>
    <>
      <AppBar position='fixed'>
        <Toolbar>

        </Toolbar>
      </AppBar>
      <main>

        <Container maxWidth='lg'>
          <FlatsTable></FlatsTable>
        </Container>
      </main>
    </>
  </QueryClientProvider>

);


export default App;
