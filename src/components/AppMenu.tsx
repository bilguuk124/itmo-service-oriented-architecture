import * as React from 'react';
import { AppBar, Toolbar } from '@material-ui/core';
import { TabPanel, TabList, TabContext } from '@mui/lab';
import { Box, Tab, Divider, Alert, Snackbar } from '@mui/material';
import { FlatsTable } from './firstService/FlatsTable';
import { CreateFlatForm } from './firstService/CreateFlatForm';
import { CreateHouseForm } from './firstService/CreateHouseForm';
import { HousesTable } from './firstService/HousesTable';
import { Feedback } from '../types';
import { AgencyTools } from './secondService/Agency';
import { Button, message } from 'antd';

message.config({})

export const AppMenu: React.FC = () => {
  const [value, setValue] = React.useState('1');
  const [feedback, setFeedback] = React.useState<Feedback>({ message: undefined, status: undefined });
  const [messageApi, contextHolder] = message.useMessage();

  React.useEffect(() => {
    if (!feedback)
      return
    switch (feedback.status) {
      case 'error': messageApi.error({content: feedback.message, style: {marginRight: 0, color: 'red'}})
        break
      case 'info': messageApi.info({content: feedback.message, style: {marginRight: 0}})
        break
      case 'success': messageApi.success({content: feedback.message, style: {marginRight: 0, color: 'green'}, })
        break
    }
  }, [feedback])

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
              {/* <Tab value='6' label="Other tools" /> */}
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
            <AgencyTools setFeedback={setFeedback} />
          </TabPanel>
          {/* <TabPanel value='6'>
            <OtherTools setFeedback={setFeedback} />
          </TabPanel> */}
        </Box>
      </TabContext>
      {contextHolder}
    </Box>
  );
};
