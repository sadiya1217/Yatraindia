import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import apiClient from "../services/apiClient";
import "./CabsPage.css";


function CabsPage() {
    const [cabs, setCabs] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const [name, setName] = useState("");
    const [city, setCity] = useState("");
    const [state, setState] = useState("");
    const [cabType, setCabType] = useState("");

    useEffect(() => {
        const loadCabs = async () => {
            try {
                const data = await apiClient("/api/cabs");
                setCabs(data);
            } catch (err) {
                setError("Unable to load cabs.");
            } finally {
                setLoading(false);
            }
        };

        loadCabs();
    }, []);

    const filteredCabs = cabs.filter((cab) => {
        const matchesName =
            !name ||
            cab.name.toLowerCase().includes(name.toLowerCase());

        const matchesCity =
            !city ||
            cab.city.toLowerCase() === city.toLowerCase();

        const matchesState =
            !state ||
            cab.state.toLowerCase() === state.toLowerCase();

        const matchesCabType =
            !cabType ||
            cab.cabType.toLowerCase() === cabType.toLowerCase();

        return (
            matchesName &&
            matchesCity &&
            matchesState &&
            matchesCabType
        );
    });

    if (loading) {
        return <p>Loading cabs...</p>;
    }

    if (error) {
        return <p>{error}</p>;
    }

    return (
        <div className="cabs-page">
            <h1>Cab Discovery</h1>

            <p>
                Find cabs for local travel, airport transfers,
                sightseeing and other travel needs.
            </p>

            <div className="cab-filters">
                <input
                    type="text"
                    placeholder="Search by cab name"
                    value={name}
                    onChange={(event) => setName(event.target.value)}
                />

                <input
                    type="text"
                    placeholder="Filter by city"
                    value={city}
                    onChange={(event) => setCity(event.target.value)}
                />

                <input
                    type="text"
                    placeholder="Filter by state"
                    value={state}
                    onChange={(event) => setState(event.target.value)}
                />

                <select
                    value={cabType}
                    onChange={(event) => setCabType(event.target.value)}
                >
                    <option value="">All Cab Types</option>
                    <option value="SEDAN">Sedan</option>
                    <option value="SUV">SUV</option>
                    <option value="HATCHBACK">Hatchback</option>
                </select>
            </div>

            <div className="cab-results">
                {filteredCabs.length === 0 ? (
                    <p>No cabs found.</p>
                ) : (
                    filteredCabs.map((cab) => (
                        <div className="cab-card" key={cab.id}>
                            <h2>{cab.name}</h2>

                            <p>
                                <strong>Type:</strong>{" "}
                                {cab.cabType}
                            </p>

                            <p>
                                <strong>Location:</strong>{" "}
                                {cab.city}, {cab.state}
                            </p>

                            <p>
                                <strong>Seats:</strong>{" "}
                                {cab.seatingCapacity}
                            </p>

                            <p>
                                <strong>Base Fare:</strong>{" "}
                                ₹{cab.baseFare}
                            </p>

                            <p>
                                <strong>Per KM:</strong>{" "}
                                ₹{cab.perKmRate}
                            </p>

                            <p>
                                <strong>Per Hour:</strong>{" "}
                                ₹{cab.perHourRate}
                            </p>

                            <p>{cab.description}</p>
                            <Link
    to={`/cabs/${cab.id}`}
    className="cab-book-button"
>
    Book Now
</Link>
                        </div>
                    ))
                )}
            </div>
        </div>
    );
}

export default CabsPage;