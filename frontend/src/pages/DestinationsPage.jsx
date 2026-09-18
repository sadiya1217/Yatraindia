import { useEffect, useState } from "react";
import apiClient from "../services/apiClient";

function DestinationsPage() {
    const [destinations, setDestinations] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        const loadDestinations = async () => {
            try {
                const data = await apiClient("/api/destinations");
                setDestinations(data);
            } catch (err) {
                setError("Unable to load destinations.");
            } finally {
                setLoading(false);
            }
        };

        loadDestinations();
    }, []);

    return (
        <main>
            <section className="destination-header">
                <p className="section-label">DISCOVER INDIA</p>

                <h1>Explore destinations</h1>

                <p>
                    Discover places to visit, things to do and
                    experiences across India.
                </p>
            </section>

            <section className="destination-section">
                {loading && <p>Loading destinations...</p>}

                {error && <p>{error}</p>}

                {!loading && !error && (
                    <div className="destination-grid">
                        {destinations.map((destination) => (
                            <div
                                className="destination-card"
                                key={destination.id}
                            >
                                <h2>{destination.name}</h2>

                                <p>
                                    {destination.shortDescription}
                                </p>

                                <button type="button">
                                    Explore {destination.name}
                                </button>
                            </div>
                        ))}
                    </div>
                )}
            </section>
        </main>
    );
}

export default DestinationsPage;