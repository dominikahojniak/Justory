import React, { useState, useEffect } from 'react';
import '../pages_css/addBook.css';
import Footer from '../components/Footer/Footer.jsx';
import Header from '../components/Header/Header.jsx';
import AccessTypeComponent from "../components/accessTypes/AccessTypeComponent.jsx";
import axios from '../../axiosConfig.js';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import InputFileUpload from "../components/InputFileUpload.jsx";
import dayjs from 'dayjs';
import {validateISBN, validateDescription, validateFile, validateTitle, validateAuthor, validateCategories, validatePublisher, validateLanguage} from '../validation/validators.jsx';

const AddBook = () => {
    const [platforms, setPlatforms] = useState([]);
    const [formats, setFormats] = useState([]);
    const [accessTypes, setAccessTypes] = useState([]);
    const [availability, setAvailability] = useState({});
    const [title, setTitle] = useState('');
    const [author, setAuthor] = useState('');
    const [ISBN, setISBN] = useState('');
    const [categories, setCategories] = useState('');
    const [date, setDate] = useState(dayjs());
    const [language, setLanguage] = useState('');
    const [description, setDescription] = useState('');
    const [publisher, setPublisher] = useState('');
    const [file, setFile] = useState(null);
    const [errors, setErrors] = useState({
        title: false,
        author: false,
        language: false,
        description: false,
        ISBN: false,
        file: false,
    });

    useEffect(() => {
        const fetchData = async () => {
            try {
                const platformsResponse = await axios.get('/api/platforms');
                const formatsResponse = await axios.get('/api/formats');
                const accessTypesResponse = await axios.get('/api/access-types');

                setPlatforms(platformsResponse.data);
                setFormats(formatsResponse.data);
                setAccessTypes(accessTypesResponse.data);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchData();
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (errors.ISBN || errors.file || errors.title || errors.author || errors.language || errors.description) {
            alert('Proszę poprawić błędy walidacji.');
            return;
        }
        if (!title || !author || !ISBN || !date || !language || !description || !file) {
            alert('Proszę wypełnić wszystkie pola.');
            return;
        }


        try {
            const formData = new FormData();

            formData.append('file', file);

            const bookDTO = {
                title: title,
                authors: author.split(',').map(a => {
                    const [firstName, lastName] = a.trim().split(' ');
                    return { firstName, lastName };
                }),
                isbn: ISBN,
                language: language,
                description: description,
                publisher: { name: publisher },
                date: date.format('YYYY-MM-DD'),
                categories: categories.split(',').map(c => ({ name: c.trim() })),
                availabilities: Object.keys(availability).map(platformName => {
                    return Object.keys(availability[platformName]).map(formatName => {
                        return Object.keys(availability[platformName][formatName]).map(accessTypeName => {
                            const platform = platforms.find(p => p.name === platformName);
                            const format = formats.find(f => f.name === formatName);
                            const accessType = accessTypes.find(a => a.name === accessTypeName);

                            return {
                                platformName: platform ? platform.name : 'Unknown platform',
                                formatName: format ? format.name : 'Unknown format',
                                accessTypeName: accessType ? accessType.name : 'Unknown access type'
                            };
                        });
                    });
                }).flat(2)
            };

            formData.append('bookDTO', JSON.stringify(bookDTO));

            const response = await axios.post('/api/books/add', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });

            console.log('Book added successfully:', response.data);
            alert('Książka została pomyślnie dodana!');
        } catch (error) {
            console.error('Error adding book:', error);
            alert('Błąd podczas dodawania książki!')
        }
    };

    const handleTitleChange = (e) => {
        const value = e.target.value;
        setTitle(value);
        setErrors(prev => ({ ...prev, title: !validateTitle(value) }));
    };

    const handleAuthorChange = (e) => {
        const value = e.target.value;
        setAuthor(value);
        setErrors(prev => ({ ...prev, author: !validateAuthor(value) }));
    };

    const handleISBNChange = (e) => {
        const value = e.target.value;
        setISBN(value);
        setErrors(prev => ({ ...prev, ISBN: !validateISBN(value) }));
    };

    const handleCategoriesChange = (e) => {
        const value = e.target.value;
        setCategories(value);
        setErrors(prev => ({ ...prev, categories: !validateCategories(value) }));
    };

    const handlePublisherChange = (e) => {
        const value = e.target.value;
        setPublisher(value);
        setErrors(prev => ({ ...prev, publisher: !validatePublisher(value) }));
    };

    const handleDateChange = (newValue) => {
        setDate(newValue);
    };

    const handleLanguageChange = (e) => {
        const value = e.target.value;
        setLanguage(value);
        setErrors(prev => ({ ...prev, language: !validateLanguage(value) }));
    };

    const handleDescriptionChange = (e) => {
        const value = e.target.value;
        setDescription(value);
        setErrors(prev => ({ ...prev, description: !validateDescription(value) }));
    };

    const handleFileChange = (e) => {
        const selectedFile = e.target.files[0];
        setFile(selectedFile);
        setErrors(prev => ({ ...prev, file: !validateFile(selectedFile) }));
    };
    return (
        <div className="addbook-container">
            <Header activePage="addbook" />
            <main className='main-addbook'>
                <div className="add">
                    Dodaj Książkę
                </div>
                <div className="addbook">
                    <form className="form-addbook" onSubmit={handleSubmit}>
                        <input name="title" type="text" placeholder="Tytuł" id="title" value={title}
                               onChange={handleTitleChange} className={errors.title ? 'error' : ''}/>
                        {errors.title &&
                            <p className="error-message">Tytuł jest wymagany i nie może przekraczać 255 znaków.</p>}
                        <input name="author" type="text" placeholder="Autor (oddziel przecinkiem)" id="author"
                               value={author} onChange={handleAuthorChange} className={errors.author ? 'error' : ''}/>
                        {errors.author &&
                            <p className="error-message">Autor jest wymagany. Podaj imię i nazwisko każdego autora,
                                oddzielone przecinkiem.</p>}
                        <input name="ISBN" type="text" placeholder="ISBN" id="ISBN" value={ISBN}
                               onChange={handleISBNChange} className={errors.ISBN ? 'error' : ''}/>
                        {errors.ISBN && <p className="error-message">ISBN musi składać się z 13 cyfr.</p>}
                        <input name="categories" type="text" placeholder="Kategoria (oddziel przecinkiem)"
                               id="categories" value={categories} onChange={handleCategoriesChange}
                               className={errors.categories ? 'error' : ''}/>
                        {errors.categories &&
                            <p className="error-message">Kategoria jest wymagana. Podaj niepuste nazwy kategorii
                                oddzielone przecinkiem.</p>}
                        <input name="publisher" type="text" placeholder="Wydawnictwo" id="publisher" value={publisher}
                               onChange={handlePublisherChange} className={errors.publisher ? 'error' : ''}/>
                        {errors.publisher &&
                            <p className="error-message">Wydawnictwo jest wymagane i nie może przekraczać 255
                                znaków.</p>}
                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                            <div className="date-picker-container">
                                <DatePicker
                                    label="Wybierz datę"
                                    value={date}
                                    onChange={handleDateChange}
                                    renderInput={(params) => (
                                        <input
                                            {...params}
                                        />
                                    )}
                                />
                            </div>
                        </LocalizationProvider>
                        <input name="language" type="text" placeholder="Język" id="language" value={language}
                               onChange={handleLanguageChange} className={errors.language ? 'error' : ''}/>
                        {errors.language &&
                            <p className="error-message">Język jest wymagany, nie może zawierać cyfr i nie może
                                przekraczać 255 znaków.</p>}
                        <textarea name="description" placeholder="Opis" id="description" value={description} onChange={handleDescriptionChange}
                            className={errors.description ? 'error' : ''} maxLength="3000" aria-invalid={errors.description} aria-describedby="description-error"
                        ></textarea>
                        <p className="char-counter">{description.length}/3000 znaków</p>
                        {errors.description &&
                            <p className="error-message">Opis jest wymagany, może mieć maksymalnie 3000 znaków.</p>}
                        <InputFileUpload onChange={handleFileChange} className={errors.file ? 'error' : ''}/>
                        {errors.file && <p className="error-message">Zdjęcie musi być w formacie JPG lub PNG.</p>}
                        <h3>Dodaj dostępności</h3>
                        <AccessTypeComponent
                            platforms={platforms}
                            formats={formats}
                            accessTypes={accessTypes}
                            availability={availability}
                            setAvailability={setAvailability}
                        />
                        <button type="submit" id="add-button">DODAJ KSIĄŻKĘ</button>
                    </form>
                </div>
            </main>
            <Footer/>
        </div>
    );
};

export default AddBook;