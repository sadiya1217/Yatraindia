import "./App.css";
import { BrowserRouter, Routes, Route, Link } from "react-router-dom";
import HomePage from "./pages/HomePage";
import HotelDetailsPage from "./pages/HotelDetailsPage";
import DestinationsPage from "./pages/DestinationsPage";
import DestinationDetailsPage from "./pages/DestinationDetailsPage";
import CabBookingPage from "./pages/CabBookingPage";
import HotelsPage from "./pages/HotelsPage";
import CabsPage from "./pages/CabsPage";
import CabDetailsPage from "./pages/CabDetailsPage";
import MyTripsPage from "./pages/MyTripsPage";
import LoginPage from "./pages/LoginPage";

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
                        <Link to="/my-trips">My Trips</Link>
                    </nav>

                    <Link to="/login" className="login-btn">
                        Login
                    </Link>
                </header>

                <Routes>
                    <Route path="/" element={<HomePage />} />
                    <Route path="/hotels" element={<HotelsPage />} />
                    <Route path="/hotels/:id" element={<HotelDetailsPage />} />
                    <Route path="/cabs" element={<CabsPage />} />
                    <Route path="/my-trips" element={<MyTripsPage />} />
                    <Route path="/cabs/:id" element={<CabDetailsPage />} />
                    <Route path="/login" element={<LoginPage />} />
                    <Route
                        path="/cab-booking/:id"
                        element={<CabBookingPage />}
                    />
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
