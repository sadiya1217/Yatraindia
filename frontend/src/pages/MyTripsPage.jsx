import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import API_BASE_URL from "../services/api";
import { useAuth } from "../context/AuthContext";

function MyTripsPage() {
    const { token, isAuthenticated } = useAuth();

    const [bookings, setBookings] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        const fetchMyBookings = async () => {
            if (!isAuthenticated || !token) {
                setLoading(false);
                setError("Please log in to view your trips.");
                return;
            }

            try {
                const response = await fetch(
                    `${API_BASE_URL}/api/bookings/me`,
                    {
                        method: "GET",
                        headers: {
                            Authorization: `Bearer ${token}`,
                        },
                    }
                );

                const data = await response.json();

                if (!response.ok) {
                    throw new Error(
                        data.message ||
                            "Failed to load your bookings."
                    );
                }

                setBookings(data);
            } catch (err) {
                setError(
                    err.message ||
                        "Unable to load your bookings."
                );
            } finally {
                setLoading(false);
            }
        };

        fetchMyBookings();
    }, [token, isAuthenticated]);

    if (loading) {
        return (
            <main className="page-container">
                <h1>My Trips</h1>
                <p>Loading your bookings...</p>
            </main>
        );
    }

    if (error) {
        return (
            <main className="page-container">
                <h1>My Trips</h1>
                <p>{error}</p>
            </main>
        );
    }

    return (
        <main className="page-container">
            <h1>My Trips</h1>

            {bookings.length === 0 ? (
                <div>
                    <p>
                        You don't have any bookings yet.
                    </p>

                    <Link to="/hotels">
                        Explore Hotels
                    </Link>
                </div>
            ) : (
                <div>
                    {bookings.map((booking) => (
                        <div
                            key={booking.id}
                            className="booking-card"
                        >
                            <h2>
                                {booking.bookingType} Booking
                            </h2>

                            <p>
                                <strong>
                                    Booking Reference:
                                </strong>{" "}
                                {booking.bookingReference}
                            </p>

                            <p>
                                <strong>Status:</strong>{" "}
                                {booking.status}
                            </p>

                            <p>
                                <strong>Service ID:</strong>{" "}
                                {booking.serviceId}
                            </p>

                            <p>
                                <strong>Total Amount:</strong>{" "}
                                ₹{booking.totalAmount}
                            </p>

                            {booking.bookingType === "CAB" && (
                                <>
                                    <p>
                                        <strong>Pickup:</strong>{" "}
                                        {booking.pickupLocation}
                                    </p>

                                    <p>
                                        <strong>Drop:</strong>{" "}
                                        {booking.dropLocation}
                                    </p>

                                    <p>
                                        <strong>
                                            Passengers:
                                        </strong>{" "}
                                        {booking.passengers}
                                    </p>
                                </>
                            )}
                        </div>
                    ))}
                </div>
            )}
        </main>
    );
}

export default MyTripsPage;