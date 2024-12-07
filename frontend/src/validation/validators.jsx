import dayjs from 'dayjs';
import isSameOrBefore from 'dayjs/plugin/isSameOrBefore';

dayjs.extend(isSameOrBefore);
export const validatePhoneNumber = (phone) => {
    const phoneRegex = /^\d{9}$/;
    return phoneRegex.test(phone);
};

export const validatePassword = (password) => {
    const digitRegex = /\d/;      // sprawdza czy jest co najmniej jedna cyfra
    const uppercaseRegex = /[A-Z]/; // sprawdza czy jest co najmniej jedna wielka litera
    return digitRegex.test(password) && uppercaseRegex.test(password);
};

export const validateTitle = (title) => {
    return title.trim() !== '' && title.length <= 255;
};

export const validateAuthor = (author) => {
    if (author.trim() === '') return false;
    const authors = author.split(',').map(a => a.trim());
    return authors.every(a => a.split(' ').length >= 2); // Każdy autor powinien mieć imię i nazwisko
};

export const validateISBN = (isbn) => {
    // ISBN musi mieć dokładnie 13 cyfr
    const isbnRegex = /^\d{13}$/;
    return isbnRegex.test(isbn);
};

export const validateCategories = (categories) => {
    if (categories.trim() === '') return false;
    const cats = categories.split(',').map(c => c.trim());
    return cats.every(c => c !== '');
};

export const validatePublisher = (publisher) => {
    return publisher.trim() !== '' && publisher.length <= 255;
};

export const validateLanguage = (language) => {
    if (language.trim() === '') return false;
    const languageRegex = /^[A-Za-z\s]+$/; // Tylko litery i spacje
    return languageRegex.test(language) && language.length <= 255;
};

export const validateDescription = (description) => {
    if (description.trim() === '') return false;
    return description.length <= 3000;
};

export const validateFile = (file) => {
    if(!file) return false;
    // Plik musi być w formacie jpg lub png
    const validTypes = ['image/jpeg', 'image/png'];
    return validTypes.includes(file.type);
};