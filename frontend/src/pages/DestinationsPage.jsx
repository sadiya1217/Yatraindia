function DestinationsPage() {
    return (
        <main>
            <section className="destination-header">
                <p className="section-label">
                    DISCOVER INDIA
                </p>

                <h1>Explore destinations</h1>

                <p>
                    Discover places to visit, things to do and
                    experiences across India.
                </p>
            </section>

            <section className="destination-section">
                <div className="destination-grid">
                    <div className="destination-card">
                        <h2>Hyderabad</h2>
                        <p>
                            Explore heritage, food, culture and
                            modern city experiences.
                        </p>
                        <button type="button">
                            Explore Hyderabad
                        </button>
                    </div>

                    <div className="destination-card">
                        <h2>Jaipur</h2>
                        <p>
                            Discover forts, palaces, markets and
                            Rajasthan's rich heritage.
                        </p>
                        <button type="button">
                            Explore Jaipur
                        </button>
                    </div>

                    <div className="destination-card">
                        <h2>Goa</h2>
                        <p>
                            Find beaches, local experiences, food
                            and places to relax.
                        </p>
                        <button type="button">
                            Explore Goa
                        </button>
                    </div>
                </div>
            </section>
        </main>
    );
}

export default DestinationsPage;