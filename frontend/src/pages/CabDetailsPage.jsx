import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import apiClient from "../services/apiClient";
import "./CabDetailsPage.css";

function CabDetailsPage() {
    const { id } = useParams();
    const navigate = useNavigate();

    const [cab, setCab] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        const loadCab = async () => {
            try {
                const data = await apiClient(`/api/cabs/${id}`);
                setCab(data);
            } catch (err) {
                setError("Unable to load cab details.");
            } finally {
                setLoading(false);
            }
        };

        loadCab();
    }, [id]);

    if (loading) {
        return <p>Loading cab details...</p>;
    }

    if (error) {
        return <p>{error}</p>;
    }

    if (!cab) {
        return <p>Cab not found.</p>;
    }

    return (
        <div className="cab-details-page">
            <div className="cab-details-card">
                <h1>{cab.name}</h1>

                <p className="cab-description">
                    {cab.description}
                </p>

                <p>
                    <strong>Cab Type:</strong> {cab.cabType}
                </p>

                <p>
                    <strong>Location:</strong>{" "}
                    {cab.city}, {cab.state}, {cab.country}
                </p>

                <p>
                    <strong>Seating Capacity:</strong>{" "}
                    {cab.seatingCapacity}
                </p>

                <p>
                    <strong>Base Fare:</strong> ₹{cab.baseFare}
                </p>

                <p>
                    <strong>Per KM Rate:</strong> ₹{cab.perKmRate}
                </p>

                <p>
                    <strong>Per Hour Rate:</strong> ₹{cab.perHourRate}
                </p>

                <p>
                    <strong>Contact Phone:</strong>{" "}
                    {cab.contactPhone}
                </p>

                <p>
                    <strong>Contact Email:</strong>{" "}
                    {cab.contactEmail}
                </p>

                <p>
                    <strong>Status:</strong> {cab.status}
                </p>

                <button
                    className="book-now-button"
                    onClick={() => navigate(`/cab-booking/${cab.id}`)}
                    disabled={cab.status !== "ACTIVE"}
                >
                    {cab.status === "ACTIVE"
                        ? "Book Now"
                        : "Currently Unavailable"}
                </button>
            </div>
        </div>
    );
}

export default CabDetailsPage;