import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";
import "leaflet/dist/leaflet.css";

function DestinationMap({ latitude, longitude, name }) {
    return (
        <MapContainer
            center={[latitude, longitude]}
            zoom={12}
            scrollWheelZoom={false}
            style={{ height: "400px", width: "100%" }}
        >
            <TileLayer
                attribution='&copy; OpenStreetMap contributors'
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            />

            <Marker position={[latitude, longitude]}>
                <Popup>
                    {name}
                </Popup>
            </Marker>
        </MapContainer>
    );
}

export default DestinationMap;