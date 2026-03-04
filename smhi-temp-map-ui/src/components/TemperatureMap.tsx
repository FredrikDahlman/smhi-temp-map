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
    return new L.DivIcon({
      className: 'temperature-marker',
      html: '<div class="marker-label marker-grey">-</div>',
      iconSize: [40, 40],
      iconAnchor: [20, 20],
    });
  }
  
  const colorClass = temperature > 0 ? 'marker-blue' : 'marker-red';
  const displayTemp = temperature > 0 ? `+${temperature.toFixed(0)}` : temperature.toFixed(0);
  
  return new L.DivIcon({
    className: 'temperature-marker',
    html: `<div class="marker-label ${colorClass}">${displayTemp}°</div>`,
    iconSize: [44, 44],
    iconAnchor: [22, 22],
  });
}

export default function TemperatureMap({ stations, onStationClick }: TemperatureMapProps) {
  return (
    <>
      <style>{`
        .temperature-marker {
          background: transparent;
          border: none;
        }
        .marker-label {
          display: flex;
          align-items: center;
          justify-content: center;
          width: 40px;
          height: 40px;
          border-radius: 50%;
          font-weight: bold;
          font-size: 14px;
          color: white;
          box-shadow: 0 2px 6px rgba(0,0,0,0.3);
          border: 2px solid white;
        }
        .marker-blue {
          background: #2563eb;
        }
        .marker-red {
          background: #dc2626;
        }
        .marker-grey {
          background: #6b7280;
        }
      `}</style>
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
    </>
  );
}
