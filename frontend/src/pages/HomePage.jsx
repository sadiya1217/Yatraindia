function HomePage() {
    return (
        <main>
            {/* Hero */}
            <section className="hero-section">
                <div className="hero-content">
                    <p className="tagline">
                        EXPLORE INDIA YOUR WAY
                    </p>

                    <h1>
                        Discover India.
                        <br />
                        <span>Travel your way.</span>
                    </h1>

                    <p className="hero-text">
                        Discover beautiful destinations, plan memorable
                        trips, find trusted stays and cabs, and get
                        local travel assistance — all in one place.
                    </p>

                    <div className="search-box">
                        <input
                            type="text"
                            placeholder="Search destinations, cities or places..."
                        />

                        <button type="button">
                            Explore
                        </button>
                    </div>

                    <div className="quick-links">
                        <span>Popular:</span>
                        <button type="button">Goa</button>
                        <button type="button">Hyderabad</button>
                        <button type="button">Jaipur</button>
                        <button type="button">Kerala</button>
                    </div>
                </div>
            </section>

            {/* Discover */}
            <section className="section">
                <p className="section-label">
                    DISCOVER INDIA
                </p>

                <h2>
                    Start your journey with YatraIndia
                </h2>

                <p className="section-description">
                    Everything you need to discover, plan, book and
                    travel across India.
                </p>

                <div className="cards">
                    <div className="card">
                        <span className="card-number">01</span>
                        <h3>Discover</h3>
                        <p>
                            Explore destinations, attractions, culture,
                            food and experiences across India.
                        </p>
                    </div>

                    <div className="card">
                        <span className="card-number">02</span>
                        <h3>Plan</h3>
                        <p>
                            Create your travel plans and organize your
                            journey with useful itineraries.
                        </p>
                    </div>

                    <div className="card">
                        <span className="card-number">03</span>
                        <h3>Book</h3>
                        <p>
                            Find hotels and cabs and manage your travel
                            bookings from one place.
                        </p>
                    </div>

                    <div className="card">
                        <span className="card-number">04</span>
                        <h3>Travel Assist</h3>
                        <p>
                            Get multilingual, local and safety assistance
                            while travelling.
                        </p>
                    </div>
                </div>
            </section>

            {/* Travel Assist Preview */}
            <section className="assist-section">
                <div className="assist-content">
                    <p className="section-label">
                        TRAVEL ASSIST
                    </p>

                    <h2>
                        Travel with local help when you need it.
                    </h2>

                    <p>
                        YatraIndia will bring language assistance,
                        nearby services, emergency support and local
                        travel information together for your journey.
                    </p>

                    <button type="button" className="assist-button">
                        Explore Travel Assist
                    </button>
                </div>
            </section>
        </main>
    );
}

export default HomePage;