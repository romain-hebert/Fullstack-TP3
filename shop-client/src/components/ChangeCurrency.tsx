import { IconButton, Menu, MenuItem } from '@mui/material';
import { useState } from 'react';
import CurrencyIcon from '@mui/icons-material/MonetizationOn';
import Currency from '../types/currency';
import { useAppContext } from '../context';

const ChangeCurrency = () => {
    const { setCurrency, currency } = useAppContext();
    const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);

    const handleClick = (currency: Currency) => {
        setCurrency(currency); // Replace setLocale with a method to set currency
        handleClose();
    };

    const handleMenu = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };

    return (
        <div>
            <IconButton size="large" onClick={handleMenu} color="inherit">
                <CurrencyIcon />
            </IconButton>
            <Menu
                sx={{ mt: '35px' }}
                id="menu-appbar"
                anchorEl={anchorEl}
                anchorOrigin={{
                    vertical: 'top',
                    horizontal: 'right',
                }}
                keepMounted
                transformOrigin={{
                    vertical: 'top',
                    horizontal: 'right',
                }}
                open={Boolean(anchorEl)}
                onClose={handleClose}
            >
                <MenuItem
                    onClick={() => handleClick(Currency.USD)} // Replace with currency values
                    sx={{ backgroundColor: currency === 'USD' ? '#f2f5f6' : 'white' }}
                >
                    USD
                </MenuItem>
                <MenuItem
                    onClick={() => handleClick(Currency.EUR)}
                    sx={{ backgroundColor: currency === 'EUR' ? '#f2f5f6' : 'white' }}
                >
                    EUR
                </MenuItem>
            </Menu>
        </div>
    );
};

export default ChangeCurrency;
