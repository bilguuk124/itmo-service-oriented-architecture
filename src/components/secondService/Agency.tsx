import { Box, Divider, FormControl, FormControlLabel, Stack, Switch, TextField, Typography, Button, Paper, TableContainer, Table, TableHead, TableRow, TableCell, TableBody } from "@mui/material"
import React, { useState } from "react"
import { flatInitState, reactQueryKeys } from "../../constants"
import Flat, { Feedback } from "../../types";
import { AgencyService } from "../../services/AgencyService";
import { useQuery } from "@tanstack/react-query";

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
    const [flatParams, setFlatParams] = useState({
        firstFlatId: 0,
        secondFlatId: 0,
        hasBalcony: false,
        isCheapest: false
    })
    const [compareResult, setComparingResult] = useState<Flat | undefined>(undefined)

    const findFlat = (e: React.SyntheticEvent) => {
        e.preventDefault();

    }

    const compareFlats = (e: React.SyntheticEvent) => {
        e.preventDefault();
        const { data: resp } = useQuery({
            queryKey: [reactQueryKeys.compareFlats],
            queryFn: () => AgencyService.compareFlats(flatParams.firstFlatId, flatParams.secondFlatId)
        })
        setComparingResult(resp)
    }

    return (
        <Box sx={{ color: 'black' }}>
            <form onSubmit={compareFlats}>
                <Typography variant="h5">Comparing flats</Typography>
                <FormControl>
                    <Stack direction='row' spacing={3} sx={{ m: 2 }}>
                        <TextField
                            id='firstFlatId'
                            label='First flat'
                            error={validateForm(flatParams).includes('firstFlatId')}
                            value={flatParams.firstFlatId}
                            type="number"
                            onChange={e => setFlatParams({ ...flatParams, firstFlatId: parseInt(e.target.value) })}></TextField>
                        <TextField
                            id='secondFlatId'
                            label='Second flat'
                            error={validateForm(flatParams).includes('firstFlatId')}
                            value={flatParams.secondFlatId}
                            type="number"
                            onChange={e => setFlatParams({ ...flatParams, secondFlatId: parseInt(e.target.value) })}></TextField>
                    </Stack>
                    <Button variant='contained' color='primary' sx={{ width: '10vw', alignSelf: 'center' }} >Compare</Button>
                </FormControl>
            </form>
            {/* {
                compareResult ?
                    <TableContainer component={Paper}>
                        <Table sx={{ minWidth: 650 }} aria-label="simple table">
                            <TableHead>
                                <TableRow>
                                    <TableCell>Dessert (100g serving)</TableCell>
                                    <TableCell align="right">Calories</TableCell>
                                    <TableCell align="right">Fat&nbsp;(g)</TableCell>
                                    <TableCell align="right">Carbs&nbsp;(g)</TableCell>
                                    <TableCell align="right">Protein&nbsp;(g)</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {rows.map((row) => (
                                    <TableRow
                                        key={row.name}
                                        sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                                    >
                                        <TableCell component="th" scope="row">
                                            {row.name}
                                        </TableCell>
                                        <TableCell align="right">{row.calories}</TableCell>
                                        <TableCell align="right">{row.fat}</TableCell>
                                        <TableCell align="right">{row.carbs}</TableCell>
                                        <TableCell align="right">{row.protein}</TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                    :
                    undefined
            } */}
            <Divider sx={{ m: 3 }} />
            <form onSubmit={findFlat}>
                <FormControl>
                    <Typography variant="h5">Find best flat</Typography>
                    <Stack direction='row' spacing={3} sx={{ m: 2 }}>
                        <FormControlLabel
                            control={<Switch />}
                            label="Has balcony"
                            value={flatParams.hasBalcony}
                            onChange={(e, val) => setFlatParams({ ...flatParams, hasBalcony: val })}
                            sx={{ alignSelf: 'center', color: 'black' }} />

                        <FormControlLabel
                            control={<Switch />}
                            label="Is cheapest"
                            value={flatParams.isCheapest}
                            onChange={(e, val) => setFlatParams({ ...flatParams, isCheapest: val })}
                            sx={{ alignSelf: 'center', color: 'black' }} />
                    </Stack>
                    <Button variant='contained' color='primary' sx={{ width: '10vw', alignSelf: 'center' }} type="submit">Find</Button>
                </FormControl>
            </form>
        </Box>
    )
}

