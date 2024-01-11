import { SyntheticEvent, useState } from "react";
import { FlatService } from "../../services/FlatsService";
import { buildFeedback } from "../../utils";
import { IconButton, Menu, Stack, TextField, Typography } from "@mui/material";
import { FedbackableProps } from "../../types";
import ConstructionIcon from '@mui/icons-material/Construction';

export const CustomDataGridToolbar: React.FC<FedbackableProps> = ({ setFeedback }) => {
    const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
    const [roomsNumber, setRoomsNumber] = useState(0)
    const open = Boolean(anchorEl);
    const handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
      setAnchorEl(event.currentTarget);
    };
    const handleClose = () => {
      setAnchorEl(null);
    };
  
    const handleSubmit = (e: SyntheticEvent) => {
      e.preventDefault()
      FlatService.countWithLessRoomsNumber(roomsNumber)
        .then(flatsNumber => `There are ${flatsNumber} flats which has less ${roomsNumber} rooms `)
        .then(message => setFeedback(buildFeedback('info', message)))
        .catch(err => setFeedback(buildFeedback('info', undefined, err)))
    }
  
    return (
      <div>
        <IconButton
          id="basic-button"
          aria-controls={open ? 'basic-menu' : undefined}
          aria-haspopup="true"
          aria-expanded={open ? 'true' : undefined}
          onClick={handleClick}
        >
          <ConstructionIcon color='primary' sx={{ fontSize: 20 }} />
        </IconButton>
        <Menu
          id="basic-menu"
          anchorEl={anchorEl}
          open={open}
          onClose={handleClose}
        >
          <form onSubmit={handleSubmit}>
            <Stack sx={{ m: 1 }}>
              <Typography>Count flats with less rooms number</Typography>
              <TextField
                label="Rooms number"
                type='number'
                variant="filled"
                size='small'
                value={roomsNumber}
                onChange={(val) => setRoomsNumber(parseInt(val.target.value))} />
            </Stack>
          </form>
        </Menu>
      </div>
    );
  }