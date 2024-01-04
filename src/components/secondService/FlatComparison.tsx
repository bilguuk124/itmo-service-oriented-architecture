import { Paper, Box, Stack } from "@mui/material";
import Flat from "../../types";
import JSONFormatter from 'json-formatter-js'
import React from "react";

interface FlatComparisonProps {
    firstFlat: Flat,
    secondFlat: Flat,
    cheapestFlatId: number
}

export const FlatComparison: React.FC<FlatComparisonProps> = ({ firstFlat, secondFlat, cheapestFlatId }) => {
    return (
        <Stack direction='row' spacing={2} sx={{ m: 2 }} justifyContent='center' useFlexGap flexWrap="wrap">
            <Paper
                elevation={cheapestFlatId == firstFlat.id ? 20 : 1}
                sx={{ textAlign: 'left', p: 2, bgcolor: cheapestFlatId == firstFlat.id ? 'PaleGreen' : 'inherit' }}
                ref={ref => ref?.replaceChildren(new JSONFormatter(firstFlat).render())}>
            </Paper>
            <Paper
                elevation={cheapestFlatId == secondFlat.id ? 20 : 1}
                sx={{ textAlign: 'left', p: 2, bgcolor: cheapestFlatId == secondFlat.id ? 'PaleGreen' : 'inherit' }}
                ref={ref => ref?.replaceChildren(new JSONFormatter(secondFlat).render())}>
            </Paper>
        </Stack>
    )
}