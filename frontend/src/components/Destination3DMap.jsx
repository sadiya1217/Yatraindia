import { useEffect, useRef } from "react";
import * as maplibregl from "maplibre-gl";
import "maplibre-gl/dist/maplibre-gl.css";

function Destination3DMap({ latitude, longitude, name }) {
    const mapContainer = useRef(null);
    const map = useRef(null);

    useEffect(() => {
        if (map.current) {
            return;
        }

        map.current = new maplibregl.Map({
            container: mapContainer.current,
            style: "https://tiles.openfreemap.org/styles/liberty",
            center: [longitude, latitude],
            zoom: 14,
            pitch: 60,
            bearing: -20,
            maxPitch: 85
        });

        map.current.addControl(
            new maplibregl.NavigationControl(),
            "top-right"
        );

        map.current.on("load", () => {
            const layers = map.current.getStyle().layers;

            layers.forEach((layer) => {
                if (
                    layer.type === "fill-extrusion" &&
                    layer.source === "openmaptiles"
                ) {
                    map.current.setPaintProperty(
                        layer.id,
                        "fill-extrusion-height",
                        [
                            "interpolate",
                            ["linear"],
                            ["zoom"],
                            14,
                            0,
                            16,
                            ["get", "render_height"]
                        ]
                    );
                }
            });

            new maplibregl.Marker()
                .setLngLat([longitude, latitude])
                .setPopup(
                    new maplibregl.Popup().setText(name)
                )
                .addTo(map.current);
        });

        return () => {
            map.current.remove();
            map.current = null;
        };
    }, [latitude, longitude, name]);

    return (
        <div
            ref={mapContainer}
            style={{
                width: "100%",
                height: "500px",
                borderRadius: "12px",
                overflow: "hidden"
            }}
        />
    );
}

export default Destination3DMap;