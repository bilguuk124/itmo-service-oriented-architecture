import { FormControl, Stack, TextField, Typography, Button } from "@mui/material"
import React, { memo, useEffect, useState } from "react"
import Flat, { FedbackableProps} from "../../types";
import { AgencyService } from "../../services/AgencyService";
import { FlatsComparisonPanel } from "./FlatComparisonPanel";
import { buildFeedback } from "../../utils";
import { AxiosError } from "axios";
import { FlatService } from "../../services/FlatsService";

export const GetCheapestComponent: React.FC<FedbackableProps> = ({ setFeedback }) => {
    const [flatParams, setFlatParams] = useState({
        firstFlatId: 0,
        secondFlatId: 0
    })
    const [cheapestFlat, setCheapestFlat] = useState<Flat | undefined>(undefined)
    const [nonCheapestFlat, setNonCheapestFlat] = useState<Flat | undefined>()

    const compareFlats = (e: React.SyntheticEvent) => {
        e.preventDefault();
        AgencyService.compareFlats(flatParams.firstFlatId, flatParams.secondFlatId).then(flat => { setCheapestFlat((prev) => prev = flat) }).catch((a) => {
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
        FlatService.get(cheapestFlat.id == flatParams.firstFlatId ? flatParams.secondFlatId : flatParams.firstFlatId)
            .then(flat => setNonCheapestFlat(flat))
            .catch((a) => setFeedback(buildFeedback('error', undefined, a as AxiosError)))

    }, [cheapestFlat])

    const ShowComparison = memo<{cheapestFlat: Flat | undefined}>(({cheapestFlat}) => {
        if (cheapestFlat && nonCheapestFlat)
            return (<FlatsComparisonPanel
                firstFlat={cheapestFlat.id == flatParams.firstFlatId ? cheapestFlat : nonCheapestFlat}
                secondFlat={cheapestFlat.id == flatParams.secondFlatId ? cheapestFlat : nonCheapestFlat}
                cheapestFlatId={cheapestFlat.id} />)
        return null
    })

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
                            onChange={e => setFlatParams((prev) => { return { ...prev, firstFlatId: parseInt(e.target.value) } })}></TextField>
                        <TextField
                            id='secondFlatId'
                            label='Second flat'
                            error={validateForm(flatParams).includes('secondFlatId')}
                            value={flatParams.secondFlatId}
                            type="number"
                            onChange={e => setFlatParams((prev) => { return { ...prev, secondFlatId: parseInt(e.target.value) } })}></TextField>
                    </Stack>
                    <Button variant='contained' color='primary' sx={{ width: '10vw', alignSelf: 'center' }} type="submit" >Compare</Button>
                </FormControl>
            </form>
            <ShowComparison cheapestFlat={cheapestFlat}></ShowComparison>
        </>
    )
}