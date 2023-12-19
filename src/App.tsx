import * as React from 'react';
import { AppBar, Toolbar } from '@material-ui/core';
import { TabPanel, TabList, TabContext } from '@mui/lab';
import { Container, Box, Tab } from '@mui/material'
import { FlatsTable } from './components/FlatsTable';
import { QueryClient, QueryClientProvider } from 'react-query';
import { CreateFlatForm } from './components/CreateFlatForm';
import { CreateHouseForm } from './components/CreateHouseForm';
import { HousesTable } from './components/HousesTable';

export const queryClient = new QueryClient();

function BasicTabs() {
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
            <FlatsTable />
          </TabPanel>
          <TabPanel value='3'>
            <HousesTable />
          </TabPanel>
          <TabPanel value='0'>
            <CreateFlatForm />
          </TabPanel>
          <TabPanel value='2'>
            <CreateHouseForm />
          </TabPanel>
        </Box>
      </TabContext>
    </Box>
  );
}

const App: React.FC = () => (
  <QueryClientProvider client={queryClient}>
    <>
      <AppBar position='static'>
        <Toolbar>
        </Toolbar>
      </AppBar>
      <main>
        <Container maxWidth={false} >
          <BasicTabs />
        </Container>
      </main>
    </>
  </QueryClientProvider>

);


export default App;
