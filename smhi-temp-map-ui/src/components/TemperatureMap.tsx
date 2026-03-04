import { MapContainer, TileLayer, Marker, Popup, useMap } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';
import type { Station } from '../types';
import { useEffect } from 'react';

interface TemperatureMapProps {
  stations: Station[];
  onStationClick: (station: Station) => void;
}

const swedenCenter: [number, number] = [60.0, 15.0];
const defaultZoom = 5;

function ChangeView({ center, zoom }: { center: [number, number]; zoom: number }) {
  const map = useMap();
  useEffect(() => {
    map.setView(center, zoom);
  }, [center, zoom, map]);
  return null;
}

function createTemperatureIcon(temperature: number | undefined) {
  if (temperature === undefined) {
    return new L.Icon({
      iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-grey.png',
      iconSize: [25, 41],
      iconAnchor: [12, 41],
      popupAnchor: [1, -34],
    });
  }
  
  const color = temperature > 0 ? 'blue' : 'red';
  return new L.Icon({
    iconUrl: `https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-${color}.png`,
    shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
    iconSize: [25, 41],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
    shadowSize: [41, 41]
  });
}

export default function TemperatureMap({ stations, onStationClick }: TemperatureMapProps) {
  return (
    <MapContainer 
      center={swedenCenter} 
      zoom={defaultZoom} 
      style={{ height: '100%', width: '100%' }}
    >
      <ChangeView center={swedenCenter} zoom={defaultZoom} />
      <TileLayer
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      {stations.map((station) => (
        <Marker
          key={station.id}
          position={[station.latitude, station.longitude]}
          icon={createTemperatureIcon(station.temperature)}
          eventHandlers={{
            click: () => onStationClick(station),
          }}
        >
          <Popup>
            <div style={{ minWidth: 150 }}>
              <strong>{station.name}</strong>
              <br />
              {station.temperature !== undefined ? (
                <>
                  <span style={{ fontSize: '1.2em', fontWeight: 'bold' }}>
                    {station.temperature.toFixed(1)}°C
                  </span>
                  <br />
                  <small>Click for history</small>
                </>
              ) : (
                <span>No data</span>
              )}
            </div>
          </Popup>
        </Marker>
      ))}
    </MapContainer>
  );
}
