import { Box, Divider, FormControl, FormControlLabel, Stack, Switch, TextField, Typography, Button } from "@mui/material"
import React, { useEffect, useState } from "react"
import { reactQueryKeys } from "../../constants"
import Flat, { Feedback } from "../../types";
import { AgencyService } from "../../services/AgencyService";
import { useQuery } from "@tanstack/react-query";
import { FlatComparison } from "./FlatComparison";
import { buildFeedback, isFlatExist } from "../../utils";
import axios, { AxiosError } from "axios";
import { FlatService } from "../../services/FlatsService";

type FormData = {
    firstFlatId: number;
    secondFlatId: number;
    hasBalcony: boolean;
    isCheapest: boolean
}

const validateForm = (form: FormData): string[] => {
    const res = ['']
    // var isFirstExist, isSecondExist;
    // FlatService.isExist(form.firstFlatId).then(a => isFirstExist = a)
    // FlatService.isExist(form.secondFlatId).then(a => isSecondExist  =a)
    if (Number.isNaN(form.firstFlatId))
        res.push('firstFlatId')
    if (Number.isNaN(form.secondFlatId))
        res.push('secondFlatId')
    return res
}

interface AgencyToolsProps {
    setFeedback: React.Dispatch<React.SetStateAction<Feedback>>
}

export const AgencyTools: React.FC<AgencyToolsProps> = ({ setFeedback }) => {
    const [flatParams, setFlatParams] = useState({
        firstFlatId: 0,
        secondFlatId: 0,
        hasBalcony: false,
        isCheapest: false
    })
    const [cheapestFlat, setCheapestFlat] = useState<Flat | undefined>(undefined)
    const [nonCheapestFlat, setNonCheapestFlat] = useState<Flat | undefined>()

    const findFlat = (e: React.SyntheticEvent) => {
        e.preventDefault();

    }

    const compareFlats = (e: React.SyntheticEvent) => {
        e.preventDefault();
        try {
            AgencyService.compareFlats(flatParams.firstFlatId, flatParams.secondFlatId).then(flat => {
                console.log(flat);
                ; setCheapestFlat({ ...flat })
            })
        }
        catch (err) {
            console.log(err)
            setFeedback(buildFeedback('error', undefined, err as AxiosError))
        }
    }

    useEffect(() => {
        if (!cheapestFlat)
            return

        try {
            console.log(cheapestFlat);
            FlatService.get(cheapestFlat.id === flatParams.firstFlatId ? flatParams.secondFlatId : flatParams.firstFlatId).then(flat => setNonCheapestFlat(flat))
        }
        catch (err) {
            console.log(err)
            setFeedback(buildFeedback('error', undefined, err as AxiosError))
        }

    }, [cheapestFlat])

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
                            error={validateForm(flatParams).includes('secondFlatId')}
                            value={flatParams.secondFlatId}
                            type="number"
                            onChange={e => setFlatParams({ ...flatParams, secondFlatId: parseInt(e.target.value) })}></TextField>
                    </Stack>
                    <Button variant='contained' color='primary' sx={{ width: '10vw', alignSelf: 'center' }} type="submit" >Compare</Button>
                </FormControl>
            </form>
            {cheapestFlat && nonCheapestFlat ?
                <FlatComparison
                    firstFlat={cheapestFlat.id == flatParams.firstFlatId ? cheapestFlat : nonCheapestFlat}
                    secondFlat={nonCheapestFlat.id == flatParams.secondFlatId ? nonCheapestFlat : cheapestFlat}
                    cheapestFlatId={cheapestFlat.id} />
                : undefined}
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

