import * as React from 'react';
import { Container, Snackbar, Alert, Stack, Button } from '@mui/material'
import { QueryClient, QueryClientProvider } from 'react-query';
import { Feedback } from './types';
import { Anchor } from 'antd';
import { SelectService } from './components/SelectService';

export const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
    },
  },
});

const App: React.FC = () => {

  return (
    <>
    <QueryClientProvider client={queryClient}>
        <SelectService></SelectService>
    </QueryClientProvider>
    </>
  )

};

export default App;
