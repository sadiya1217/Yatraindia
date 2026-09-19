import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import apiClient from "../services/apiClient";
import DestinationMap from "../components/DestinationMap";
function DestinationDetailsPage() {
    const { id } = useParams();

    const [destination, setDestination] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        const loadDestination = async () => {
            try {
                const data = await apiClient(`/api/destinations/${id}`);
                setDestination(data);
            } catch (err) {
                setError("Unable to load destination details.");
            } finally {
                setLoading(false);
            }
        };

        loadDestination();
    }, [id]);

    if (loading) {
        return <main><p>Loading destination...</p></main>;
    }

    if (error) {
        return <main><p>{error}</p></main>;
    }

    return (
        <main>
            <section className="destination-details">
                <p className="section-label">DESTINATION</p>

                <h1>{destination.name}</h1>

                <p>
                    {destination.shortDescription}
                </p>

                <div className="destination-info">
                    <p>
                        <strong>State:</strong> {destination.state}
                    </p>

                    <p>
                        <strong>Country:</strong> {destination.country}
                    </p>

                    <p>
                        <strong>Best time to visit:</strong>{" "}
                        {destination.bestTimeToVisit}
                    </p>
                </div>
<DestinationMap
    latitude={destination.latitude}
    longitude={destination.longitude}
    name={destination.name}
/>
            </section>
        </main>
    );
}

export default DestinationDetailsPage;