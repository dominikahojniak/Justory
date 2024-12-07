import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from '../../axiosConfig.js';
import '../pages_css/editBook.css';
import Header from '../components/Header/Header.jsx';
import Footer from '../components/Footer/Footer.jsx';
import InputFileUpload from '../components/InputFileUpload.jsx';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import dayjs from 'dayjs';
import AccessTypeComponent from "../components/accessTypes/AccessTypeComponent.jsx";
import {validateISBN, validateDescription, validateFile, validateTitle, validateAuthor, validateCategories, validatePublisher, validateLanguage} from '../validation/validators.jsx';
const EditBook = () => {
    const { title } = useParams();
    const navigate = useNavigate();

    const [bookData, setBookData] = useState({
        title: '',
        authors: '',
        ISBN: '',
        language: '',
        description: '',
        date: dayjs(),
        publisher: '',
        categories: '',
        file: null,
    });

    const [platforms, setPlatforms] = useState([]);
    const [formats, setFormats] = useState([]);
    const [accessTypes, setAccessTypes] = useState([]);
    const [availability, setAvailability] = useState({});

    const [loading, setLoading] = useState(true);
    const [bookId, setBookId] = useState(null);
    const [errors, setErrors] = useState({
        title: false,
        author: false,
        language: false,
        description: false,
        ISBN: false,
        file: false,
        categories: false,
        publisher: false,
    });

    const transformAvailability = (availabilities) => {
        const transformed = {};
        availabilities.forEach(({ platformName, formatName, accessTypeName }) => {
            if (!transformed[platformName]) {
                transformed[platformName] = {};
            }
            if (!transformed[platformName][formatName]) {
                transformed[platformName][formatName] = {};
            }
            transformed[platformName][formatName][accessTypeName] = true;
        });
        return transformed;
    };

    const reverseTransformAvailability = (availability) => {
        const result = [];
        Object.keys(availability).forEach(platformName => {
            Object.keys(availability[platformName]).forEach(formatName => {
                Object.keys(availability[platformName][formatName]).forEach(accessTypeName => {
                    if (availability[platformName][formatName][accessTypeName]) {
                        result.push({
                            platformName,
                            formatName,
                            accessTypeName,
                        });
                    }
                });
            });
        });
        return result;
    };

    useEffect(() => {
        const fetchBookData = async () => {
            try {
                const response = await axios.get(`/api/books/title/${title}`);
                const book = response.data;
                setBookId(book.id);
                setBookData({
                    title: book.title,
                    authors: book.authors.map(a => `${a.firstName} ${a.lastName}`).join(', '),
                    isbn: book.isbn,
                    language: book.language,
                    description: book.description,
                    date: dayjs(book.date),
                    publisher: book.publisher.name,
                    categories: book.categories.map(c => c.name).join(', '),
                });

                const transformedAvailability = transformAvailability(book.availabilities);
                setAvailability(transformedAvailability);
            } catch (error) {
                console.error('Error fetching book data:', error);
            } finally {
                setLoading(false);
            }
        };

        const fetchAdditionalData = async () => {
            try {
                const [platformsRes, formatsRes, accessTypesRes] = await Promise.all([
                    axios.get('/api/platforms'),
                    axios.get('/api/formats'),
                    axios.get('/api/access-types'),
                ]);

                setPlatforms(platformsRes.data);
                setFormats(formatsRes.data);
                setAccessTypes(accessTypesRes.data);
            } catch (error) {
                console.error('Error fetching additional data:', error);
            }
        };


        fetchBookData();
        fetchAdditionalData();
    }, [title]);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setBookData(prev => ({ ...prev, [name]: value }));
    };


    const validateForm = () => {
        const requiredFields = ['title', 'authors', 'isbn', 'language', 'description', 'date', 'publisher'];
        for (let field of requiredFields) {
            if (!bookData[field]) {
                alert(`Pole "${field}" jest wymagane.`);
                return false;
            }
        }
        return true;
    };


    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!validateForm()) return;

        try {
            const formData = new FormData();
            if (bookData.file) {
                formData.append('file', bookData.file);
            } else {
                formData.append('file', new Blob(), 'empty.txt');
            }

            const updatedBook = {
                title: bookData.title,
                authors: bookData.authors.split(',').map(author => {
                    const [firstName, lastName] = author.trim().split(' ');
                    return { firstName, lastName };
                }),
                isbn: bookData.isbn,
                language: bookData.language,
                description: bookData.description,
                date: bookData.date.format('YYYY-MM-DD'),
                publisher: { name: bookData.publisher },
                categories: bookData.categories.split(',').map(c => ({ name: c.trim() })),
                availabilities: reverseTransformAvailability(availability),
            };

            formData.append('bookDTO', JSON.stringify(updatedBook));
            console.log('Updated Book:', updatedBook);
            await axios.put(`/api/books/edit/${bookId}`, formData, {
                headers: { 'Content-Type': 'multipart/form-data' },
            });

            alert('Książka została pomyślnie zaktualizowana!');
            navigate(`/book/${bookData.title}`);
        } catch (error) {
            console.error('Error updating book:', error);
            alert('Błąd podczas aktualizacji książki.');
        }
    };
    const handleTitleChange = (e) => {
        const value = e.target.value;
        setBookData(prev => ({ ...prev, title: value }));
        setErrors(prev => ({ ...prev, title: !validateTitle(value) }));
    };

    const handleAuthorChange = (e) => {
        const value = e.target.value;
        setBookData(prev => ({ ...prev, authors: value }));
        setErrors(prev => ({ ...prev, author: !validateAuthor(value) }));
    };

    const handleISBNChange = (e) => {
        const value = e.target.value;
        setBookData(prev => ({ ...prev, isbn: value }));
        setErrors(prev => ({ ...prev, ISBN: !validateISBN(value) }));
    };

    const handleCategoriesChange = (e) => {
        const value = e.target.value;
        setBookData(prev => ({ ...prev, categories: value }));
        setErrors(prev => ({ ...prev, categories: !validateCategories(value) }));
    };

    const handlePublisherChange = (e) => {
        const value = e.target.value;
        setBookData(prev => ({ ...prev, publisher: value }));
        setErrors(prev => ({ ...prev, publisher: !validatePublisher(value) }));
    };

    const handleLanguageChange = (e) => {
        const value = e.target.value;
        setBookData(prev => ({ ...prev, language: value }));
        setErrors(prev => ({ ...prev, language: !validateLanguage(value) }));
    };

    const handleDescriptionChange = (e) => {
        const value = e.target.value;
        setBookData(prev => ({ ...prev, description: value }));
        setErrors(prev => ({ ...prev, description: !validateDescription(value) }));
    };

    const handleFileChange = (e) => {
        const selectedFile = e.target.files[0];
        setBookData(prev => ({ ...prev, file: selectedFile }));
        setErrors(prev => ({ ...prev, file: !validateFile(selectedFile) }));
    };

    if (loading) return <p>Ładowanie...</p>;

    return (
        <div className="editbook-container">
            <Header activePage="editBook" />
            <main className="main-editbook">
                <div className="add">
                    Edytuj Książkę
                </div>
                <div className="addbook">
                    <form className="form-editbook" onSubmit={handleSubmit}>
                        <input name="title" type="text" placeholder="Tytuł" id="etitle" value={bookData.title}
                               onChange={handleTitleChange}
                               className={errors.title ? 'error' : ''}
                               aria-invalid={errors.title}
                               aria-describedby="title-error"/>
                        {errors.title && <p id="title-error" className="error-message">Tytuł jest wymagany i nie może przekraczać 255 znaków.</p>}
                        <input name="authors" type="text" placeholder="Autor (oddziel przecinkiem)" id="eauthor"
                               value={bookData.authors} onChange={handleAuthorChange} className={errors.author ? 'error' : ''} aria-invalid={errors.author} aria-describedby="author-error"/>
                        {errors.author && <p id="author-error" className="error-message">Autor jest wymagany. Podaj imię i nazwisko każdego autora, oddzielone przecinkiem.</p>}
                        <input name="ISBN" type="text" placeholder="ISBN" id="eISBN" value={bookData.isbn}
                               onChange={handleISBNChange} className={errors.ISBN ? 'error' : ''} aria-invalid={errors.ISBN} aria-describedby="isbn-error"/>
                        {errors.ISBN && <p id="isbn-error" className="error-message">ISBN musi składać się z 13 cyfr.</p>}
                        <input name="categories" type="text" placeholder="Kategoria (oddziel przecinkiem)" id="ecategories" value={bookData.categories}  onChange={handleCategoriesChange}
                               className={errors.categories ? 'error' : ''} aria-invalid={errors.categories} aria-describedby="categories-error"/>
                        {errors.categories && <p id="categories-error" className="error-message">Kategoria jest wymagana. Podaj niepuste nazwy kategorii oddzielone przecinkiem.</p>}
                        <input name="publisher" type="text" placeholder="Wydawnictwo" value={bookData.publisher} id="epublisher"
                               onChange={handlePublisherChange} className={errors.publisher ? 'error' : ''} aria-invalid={errors.publisher} aria-describedby="publisher-error"/>
                        {errors.publisher && <p id="publisher-error" className="error-message">Wydawnictwo jest wymagane i nie może przekraczać 255 znaków.</p>}
                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                            <div className="date-picker-container">
                                <DatePicker
                                    label="Wybierz datę"
                                    value={bookData.date}
                                    onChange={(newValue) => setBookData((prev) => ({ ...prev, date: newValue }))}
                                    renderInput={(params) => <input {...params} />}
                                />
                            </div>
                        </LocalizationProvider>
                        <input name="language" type="text" placeholder="Język" value={bookData.language} id="elanguage"
                               onChange={handleLanguageChange} className={errors.language ? 'error' : ''} aria-invalid={errors.language} aria-describedby="language-error"/>
                        {errors.language && <p id="language-error" className="error-message">Język jest wymagany, nie może zawierać cyfr i nie może przekraczać 255 znaków.</p>}
                        <textarea
                            name="description"
                            placeholder="Opis"
                            id="edescription"
                            value={bookData.description}
                            onChange={handleDescriptionChange}
                            className={errors.description ? 'error' : ''}
                            maxLength="3000"
                            aria-invalid={errors.description}
                            aria-describedby="description-error"
                        ></textarea>
                        <p className="char-counter">{bookData.description.length}/3000 znaków</p>
                        {errors.description && <p id="description-error" className="error-message">Opis jest wymagany, może mieć maksymalnie 3000 znaków.</p>}
                        <InputFileUpload  onChange={handleFileChange} className={errors.file ? 'error' : ''} aria-invalid={errors.file} aria-describedby="file-error"/>
                        {errors.file && <p id="file-error" className="error-message">Zdjęcie musi być w formacie JPG lub PNG.</p>}
                        <h3>Edytuj dostępności</h3>
                        <AccessTypeComponent
                            platforms={platforms}
                            formats={formats}
                            accessTypes={accessTypes}
                            availability={availability}
                            setAvailability={setAvailability}
                        />
                        <button type="submit" id="add-button">ZAKTUALIZUJ KSIĄŻKĘ</button>
                    </form>
                </div>
            </main>
            <Footer/>
        </div>
    );
};

export default EditBook;