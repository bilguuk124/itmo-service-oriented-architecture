import * as React from 'react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools'
import { AppMenu } from './components/AppMenu';
import axios from 'axios';

axios.defaults.baseURL = "http://localhost:8080/api"

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
        <AppMenu></AppMenu>
        {/* <ReactQueryDevtools /> */}
    </QueryClientProvider>
    </>
  )

};

export default App;
