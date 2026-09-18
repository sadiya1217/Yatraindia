import "./App.css";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import HomePage from "./pages/HomePage";
import DestinationsPage from "./pages/DestinationsPage";
import DestinationDetailsPage from "./pages/DestinationDetailsPage";

function App() {
    return (
        <BrowserRouter>
            <div className="app">
                <header className="navbar">
                    <div className="logo">YatraIndia</div>

                    <nav className="nav-links">
                        <a href="/">Discover</a>
                        <a href="#plan">Plan</a>
                        <a href="#book">Book</a>
                        <a href="#assist">Travel Assist</a>
                    </nav>

                    <button className="login-btn" type="button">
                        Login
                    </button>
                </header>

                <Routes>
                    <Route path="/" element={<HomePage />} />
                    <Route
                        path="/destinations"
                        element={<DestinationsPage />}
                    />
                    <Route
                        path="/destinations/:id"
                        element={<DestinationDetailsPage />}
                    />
                </Routes>

                <footer>
                    <p>
                        YatraIndia - Explore India, your way.
                    </p>
                </footer>
            </div>
        </BrowserRouter>
    );
}

export default App;