import { Box, Divider, FormControl, FormControlLabel, Stack, Switch, TextField, Typography, Button } from "@mui/material"
import React from "react"
import { flatInitState } from "../../constants"
import { Feedback } from "../../types";

type FormData = {
    firstFlatId: number;
    secondFlatId: number;
    hasBalcony: boolean;
    isCheapest: boolean
}

const validateForm = (data: FormData): string[] => {
    const res = ['']
    return res
}

interface HouseTableProps {
    setFeedback: React.Dispatch<React.SetStateAction<Feedback>>
}

export const SecondService: React.FC<HouseTableProps> = ({ setFeedback }) => {
    const [flatState, setFlatState] = React.useState({
        firstFlatId: 0,
        secondFlatId: 0,
        hasBalcony: false,
        isCheapest: false
    })

    const findFlat = (e: React.SyntheticEvent) => {
        e.preventDefault();
    }

    const compareFlats = (e: React.SyntheticEvent) => {
        e.preventDefault();
    }

    return (
        <Box sx={{ color: 'black' }}>
            <form>
                <Typography variant="h5">Comparing flats</Typography>
                <FormControl>
                    <Stack direction='row' spacing={3} sx={{ m: 2 }}>
                        <TextField
                            id='firstFlatId'
                            label='First flat'
                            error={validateForm(flatState).includes('firstFlatId')}
                            value={flatState.firstFlatId}
                            type="number"
                            onChange={e => setFlatState({ ...flatState, firstFlatId: parseInt(e.target.value) })}></TextField>
                        <TextField
                            id='secondFlatId'
                            label='Second flat'
                            error={validateForm(flatState).includes('firstFlatId')}
                            value={flatState.secondFlatId}
                            type="number"
                            onChange={e => setFlatState({ ...flatState, secondFlatId: parseInt(e.target.value) })}></TextField>
                    </Stack>
                    <Button variant='contained' color='primary' sx={{ width: '10vw', alignSelf: 'center' }} >Compare</Button>
                </FormControl>
            </form>
            <Divider sx={{ m: 3 }} />
            <form onSubmit={findFlat}>
                <FormControl>
                    <Typography variant="h5">Find best flat</Typography>
                    <Stack direction='row' spacing={3} sx={{ m: 2 }}>
                        <FormControlLabel
                            control={<Switch />}
                            label="Has balcony"
                            value={flatState.hasBalcony}
                            onChange={(e, val) => setFlatState({ ...flatState, hasBalcony: val })}
                            sx={{ alignSelf: 'center', color: 'black' }} />

                        <FormControlLabel
                            control={<Switch />}
                            label="Is cheapest"
                            value={flatState.isCheapest}
                            onChange={(e, val) => setFlatState({ ...flatState, isCheapest: val })}
                            sx={{ alignSelf: 'center', color: 'black' }} />
                    </Stack>
                    <Button variant='contained' color='primary' sx={{ width: '10vw', alignSelf: 'center' }} type="submit">Find</Button>
                </FormControl>
            </form>
        </Box>
    )
}

