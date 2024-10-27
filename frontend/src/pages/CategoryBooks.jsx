import React, {useEffect, useState} from 'react';
import Header from '../components/Header/Header.jsx';
import Footer from '../components/Footer/Footer.jsx';
import '../pages_css/categoryBooks.css';
import axios from "../../axiosConfig.js";
import {Link, useParams} from "react-router-dom";
const CategoryBooks = () => {
    const [books, setBooks] = useState([]);
    const { categoryName }= useParams();
    useEffect(() => {
        const fetchBooksByCategory = async () => {
            try {
                const response = await axios.get(`/api/categories/${categoryName}/books`);
                setBooks(response.data.books);
            } catch (error) {
                console.error("Error fetching books by category:", error);
            }
        };
        fetchBooksByCategory();
    }, [categoryName]);
    return (
        <div className="search-container">
            <Header activePage="search" />
            <main className='main-search'>
                <div className="search">{categoryName}</div>
                <div className="category-books">
                    {books.map((book) => (
                        <Link to={`/book/${book.title}`} key={book.id} className="book-link">
                            <div className="book-item">
                                <img src={`data:image/jpeg;base64, ${book.img}`} alt="Book cover"
                                     className="book-cover"/>
                                <h3>{book.title}</h3>
                                <p>by {book.authors.map(author => (
                                    <span key={author.id}>{author.firstName} {author.lastName}</span>
                                )).reduce((prev, curr) => [prev, ', ', curr])}</p>
                            </div>
                        </Link>
                    ))}
                </div>
            </main>
            <Footer/>
        </div>
    );
}

export default CategoryBooks;