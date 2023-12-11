import * as React from 'react';
import {
    TextField,
    FormControl,
    Container,
    Typography,
    Box,
    Button
} from '@mui/material/';
import Flat, { Furnish, View, Transport, FlatCreate, House } from '../model/Flat';
import { useMutation } from 'react-query';
import { HouseService } from '../services/HouseService';
import { genXml } from '../utils';

const checkValid = (house: House): string[] => {
    const res = []
    if (house.name === '' || house.name === null)
        res.push('name')
    if (house.year < 0 || house.year > 634)
        res.push('year')
    if (house.numberOfFloors < 0)
        res.push('numberOfFloors')
    return res
}



export const CreateHouseForm = () => {
    const initStateHouse = {
        name: 'hrushevka',
        year: 1,
        numberOfFloors: 1
    }
    const [houseState, setHouseState] = React.useState<House>(initStateHouse)

    const { mutate } = useMutation(['createFlat'],
        (data: House) => HouseService.create(data),
        {
            onSuccess() { setHouseState(initStateHouse);  }
        }
    )

    const submitForm = (e: React.SyntheticEvent) => {
        e.preventDefault();
        mutate(houseState)
    }

    return (
        <Container maxWidth='sm'>
            <Typography variant='button' sx={{ fontSize: 20 }}> Creating House</Typography>
            <form onSubmit={submitForm}>
                <FormControl fullWidth>
                    <TextField
                        sx={{ mb: 1, mt: 1 }}
                        id='houseName'
                        label='Name'
                        error={checkValid(houseState).includes('name')}
                        value={houseState.name}
                        onChange={e => setHouseState({ ...houseState, name: e.target.value })}

                    />
                    <TextField
                        sx={{ mb: 1, mt: 1 }}
                        type='number'
                        id='houseYear'
                        label='Year'
                        error={checkValid(houseState).includes('year')}
                        value={houseState.year}
                        onChange={e => setHouseState({ ...houseState, year: parseInt(e.target.value) })} />
                    <TextField
                        sx={{ mb: 1, mt: 1 }}
                        type='number'
                        id='numberOfFloors'
                        label='Number of floors'
                        error={checkValid(houseState).includes('numberOfFloors')}
                        value={houseState.numberOfFloors}
                        onChange={e => setHouseState({ ...houseState, numberOfFloors: parseInt(e.target.value) })} />
                </FormControl>
                <Button variant='contained' sx={{ width: '70%', m: 2 }} type='submit'>Send</Button>
            </form>
            <Typography variant='h3'></Typography>
        </Container>
    )
}