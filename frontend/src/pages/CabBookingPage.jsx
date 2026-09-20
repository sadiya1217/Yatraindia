import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import apiClient from "../services/apiClient";
import "./CabBookingPage.css";

function CabBookingPage() {
    const { id } = useParams();
    const navigate = useNavigate();

    const [cab, setCab] = useState(null);
    const [loading, setLoading] = useState(true);
    const [message, setMessage] = useState("");
    const [error, setError] = useState("");

    const [formData, setFormData] = useState({
        pickupLocation: "",
        dropLocation: "",
        travelDate: "",
        pickupTime: "",
        passengers: 1,
    });

    useEffect(() => {
        const loadCab = async () => {
            try {
                const data = await apiClient(`/api/cabs/${id}`);
                setCab(data);
            } catch (err) {
                setError("Unable to load cab.");
            } finally {
                setLoading(false);
            }
        };

        loadCab();
    }, [id]);

    const handleChange = (event) => {
        const { name, value } = event.target;

        setFormData((previous) => ({
            ...previous,
            [name]: value,
        }));
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        setMessage("");
        setError("");

        try {
            const bookingReference = `CAB-${Date.now()}`;

            const checkIn =
                `${formData.travelDate}T${formData.pickupTime}:00`;

            const bookingData = {
                bookingReference,
                userId: 1,
                bookingType: "CAB",
                serviceId: Number(id),
                checkIn,
                checkOut: null,
                totalAmount: Number(cab.baseFare),
                status: "CONFIRMED",
                pickupLocation: formData.pickupLocation,
                dropLocation: formData.dropLocation,
                passengers: Number(formData.passengers),
            };

            await apiClient("/api/bookings", {
                method: "POST",
                body: JSON.stringify(bookingData),
            });

            setMessage(
                `Cab booked successfully. Booking reference: ${bookingReference}`
            );
        } catch (err) {
            setError("Unable to create cab booking.");
        }
    };

    if (loading) {
        return <p>Loading booking page...</p>;
    }

    if (error && !cab) {
        return <p>{error}</p>;
    }

    if (!cab) {
        return <p>Cab not found.</p>;
    }

    return (
        <div className="cab-booking-page">
            <div className="cab-booking-card">
                <h1>Book {cab.name}</h1>

                <p>
                    <strong>Cab Type:</strong> {cab.cabType}
                </p>

                <p>
                    <strong>Base Fare:</strong> ₹{cab.baseFare}
                </p>

                <form onSubmit={handleSubmit}>
                    <div className="booking-field">
                        <label>Pickup Location</label>
                        <input
                            type="text"
                            name="pickupLocation"
                            placeholder="Enter pickup location"
                            value={formData.pickupLocation}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className="booking-field">
                        <label>Drop Location</label>
                        <input
                            type="text"
                            name="dropLocation"
                            placeholder="Enter drop location"
                            value={formData.dropLocation}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className="booking-field">
                        <label>Travel Date</label>
                        <input
                            type="date"
                            name="travelDate"
                            value={formData.travelDate}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className="booking-field">
                        <label>Pickup Time</label>
                        <input
                            type="time"
                            name="pickupTime"
                            value={formData.pickupTime}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className="booking-field">
                        <label>Passengers</label>
                        <input
                            type="number"
                            name="passengers"
                            min="1"
                            max={cab.seatingCapacity}
                            value={formData.passengers}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <button
                        className="confirm-booking-button"
                        type="submit"
                    >
                        Confirm Booking
                    </button>
                </form>

                {message && (
                    <div className="booking-success">
                        {message}
                    </div>
                )}

                {error && (
                    <div className="booking-error">
                        {error}
                    </div>
                )}

                <button
                    className="back-button"
                    type="button"
                    onClick={() => navigate(`/cabs/${cab.id}`)}
                >
                    Back to Cab Details
                </button>
            </div>
        </div>
    );
}

export default CabBookingPage;