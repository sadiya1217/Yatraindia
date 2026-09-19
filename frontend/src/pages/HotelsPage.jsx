import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import apiClient from "../services/apiClient";

function HotelsPage() {
    const [hotels, setHotels] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [searchTerm, setSearchTerm] = useState("");
    const [cityFilter, setCityFilter] = useState("");
    const [stateFilter, setStateFilter] = useState("");

    useEffect(() => {
        const loadHotels = async () => {
            try {
                const data = await apiClient("/api/hotels");
                setHotels(data);
            } catch (err) {
                setError("Unable to load hotels.");
            } finally {
                setLoading(false);
            }
        };

        loadHotels();
    }, []);

    const filteredHotels = hotels.filter((hotel) => {
        const matchesName = hotel.name
            .toLowerCase()
            .includes(searchTerm.toLowerCase());

        const matchesCity = hotel.city
            .toLowerCase()
            .includes(cityFilter.toLowerCase());

        const matchesState = hotel.state
            .toLowerCase()
            .includes(stateFilter.toLowerCase());

        return matchesName && matchesCity && matchesState;
    });

    if (loading) {
        return <div className="page-container">Loading hotels...</div>;
    }

    if (error) {
        return <div className="page-container">{error}</div>;
    }

    return (
        <div className="page-container">
            <h1>Hotels</h1>

            <p>
                Discover hotels for your journey across India.
            </p>

            <div className="hotel-search">
                <input
                    type="text"
                    placeholder="Search hotels by name..."
                    value={searchTerm}
                    onChange={(event) => setSearchTerm(event.target.value)}
                />
            </div>

            <div className="hotel-search">
                <input
                    type="text"
                    placeholder="Filter by city..."
                    value={cityFilter}
                    onChange={(event) => setCityFilter(event.target.value)}
                />
            </div>

            <div className="hotel-search">
                <input
                    type="text"
                    placeholder="Filter by state..."
                    value={stateFilter}
                    onChange={(event) => setStateFilter(event.target.value)}
                />
            </div>

            <div className="hotel-grid">
                {filteredHotels.length === 0 ? (
                    <p>No hotels found.</p>
                ) : (
                    filteredHotels.map((hotel) => (
                        <div className="hotel-card" key={hotel.id}>
                            <h2>{hotel.name}</h2>

                            <p>
                                <strong>City:</strong> {hotel.city}
                            </p>

                            <p>
                                <strong>State:</strong> {hotel.state}
                            </p>

                            <p>{hotel.description}</p>

                            <p>
    <strong>Rating:</strong> {hotel.starRating} / 5
</p>


                            <Link to={`/hotels/${hotel.id}`}>
                                View Hotel
                            </Link>
                        </div>
                    ))
                )}
            </div>
        </div>
    );
}

export default HotelsPage