import * as React from 'react';
import {
    TextField,
    FormControl,
    InputLabel,
    FormGroup,
    FormLabel,
    Container,
    Typography,
    FormControlLabel,
    Box,
    Paper,
    Button,
    Autocomplete,
    Switch,
    Grid
} from '@mui/material/';
import { Furnish, View, Transport } from '../model/Flat';
import { Label } from '@material-ui/icons';

export const CreateFlatForm = () => {
    return (
        <Container maxWidth='sm'>
            <Typography variant='h5'> Creating Flat</Typography>
            <form>

                <FormGroup aria-label='Flats'>
                    <TextField id='name' label='Name' required sx={{ mb: 1, mt: 1 }} />
                    <TextField id='area' label='Area' type='number' required sx={{ mb: 1, mt: 1 }} />
                    <TextField id='numberOfRooms' label='Number of rooms' type='number' required sx={{ mb: 1, mt: 1 }} />
                    <TextField id='price' label='Price' type='number' required sx={{ mb: 1, mt: 1 }} />
                    <Autocomplete
                        disablePortal
                        id="furnish"
                        options={Object.keys(Furnish)}
                        renderInput={(params) =>
                            <TextField {...params} label="Furnish" required />}
                        sx={{ mb: 1, mt: 1 }}
                    />
                    <Autocomplete
                        disablePortal
                        id='view'
                        options={Object.keys(View)}
                        renderInput={(params) =>
                            <TextField {...params} label="View" required />}
                        sx={{ mb: 1, mt: 1 }}
                    />
                    <Autocomplete
                        id='transport'
                        options={Object.keys(Transport)}
                        renderInput={(params) =>
                            <TextField {...params} label="Transport" required />}
                        sx={{ mb: 1, mt: 1 }}
                    />
                    <Box sx={{ p: 2, pt: 1, mb: 1, mt: 1, border: '1px solid', borderRadius: '5px', borderColor: 'LightGray' }}>
                        <Typography variant='h6' sx={{ textAlign: 'start', pb: 1, pl: 1 }}>Coordinates</Typography>
                        <Box
                            sx={{
                                borderRadius: 2,
                                bgcolor: 'background.default',
                                display: 'grid',
                                gridTemplateColumns: { md: '1fr 1fr' },
                                gap: 1,
                            }}
                        >
                            <TextField type='number' id='coordinateX' label='X' />
                            <TextField type='number' id='coordinateY' label='Y' />
                        </Box>
                    </Box>
                    <Box sx={{ p: 2, pt: 1, mt: 1, mp: 1, border: '1px solid', borderRadius: '5px', borderColor: 'LightGray' }}>
                        <Typography variant='h6' sx={{ textAlign: 'start', pb: 1, pl: 1 }}>House</Typography>
                        <Box
                            sx={{
                                bgcolor: 'background.default',
                                display: 'flex',
                                flexWrap: 'wrap',
                                gap: 1
                            }}
                        >
                            <TextField sx={{ minWidth: 0.4, flexShrink: -1  }} type='number' id='houseName' label='Name' />
                            <TextField sx={{ minWidth: 0.5, flexShrink: -1 }} type='number' id='houseYear' label='Year' />
                            <TextField sx={{ minWidth: 1, flexShrink: -1  }} type='number' id='houseRoomCount' label='Rooms' />
                        </Box>
                    </Box>
                    <FormControlLabel control={<Switch />} label="Has balcony" sx={{ alignSelf: 'center' }} />
                </FormGroup>
                <Button variant='contained' sx={{ width: '70%', m: 4}} type='submit'>Send</Button>
            </form>
        </Container>
    )
}
