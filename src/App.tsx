import * as React from 'react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools'
import { SelectService } from './components/SelectService';
import axios from 'axios';

axios.defaults.baseURL = "http://localhost:9090/api"

const queryClient = new QueryClient({
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
        {/* <ReactQueryDevtools /> */}
    </QueryClientProvider>
    </>
  )

};

export default App;
