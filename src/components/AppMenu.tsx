import * as React from 'react';
import { AppBar, Toolbar, styled, withStyles } from '@material-ui/core';
import { TabPanel, TabList, TabContext } from '@mui/lab';
import { Box, Tab, Divider, Alert, Snackbar, IconButton, MenuItem, Menu } from '@mui/material';
import { FlatsTable } from './firstService/FlatsTable';
import { CreateFlatForm } from './firstService/CreateFlatForm';
import { CreateHouseForm } from './firstService/CreateHouseForm';
import { HousesTable } from './firstService/HousesTable';
import { Feedback } from '../types';
import { AgencyTools } from './secondService/Agency';
import { message } from 'antd';
import ConstructionIcon from '@mui/icons-material/Construction';


const StyledTabList = styled(TabList)(({ theme }) => ({
  '& .MuiButtonBase-root': {
    color: 'white',
    height: 32,
    minHeight: 32,
    margin: 0,
    paddingTop: 0,
    paddingBottom: 0
  },
  '& .MuiTabs-root': {
    height: 32,
    minHeight: 32,
    margin: 0,
    padding:0
  },
  '& .MuiTabs-scroller': {
    height: 35,
    margin: 0,
    paddingTop: 0,
    paddingBottom: 0
  },
  '& .MuiTabs-Fixed': {
    height: 35,
    margin: 0,
    paddingTop: 0,
    paddingBottom: 0
  },
}))

const StyledTab = withStyles({
  root: {
    padding: 0,
    margin: 0,
    minHeight: 30,
  }
})(Tab);

export const AppMenu: React.FC = () => {
  const [value, setValue] = React.useState('1');
  const [feedback, setFeedback] = React.useState<Feedback>({ message: undefined, status: undefined });
  const [messageApi, contextHolder] = message.useMessage();

  React.useEffect(() => {
    if (!feedback)
      return
    switch (feedback.status) {
      case 'error': messageApi.error({ content: feedback.message, style: { marginRight: 0, color: 'red' } })
        break
      case 'info': messageApi.info({ content: feedback.message, style: { marginRight: 0, color: 'blue' } })
        break
      case 'success': messageApi.success({ content: feedback.message, style: { marginRight: 0, color: 'green' }, })
        break
    }
  }, [feedback])

  const handleChange = (event: React.SyntheticEvent, newValue: string) => {
    setValue(newValue);
  };

  return (
    <Box sx={{ height: '100%' }}>
      <TabContext value={value}>
        <AppBar
          position='static'
          style={{
            height: 43,
            display: 'inline-flex',
            flexWrap: 'wrap',
            justifyContent: 'space-between',
            justifyItems: 'center', alignItems: 'center', paddingTop: 4
          }}>
          <StyledTabList onChange={handleChange}
            sx={{
              '& .MuiTabs-indicator': { bgcolor: 'white', display: (value != '5' ? 'block' : 'none'), animation: value != '5' ? 'none' : 'none', position: 'relative' },
            }}
            centered
            textColor='inherit'
          >
            <StyledTab value="1" label="Flats table" />
            <StyledTab value="2" label="Houses table" />
            <StyledTab value="3" label="Create flat" />
            <StyledTab value="4" label="Create house" />
            <Divider orientation='vertical' flexItem sx={{ backgroundColor: 'white', ml: 4, mr: 4 }} />
            <StyledTab value='5'
              disableRipple
              sx={{
                justifySelf: 'end',
                borderRadius: 3,
                background: '#A540E3',
              }}
              label='Agency Service' />
          </StyledTabList>
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
        </Box>
      </TabContext>
      {contextHolder}
    </Box>
  );
};

