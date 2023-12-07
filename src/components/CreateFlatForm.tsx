import { TextField, FormControl, FormGroup, Input, Container, Typography, FormControlLabel, Checkbox, Autocomplete } from '@mui/material/';
import { Furnish, View, Transport } from '../model/Flat';
import { MenuItem } from '@material-ui/core';
import { Label } from '@material-ui/icons';

export const CreateFlatForm = () => {
    return (
        <Container maxWidth='sm'>
            <Typography variant='h5'> Creating Flat</Typography>
            <FormGroup aria-label='Flats'>
                <TextField id='name' label='Name' required sx={{ mb: 2, mt: 2 }} />
                <TextField id='area' label='Area' type='number' required sx={{ mb: 2 }} />
                <TextField id='numberOfRooms' label='Number of rooms' type='number' required sx={{ mb: 2 }} />
                <TextField id='price' label='Price' type='number' required sx={{ mb: 2 }} />
                <Autocomplete
                    disablePortal
                    id="furnish"
                    options={Object.keys(Furnish)}
                    renderInput={(params) => <TextField {...params} label="Furnish" required sx={{ mb: 2 }} />}></Autocomplete>
                <TextField id='view' label='View' select required sx={{ mb: 2 }}>
                    {Object.keys(View).map((key) => <MenuItem key={key} value={key}>{key}</MenuItem>)}
                </TextField>
                <TextField id='transport' label='Transport' select required sx={{ mb: 2 }}>
                    {Object.keys(Transport).map((key) => <MenuItem key={key} value={key}>{key}</MenuItem>)}
                </TextField>
                <FormControlLabel control={<Checkbox />} label="Has balcony" sx={{ alignSelf: 'center' }} />
            </FormGroup>
        </Container>
    )
}
