import * as React from 'react';
import {
    TextField,
    FormControl,
    Container,
    Typography,
    FormControlLabel,
    Box,
    Button,
    Autocomplete,
    Switch
} from '@mui/material/';
import { Furnish, View, Transport, FlatBackend, Feedback, House } from '../../types';
import { QueryCache, useMutation, useQuery } from 'react-query';
import { FlatService } from '../../services/FlatsService';
import { queryClient } from '../../App';
import { buildFeedback } from '../../utils';
import { flatInitState, reactQueryKeys } from '../../constants';
import { AxiosError } from 'axios';
import { parseXml } from '../../utils';
import { HouseService } from '../../services/HouseService';

interface LabeledBoxProps {
    label: string;
    children: React.ReactNode;
}

const LabeledBox: React.FC<LabeledBoxProps> = ({ label, children }) => {
    return (
        <Box sx={{ p: 2, pt: 1, mb: 1, mt: 1, border: '1px solid', borderRadius: '5px', borderColor: 'LightGray' }}>
            <Typography variant='h6' sx={{ textAlign: 'start', pb: 1, pl: 1 }}>{label}</Typography>
            {children}
        </Box>
    )
}

const validateForm = (flat: FlatBackend): string[] => {
    const res = []
    if (flat.name === '' || flat.name === null)
        res.push('name')
    if (flat.house.name === '' || flat.house.name === null)
        res.push('houseName')
    if (flat.house.year < 0 || flat.house.year > 634)
        res.push('houseYear')
    if (flat.house.numberOfFloors < 0)
        res.push('numberOfFloors')
    if (flat.furnish === null || !Object.keys(Furnish).includes(flat.furnish))
        res.push('furnish')
    if (flat.view === null || !Object.keys(View).includes(flat.view))
        res.push('view')
    if (flat.transport === null || !Object.keys(Transport).includes(flat.transport))
        res.push('transport')
    if (flat.area > 527 || flat.area < 0)
        res.push('area');
    if (flat.coordinates.coordinate_x > 548)
        res.push('coordinateX');
    if (flat.price == null || flat.price < 0)
        res.push('price');
    if (flat.coordinates.coordinate_y === null)
        res.push('coordinateY');
    if (flat.numberOfRooms < 0)
        res.push('numberOfRooms');
    return res
}

interface CreateFlatFormProps {
    setFeedback: React.Dispatch<React.SetStateAction<Feedback>>
}

export const CreateFlatForm: React.FC<CreateFlatFormProps> = ({ setFeedback }) => {
    const [flatState, setFlatState] = React.useState<FlatBackend>(flatInitState)

    const { mutate, status, error } = useMutation([reactQueryKeys.createFlat],
        (data: FlatBackend) => FlatService.create(data),
        {
            onSuccess() {
                setFlatState(flatInitState);
                setFeedback(buildFeedback('success', 'Flat Created'))
                queryClient.invalidateQueries(reactQueryKeys.getAllFlats)
                
            },
            onError(error: AxiosError) {
                console.log(error)
                setFeedback(buildFeedback('error', "Creation failed", error))
                queryClient.invalidateQueries(reactQueryKeys.getAllFlats)
            }
        }
    )

    const submitForm = (e: React.SyntheticEvent) => {
        e.preventDefault();
        mutate(flatState)
    }


    return (
        <Container maxWidth='sm'>
            <Typography variant='button' color='black' sx={{ fontSize: 20 }}> Creating Flat</Typography>
            <form onSubmit={submitForm}>
                <FormControl>
                    <TextField
                        id='name'
                        label='Name'
                        value={flatState.name}
                        onChange={e => setFlatState({ ...flatState, name: e.target.value })}
                        error={validateForm(flatState).includes('name')}
                        required
                        sx={{ mb: 1, mt: 1 }}
                    />
                    <TextField
                        id='area'
                        label='Area'
                        type='number'
                        error={validateForm(flatState).includes('area')}
                        required
                        value={flatState.area}
                        onChange={e => setFlatState({ ...flatState, area: parseInt(e.target.value) })}
                        sx={{ mb: 1, mt: 1 }}
                    />
                    <TextField
                        id='numberOfRooms'
                        label='Number of rooms'
                        type='number'
                        error={validateForm(flatState).includes('numberOfRooms')}
                        required
                        value={flatState.numberOfRooms}
                        onChange={e => setFlatState({ ...flatState, numberOfRooms: parseInt(e.target.value) })}
                        sx={{ mb: 1, mt: 1 }}
                    />
                    <TextField
                        id='price'
                        label='Price'
                        type='number'
                        error={validateForm(flatState).includes('price')}
                        required
                        value={flatState.price}
                        onChange={e => setFlatState({ ...flatState, price: parseInt(e.target.value) })}
                        sx={{ mb: 1, mt: 1 }}
                    />
                    <Autocomplete
                        disablePortal
                        id="furnish"
                        options={Object.keys(Furnish)}
                        value={flatState.furnish}
                        onChange={(e, val) => setFlatState({ ...flatState, furnish: val == null ? Furnish.NONE : val as Furnish })}
                        renderInput={(params) =>
                            <TextField {...params} label={"Furnish"}
                                error={validateForm(flatState).includes('furnish')} required />}
                        sx={{ mb: 1, mt: 1 }}
                    />
                    <Autocomplete
                        disablePortal
                        id="transport"
                        options={Object.keys(Transport)}
                        value={flatState.transport}
                        onChange={(e, val) => setFlatState({ ...flatState, transport: val == null ? Transport.NONE : val as Transport })}
                        renderInput={(params) =>
                            <TextField {...params} label={"Transport"}
                                error={validateForm(flatState).includes('transport')} required />}
                        sx={{ mb: 1, mt: 1 }}
                    />
                    <Autocomplete
                        disablePortal
                        id="view"
                        options={Object.keys(View)}
                        value={flatState.view}
                        onChange={(e, val) => setFlatState({ ...flatState, view: val == null ? View.NORMAL : val as View })}
                        renderInput={(params) =>
                            <TextField {...params} label={"View"}
                                error={validateForm(flatState).includes('view')} required />}
                        onFocus={()=>console.log(queryClient.getQueryData(reactQueryKeys.getAllHouses))}
                        sx={{ mb: 1, mt: 1 }}
                    />
                    <LabeledBox label='Coordinates'>
                        <Box
                            sx={{
                                display: 'grid',
                                gridTemplateColumns: { md: '1fr 1fr' },
                                gap: 2,
                            }}
                        >
                            <TextField
                                type='number'
                                id='coordinateX'
                                label='X'
                                required

                                error={validateForm(flatState).includes('coordinateX')}
                                value={flatState.coordinates.coordinate_x}
                                onChange={e => setFlatState({ ...flatState, coordinates: { ...flatState.coordinates, coordinate_x: parseInt(e.target.value) } })} />
                            <TextField
                                type='number'
                                id='coordinateY'
                                label='Y'
                                error={validateForm(flatState).includes('coordinateY')}
                                required
                                value={flatState.coordinates.coordinate_y}
                                onChange={e => setFlatState({ ...flatState, coordinates: { ...flatState.coordinates, coordinate_y: parseInt(e.target.value) } })} />
                        </Box>
                    </LabeledBox>
                    <TextField fullWidth
                        id='houseName'
                        label='House Name'
                        error={validateForm(flatState).includes('houseName')}
                        value={flatState.house.name}
                        onChange={e => setFlatState({ ...flatState, house: { ...flatState.house, name: e.target.value } })}
                    />
                    <FormControlLabel control={<Switch />} label="Has balcony" value={flatState.hasBalcony} onChange={(e, val) => setFlatState({ ...flatState, hasBalcony: val })} sx={{ alignSelf: 'center' }} />
                </FormControl>
                <Button variant='contained' sx={{ width: '70%', m: 4 }} type='submit'>Send</Button>
            </form>
        </Container>
    )
}
