import { Button, Paper } from "@mui/material"
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

    const [houseState, setHouseState] = useState<House | undefined>(undefined)

    const sendTestPostRequest = (e: React.SyntheticEvent) => {
        e.preventDefault();
        AgencyService.testPost().then(a => setHouseState(a)).catch((a) => {
            console.log(a)
            setFeedback(buildFeedback('error', undefined, a as AxiosError))
        })
    }


    return (
        <>
            <Button variant='contained' color='primary' sx={{ width: '10vw', alignSelf: 'center' }} type="submit" onClick={sendTestPostRequest}>Test Post</Button>
            {
                houseState ?
                    <Paper
                        elevation={1}
                        sx={{ textAlign: 'left', p: 2 }}
                        ref={ref => ref?.replaceChildren(new JSONFormatter(houseState).render())}>
                    </Paper>
                    :
                    undefined
            }
        </>
    )

}