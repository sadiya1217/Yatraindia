import "./App.css";
import HomePage from "./pages/HomePage";
import DestinationsPage from "./pages/DestinationsPage";
function App() {
    return (
        <div className="app">
            <header className="navbar">
                <div className="logo">YatraIndia</div>

                <nav className="nav-links">
                    <a href="#discover">Discover</a>
                    <a href="#plan">Plan</a>
                    <a href="#book">Book</a>
                    <a href="#assist">Travel Assist</a>
                </nav>

                <button className="login-btn" type="button">
                    Login
                </button>
            </header>
                <DestinationsPage />

            <footer>
                <p>
                    YatraIndia - Explore India, your way.
                </p>
            </footer>
        </div>
    );
}

export default App;