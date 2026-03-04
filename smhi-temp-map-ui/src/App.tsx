import { useState, useEffect } from 'react';
import TemperatureMap from './components/TemperatureMap';
import HistoryGraph from './components/HistoryGraph';
import type { Station, TemperatureHistory } from './types';
import './App.css';

const API_BASE = 'http://localhost:8080/api';

function App() {
  const [stations, setStations] = useState<Station[]>([]);
  const [selectedStation, setSelectedStation] = useState<Station | null>(null);
  const [history, setHistory] = useState<TemperatureHistory | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetchStations();
  }, []);

  const fetchStations = async () => {
    try {
      setError(null);
      const response = await fetch(`${API_BASE}/temperatures/current`);
      if (!response.ok) {
        throw new Error('Failed to fetch temperatures');
      }
      const data = await response.json();
      setStations(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Unknown error');
      console.error('Error fetching stations:', err);
    }
  };

  const handleStationClick = async (station: Station) => {
    setSelectedStation(station);
    setLoading(true);
    
    try {
      const response = await fetch(`${API_BASE}/temperatures/${station.id}/history`);
      if (!response.ok) {
        throw new Error('Failed to fetch history');
      }
      const data = await response.json();
      setHistory(data);
    } catch (err) {
      console.error('Error fetching history:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="app-container">
      <header className="app-header">
        <h1>Sweden Temperature Map</h1>
        <button onClick={fetchStations} className="refresh-button">
          Refresh
        </button>
      </header>
      
      {error && (
        <div className="error-message">
          {error}
        </div>
      )}

      <main className="app-main">
        <div className="map-container">
          <TemperatureMap 
            stations={stations} 
            onStationClick={handleStationClick} 
          />
        </div>
        
        {selectedStation && (
          <div className="history-panel">
            <HistoryGraph history={history} loading={loading} />
          </div>
        )}
      </main>
    </div>
  );
}

export default App;
