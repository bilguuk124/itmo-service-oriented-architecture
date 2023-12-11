import * as React from 'react';
import { AppBar, Toolbar } from '@material-ui/core';
import { TabPanel, TabList, TabContext } from '@mui/lab';
import { Container, Box, Tab } from '@mui/material'
import { FlatsTable } from './components/FlatsTable';
import { QueryClient, QueryClientProvider } from 'react-query';
import { CreateFlatForm } from './components/CreateFlatForm';
import { CreateHouseForm } from './components/CreateHouseForm';

const queryClient = new QueryClient();

function BasicTabs() {
  const [value, setValue] = React.useState('0');

  const handleChange = (event: React.SyntheticEvent, newValue: string) => {
    setValue(newValue);
  };

  return (
    <Box sx={{ width: '100%' }}>
      <TabContext value={value}  >
        <Box sx={{ borderBottom: 1, borderColor: 'divider', mt: 1 }}>
          <TabList onChange={handleChange} sx={{ m: 0 }} >
            <Tab value="1" label="Table" />
            <Tab value="0" label="Create Flat"></Tab>
            <Tab value="2" label="Create House"></Tab>
            <Tab value="3" label="Delete"></Tab>
          </TabList>
        </Box>
        <TabPanel value='1'>
          <FlatsTable />
        </TabPanel>
        <TabPanel value='0' >
          <CreateFlatForm/>
        </TabPanel>
        <TabPanel value='2'>
          <CreateHouseForm/>
        </TabPanel>
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
        <Container sx={{ width: '100%' }} maxWidth={false} >
          <BasicTabs />
        </Container>
      </main>
    </>
  </QueryClientProvider>

);


export default App;
