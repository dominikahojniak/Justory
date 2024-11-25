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
            //navigate(`/book/${bookData.title}`);
        } catch (error) {
            console.error('Error updating book:', error);
            alert('Błąd podczas aktualizacji książki.');
        }
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
                               onChange={handleInputChange}/>
                        <input name="authors" type="text" placeholder="Autor (oddziel przecinkiem)" id="eauthor"
                               value={bookData.authors} onChange={handleInputChange}/>
                        <input name="ISBN" type="text" placeholder="ISBN" id="eISBN" value={bookData.isbn}
                               onChange={handleInputChange}/>
                        <input name="categories" type="text" placeholder="Kategoria (oddziel przecinkiem)"
                               id="ecategories"
                               value={bookData.categories} onChange={handleInputChange}/>
                        <input name="publisher" type="text" placeholder="Wydawnictwo" value={bookData.publisher}
                               id="epublisher"
                               onChange={handleInputChange}/>
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
                               onChange={handleInputChange}/>
                        <input name="description" placeholder="Opis" value={bookData.description} id="edescription"
                               onChange={handleInputChange}/>
                        <InputFileUpload onChange={(e) => setBookData({...bookData, file: e.target.files[0]})}/>
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