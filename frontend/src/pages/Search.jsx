import React, { useState, useEffect } from 'react';
import '../pages_css/Search.css';
import Footer from '../components/Footer/Footer.jsx';
import Header from '../components/Header/Header.jsx';
import Searching from '../components/Searching/Searching.jsx';
import { Link } from 'react-router-dom';
import axios from '../../axiosConfig.js';
const Search = () => {
    const [searchResults, setSearchResults] = useState([]);
    const [categories, setCategories] = useState([]);
    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const response = await axios.get('/api/categories');
                setCategories(response.data);
            } catch (error) {
                console.error("Error fetching categories:", error);
            }
        };
        fetchCategories();
    }, []);
    const handleSearchResults = (results) => {
        setSearchResults(results);
    };
  return (
    <div className="search-container">
       <Header activePage="search" />
        <main className='main-search'>
            <div className="search">Search</div>
            <Searching onSearchResults={handleSearchResults}/>
            {searchResults.length === 0 && (
                <div className="categories-list">
                    <h3>Browse by Category</h3>
                    <div className="all-categories">
                        {categories.map(category => (
                            <Link to={`/category/${category.name}`} key={category.name} className="category-item">
                                    {category.name}
                                </Link>
                        ))}
                    </div>
                </div>
            )}
            {searchResults.map((book) => (
                <div className="search-item-container" key={book.id}>
                    <Link to={`/book/${book.title}`} key={book.id} className="search-link">
                        <div key={book.id} className="search-items">
                            <div className="author-books">
                            <h2>{book.title}</h2>
                                <p>
                                    by {book.authors && book.authors.map(author => (
                                    <span key={author.id}>{author.firstName} {author.lastName}</span>
                                )).reduce((prev, curr) => [prev, ', ', curr])} </p>
                            </div>
                            <div className="cover-search">
                                <img src={`data:image/jpeg;base64, ${book.img}`} alt="Book cover"
                                     className="book-cover"/>
                            </div>
                        </div>
                    </Link>
                    </div>
                ))}
        </main>
        <Footer/>
    </div>
  );
}

export default Search;