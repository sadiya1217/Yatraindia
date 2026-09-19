import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import apiClient from "../services/apiClient";

function HotelDetailsPage() {
    const { id } = useParams();

    const [hotel, setHotel] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        const loadHotel = async () => {
            try {
                const data = await apiClient(`/api/hotels/${id}`);
                setHotel(data);
            } catch (err) {
                setError("Unable to load hotel details.");
            } finally {
                setLoading(false);
            }
        };

        loadHotel();
    }, [id]);

    if (loading) {
        return <div className="page-container">Loading hotel...</div>;
    }

    if (error) {
        return <div className="page-container">{error}</div>;
    }

    if (!hotel) {
        return <div className="page-container">Hotel not found.</div>;
    }

    return (
        <div className="page-container">
            <Link to="/hotels">← Back to Hotels</Link>

            <h1>{hotel.name}</h1>

            <p>{hotel.description}</p>

            <p>
                <strong>Address:</strong> {hotel.address}
            </p>

            <p>
                <strong>City:</strong> {hotel.city}
            </p>

            <p>
                <strong>State:</strong> {hotel.state}
            </p>

            <p>
                <strong>Country:</strong> {hotel.country}
            </p>

            <p>
                <strong>Star Rating:</strong> {hotel.starRating}
            </p>

            <p>
                <strong>Check-in:</strong> {hotel.checkInTime}
            </p>

            <p>
                <strong>Check-out:</strong> {hotel.checkOutTime}
            </p>

            <p>
                <strong>Contact:</strong> {hotel.contactPhone}
            </p>

            <p>
                <strong>Email:</strong> {hotel.contactEmail}
            </p>

            <p>
                <strong>Status:</strong> {hotel.status}
            </p>
        </div>
    );
}

export default HotelDetailsPage;