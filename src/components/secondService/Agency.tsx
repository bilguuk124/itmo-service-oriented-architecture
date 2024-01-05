import { Box, Divider, FormControl, FormControlLabel, Stack, Switch, TextField, Typography, Button, Paper } from "@mui/material"
import React, { useEffect, useState } from "react"
import { reactQueryKeys } from "../../constants"
import Flat, { FedbackableProps, Feedback } from "../../types";
import { AgencyService } from "../../services/AgencyService";
import { useQuery } from "@tanstack/react-query";
import { FlatComparison } from "./FlatComparison";
import { buildFeedback, isFlatExist } from "../../utils";
import axios, { AxiosError } from "axios";
import { FlatService } from "../../services/FlatsService";
import JSONFormatter from "json-formatter-js";

interface AgencyToolsProps extends FedbackableProps {
}

export const AgencyTools: React.FC<AgencyToolsProps> = ({ setFeedback }) => {

    return (
        <Box sx={{ color: 'black' }}>
            <GetCheapest setFeedback={setFeedback} />
            <Divider sx={{ m: 3 }} />
            <FindFlat setFeedback={setFeedback} />
        </Box>
    )
}

const GetCheapest: React.FC<FedbackableProps> = ({ setFeedback }) => {
    const [flatParams, setFlatParams] = useState({
        firstFlatId: 0,
        secondFlatId: 0
    })
    const [cheapestFlat, setCheapestFlat] = useState<Flat | undefined>(undefined)
    const [nonCheapestFlat, setNonCheapestFlat] = useState<Flat | undefined>()

    const compareFlats = (e: React.SyntheticEvent) => {
        e.preventDefault();
        AgencyService.compareFlats(flatParams.firstFlatId, flatParams.secondFlatId).then(flat => { setCheapestFlat({ ...flat }) }).catch((a) => {
            console.log(a)
            setFeedback(buildFeedback('error', undefined, a as AxiosError))
        })
    }

    const validateForm = (form: any): string[] => {
        const res = ['']
        if (isNaN(form.firstFlatId))
            res.push('firstFlatId')
        if (isNaN(form.secondFlatId))
            res.push('secondFlatId')
        return res
    }

    useEffect(() => {
        if (!cheapestFlat)
            return
        console.log(cheapestFlat);
        FlatService.get(cheapestFlat.id === flatParams.firstFlatId ? flatParams.secondFlatId : flatParams.firstFlatId)
            .then(flat => setNonCheapestFlat(flat))
            .catch((a) => setFeedback(buildFeedback('error', undefined, a as AxiosError)))

    }, [cheapestFlat])
    return (
        <>
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
            {
                cheapestFlat && nonCheapestFlat ?
                    <FlatComparison
                        firstFlat={cheapestFlat.id == flatParams.firstFlatId ? cheapestFlat : nonCheapestFlat}
                        secondFlat={nonCheapestFlat.id == flatParams.secondFlatId ? nonCheapestFlat : cheapestFlat}
                        cheapestFlatId={cheapestFlat.id} />
                    : undefined
            }
        </>
    )
}

const FindFlat: React.FC<FedbackableProps> = ({ setFeedback }) => {
    const [flatParams, setFlatParams] = useState({
        hasBalcony: false,
        isCheapest: false
    })
    const [foundFlat, setfoundFlat] = useState<Flat>()

    const findFlat = (e: React.SyntheticEvent) => {
        e.preventDefault();
        try {
            AgencyService.findWithBalcony(flatParams.isCheapest, flatParams.hasBalcony)
                .then(flat => { setfoundFlat({ ...flat }) })
                .catch((a) => setFeedback(buildFeedback('error', undefined, a as AxiosError)))
        }
        catch (err: any) {
            console.log(err)
            setFeedback(buildFeedback('error', undefined, err as AxiosError))
        }

    }
    return (
        <>
            <form onSubmit={findFlat}>
                <FormControl fullWidth sx={{ maxWidth: 500 }}>
                    <Typography variant="h5">Find best flat</Typography>
                    <Stack spacing={2} sx={{ m: 1, mb: 2 }} justifyItems='center' alignItems='center'>
                        <Stack direction="row" alignItems="center">
                            <Typography>Without balcony</Typography>
                            <Switch
                                value={flatParams.hasBalcony}
                                onChange={(e, val) => setFlatParams({ ...flatParams, hasBalcony: val })} />
                            <Typography>With balcony</Typography>
                        </Stack>
                        <Stack direction="row" alignItems="center">
                            <Typography> &nbsp;	&nbsp; &nbsp; &nbsp; &nbsp;	Cheapest</Typography>
                            <Switch
                                value={flatParams.isCheapest}
                                onChange={(e, val) => setFlatParams({ ...flatParams, isCheapest: val })} />
                            <Typography>Expensive &nbsp; &nbsp;</Typography>
                        </Stack>
                    </Stack>
                    <Button variant='contained' color='primary' sx={{ width: '10vw', alignSelf: 'center' }} type="submit">Find</Button>
                </FormControl>
            </form>
            {foundFlat && <Paper 
                elevation={2}
                sx={{ textAlign: 'left', p: 2, m:2, maxWidth: 800, display: 'inline-grid' , alignSelf: 'center', alignContent: 'center' }}
                ref={ref => ref?.replaceChildren(new JSONFormatter(foundFlat).render())}>
            </Paper>}
        </>
    )
}

