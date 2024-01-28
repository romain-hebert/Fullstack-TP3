import { createContext, useContext, useState } from 'react';
import Locale from '../types/locale';
import Currency from "../types/currency";

interface AppContextInterface {
    loading: boolean;
    setLoading: (load: boolean) => void;
    locale: Locale;
    setLocale: (locale: Locale) => void;
    currency: Currency;
    setCurrency: (currency: Currency) => void;
}

const AppContext = createContext<AppContextInterface>({
    loading: false,
    setLoading: () => {
        // empty function
    },
    locale: Locale.FR,
    setLocale: () => {
        // empty function
    },
    currency: Currency.EUR,
    setCurrency: () => {
        // empty function
    },
});

type Props = {
    children: JSX.Element;
};

export function AppProvider({ children }: Props) {
    const [loading, setLoading] = useState<boolean>(false);
    const [locale, setLocale] = useState<Locale>(Locale.FR);
    const [currency, setCurrency] = useState<Currency>(Currency.EUR);


    return (
        <AppContext.Provider
            value={{
                loading,
                setLoading,
                locale,
                setLocale,
                currency,
                setCurrency,
            }}
        >
            {children}
        </AppContext.Provider>
    );
}

export const useAppContext = () => useContext(AppContext);
