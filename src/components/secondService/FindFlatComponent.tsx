import { FormControl, Stack, Switch, Typography, Button, Paper } from "@mui/material"
import React, { useState } from "react"
import Flat, { FedbackableProps } from "../../types";
import { AgencyService } from "../../services/AgencyService";
import { buildFeedback } from "../../utils";
import { AxiosError } from "axios";
import JSONFormatter from "json-formatter-js";

export const FindFlatComponent: React.FC<FedbackableProps> = ({ setFeedback }) => {
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
                sx={{ textAlign: 'left', p: 2, m: 2, maxWidth: 800, display: 'inline-grid', alignSelf: 'center', alignContent: 'center' }}
                ref={ref => ref?.replaceChildren(new JSONFormatter(foundFlat).render())}>
            </Paper>}
        </>
    )
}