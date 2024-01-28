/**
 *
 * @param word The word to pluralize if necessary
 * @param nb The number which preceeds the word
 * @returns The word pluralized if it was necessary
 */
export const pluralize = (word: string, nb: number) => `${word}${nb > 1 ? 's' : ''}`;

/**
 *
 * @param price The price to formatter
 * @param currency
 * @returns The formatted price
 */
export const priceFormatter = (price: number, currency: string): string => {
    const formatOptions = {
        style: 'currency',
        currency: currency === 'USD' ? 'USD' : 'EUR',
        minimumFractionDigits: 2,
    };

    // Adjust the locale if needed
    return new Intl.NumberFormat('fr-FR', formatOptions).format(price);
};