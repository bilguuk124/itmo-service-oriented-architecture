import { Box, Divider } from "@mui/material"
import React from "react"
import { FedbackableProps } from "../../types";
import { GetCheapestComponent } from "./GetCheapestComponent";
import { FindFlatComponent } from "./FindFlatComponent";
import { TestPostRequestToAgensyServiceComponent } from "./TestPostRequstComponent";

interface AgencyToolsProps extends FedbackableProps {
}

export const AgencyToolsComponent: React.FC<AgencyToolsProps> = ({ setFeedback }) => {

    return (
        <Box sx={{ color: 'black' }}>
            <GetCheapestComponent setFeedback={setFeedback} />
            <Divider sx={{ m: 3 }} />
            <FindFlatComponent setFeedback={setFeedback} />
            <Divider sx={{ m: 3 }} />
            <TestPostRequestToAgensyServiceComponent setFeedback={setFeedback}/>
        </Box>
    )
}






