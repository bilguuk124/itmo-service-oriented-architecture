import * as React from 'react';
import { AppBar, Toolbar } from '@material-ui/core';
import { TabPanel, TabList, TabContext } from '@mui/lab';
import { Container, Box, Tab, Snackbar, Alert } from '@mui/material'
import { FlatsTable } from './components/FlatsTable';
import { MutationStatus, QueryClient, QueryClientProvider } from 'react-query';
import { CreateFlatForm } from './components/CreateFlatForm';
import { CreateHouseForm } from './components/CreateHouseForm';
import { HousesTable } from './components/HousesTable';
import { Feedback } from './types';
import { AxiosError } from 'axios';
import { parseXml } from './utils';

export const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
    },
  },
});

export const buildFeedback = (status: MutationStatus, msg?: string, error?: AxiosError) => {
  return {
    status: status == 'error' || status == 'success' ? status : 'info',
    message: error ? parseXml(error.response?.data).errorBody.message : msg
  } as Feedback
}

interface HouseTableProps {
  setFeedback: React.Dispatch<React.SetStateAction<Feedback>>
}

const BasicTabs: React.FC<HouseTableProps> = ({ setFeedback }) => {
  const [value, setValue] = React.useState('0');

  const handleChange = (event: React.SyntheticEvent, newValue: string) => {
    setValue(newValue);
  };

  return (
    <Box>
      <TabContext value={value} >
        <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
          <TabList onChange={handleChange} sx={{ m: 0, backgroundColor: 'lightblue', width: '100%', opacity: '90%' }} >
            <Tab value="1" label="Flats table" sx={{ color: 'black', fontWeight: '500' }} />
            <Tab value="3" label="Houses table" sx={{ color: 'black', fontWeight: '500' }} ></Tab>
            <Tab value="0" label="Create Flat" sx={{ color: 'black', fontWeight: '500' }} ></Tab>
            <Tab value="2" label="Create House" sx={{ color: 'black', fontWeight: '500' }} ></Tab>
          </TabList>
        </Box>
        <Box sx={{ height: '88vh', backgroundColor: 'white' }}>
          <TabPanel value='1'>
            <FlatsTable setFeedback={setFeedback} />
          </TabPanel>
          <TabPanel value='3'>
            <HousesTable setFeedback={setFeedback} />
          </TabPanel>
          <TabPanel value='0'>
            <CreateFlatForm setFeedback={setFeedback} />
          </TabPanel>
          <TabPanel value='2'>
            <CreateHouseForm setFeedback={setFeedback} />
          </TabPanel>
        </Box>
      </TabContext>
    </Box>
  );
}

const App: React.FC = () => {


  const [feedback, setFeedback] = React.useState<Feedback>({ message: undefined, status: undefined });

  const handleClose = (event: React.SyntheticEvent | Event, reason?: string) => {
    if (reason === 'clickaway') {
      return;
    }
    setFeedback({ status: undefined, message: undefined });
  };


  return (<QueryClientProvider client={queryClient}>
    <>
      <AppBar position='static'>
        <Toolbar>
        </Toolbar>
      </AppBar>
      <main>
        <Container maxWidth={false} >
          <BasicTabs setFeedback={setFeedback} />
        </Container>
      </main>
      <Snackbar
        open={feedback?.status != undefined}
        autoHideDuration={6000}
        onClose={handleClose}
        message={feedback?.message}
        anchorOrigin={{ horizontal: 'right', vertical: 'top' }}
        sx={{ width: 'auto' }}>
        <Alert onClose={handleClose}
          variant="filled"
          severity={feedback?.status}
          sx={{ width: '100%' }}>
          {feedback?.message}
        </Alert>
      </Snackbar>
    </>
  </QueryClientProvider>)

};

export default App;
