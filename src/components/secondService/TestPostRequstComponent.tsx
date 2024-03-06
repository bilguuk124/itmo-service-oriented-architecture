import { Button, Paper, Stack, Typography } from "@mui/material"
import { FedbackableProps, House } from "../../types"
import { useMutation, useQueryClient } from "@tanstack/react-query"
import { reactQueryKeys } from "../../constants"
import { HouseService } from "../../services/HouseService"
import { buildFeedback } from "../../utils"
import { useState } from "react"
import { AgencyService } from "../../services/AgencyService"
import { AxiosError } from "axios"
import JSONFormatter from "json-formatter-js"

export const TestPostRequestToAgensyServiceComponent: React.FC<FedbackableProps> = ({ setFeedback }) => {
    const queryClient = useQueryClient()
    const houseToPost: House = { name: "house", year: 2014, numberOfFloors: 12 }

    const [respState, setRespState] = useState<House | undefined>(undefined)

    const sendTestPostRequest = (e: React.SyntheticEvent) => {
        e.preventDefault();
        AgencyService.testPost(houseToPost).then(a => setRespState(a)).catch((a) => {
            console.log(a)
            setFeedback(buildFeedback('error', undefined, a as AxiosError))
        })
    }


    return (
        <>
            <Button variant='contained' color='primary' sx={{ width: '10vw', alignSelf: 'center' }} type="submit" onClick={sendTestPostRequest}>Test Post</Button>
            <Stack direction='row' spacing={2} sx={{ m: 2 }} justifyContent='center' useFlexGap flexWrap="wrap">
                <Paper
                    elevation={1}
                    sx={{ textAlign: 'left', p: 2 }}>
                    <>

                        <Typography variant="subtitle1">Request</Typography>
                        <div ref={ref => ref?.replaceChildren(new JSONFormatter(houseToPost).render())}></div>
                    </>
                </Paper>
                {
                    respState ?
                        <Paper elevation={1} sx={{ textAlign: 'left', p: 2 }}>
                            <>
                                <Typography variant="subtitle1">Response</Typography>
                                <div ref={ref => ref?.replaceChildren(new JSONFormatter(respState).render())}></div>
                            </>
                        </Paper>
                        :
                        undefined
                }
            </Stack>

        </>
    )

}