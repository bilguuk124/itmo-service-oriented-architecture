import * as React from 'react';
import { AppBar, Toolbar } from '@material-ui/core';
import { TabPanel, TabList, TabContext } from '@mui/lab';
import { Box, Tab, Divider, Alert, Snackbar } from '@mui/material';
import { FlatsTable } from './firstService/FlatsTable';
import { CreateFlatForm } from './firstService/CreateFlatForm';
import { CreateHouseForm } from './firstService/CreateHouseForm';
import { HousesTable } from './firstService/HousesTable';
import { Feedback } from '../types';
import { SecondService } from './secondService/Agency';
import { OtherTools } from './firstService/OtherTools';

interface HouseTableProps {
  setFeedback: React.Dispatch<React.SetStateAction<Feedback>>;
}
export const SelectService: React.FC = () => {
  const [value, setValue] = React.useState('1');
  const [feedback, setFeedback] = React.useState<Feedback>({ message: undefined, status: undefined });

  const handleClose = (event: React.SyntheticEvent | Event, reason?: string) => {
    if (reason === 'clickaway') {
      return;
    }
    setFeedback({ status: undefined, message: undefined });
  };

  const handleChange = (event: React.SyntheticEvent, newValue: string) => {
    setValue(newValue);
  };

  return (
    <Box sx={{ height: '100%' }}>
      <TabContext value={value}>
        <AppBar position='static'>
          <Toolbar style={{ display: 'inline-grid' }}>
            <TabList onChange={handleChange}
              sx={{
                display: 'inline-grid',
                backgroundColor: 'inherit',
                color: 'white',
                '& .MuiTabs': {
                  color: 'white', display: 'inline-grid'
                },
                '& .MuiTabs-indicator': { bgcolor: 'white', display: (value != '5' ? 'block' : 'none'), animation: value != '5' ? 'none' : 'none' }
              }
              }
              centered
              textColor='inherit'
            >
              <Tab value="1" label="Flats table" />
              <Tab value="2" label="Houses table" />
              <Tab value="3" label="Create flat" />
              <Tab value="4" label="Create house" />
              <Tab value='6' label="Other tools" />
              <Divider orientation='vertical' flexItem sx={{ backgroundColor: 'white', ml: 4, mr: 4 }} />
              <Tab value='5'
                disableRipple
                sx={{
                  justifySelf: 'end',
                  borderRadius: 3,
                  background: '#A540E3',
                }}
                label='Agency Service' />
            </TabList>
          </Toolbar>
        </AppBar>
        <Box sx={{ bgcolor: 'white', height: '100%' }}>
          <TabPanel value='1'>
            <FlatsTable setFeedback={setFeedback} />
          </TabPanel>
          <TabPanel value='2'>
            <HousesTable setFeedback={setFeedback} />
          </TabPanel>
          <TabPanel value='3'>
            <CreateFlatForm setFeedback={setFeedback} />
          </TabPanel>
          <TabPanel value='4'>
            <CreateHouseForm setFeedback={setFeedback} />
          </TabPanel>
          <TabPanel value='5' id='secondservice'>
            <SecondService setFeedback={setFeedback} />
          </TabPanel>
          <TabPanel value='6'>
            <OtherTools setFeedback={setFeedback} />
          </TabPanel>
        </Box>
      </TabContext>
      <Snackbar
        open={feedback?.status !!= undefined}
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
    </Box>
  );
};
