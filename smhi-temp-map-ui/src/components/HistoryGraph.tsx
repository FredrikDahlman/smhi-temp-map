import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import type { TemperatureHistory } from '../types';

interface HistoryGraphProps {
  history: TemperatureHistory | null;
  loading: boolean;
}

export default function HistoryGraph({ history, loading }: HistoryGraphProps) {
  if (loading) {
    return (
      <div style={{ 
        display: 'flex', 
        alignItems: 'center', 
        justifyContent: 'center', 
        height: '200px',
        backgroundColor: '#f5f5f5',
        borderRadius: '8px'
      }}>
        Loading...
      </div>
    );
  }

  if (!history) {
    return null;
  }

  const data = history.readings
    .slice()
    .reverse()
    .map((reading) => ({
      time: new Date(reading.timestamp).toLocaleTimeString('sv-SE', { 
        hour: '2-digit', 
        minute: '2-digit' 
      }),
      temperature: reading.temperature,
    }));

  return (
    <div style={{ backgroundColor: 'white', padding: '16px', borderRadius: '8px' }}>
      <h3>{history.stationName}</h3>
      <ResponsiveContainer width="100%" height={200}>
        <LineChart data={data}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis 
            dataKey="time" 
            tick={{ fontSize: 10 }}
            interval="preserveStartEnd"
          />
          <YAxis 
            unit="°C" 
            tick={{ fontSize: 10 }}
          />
          <Tooltip 
            formatter={(value) => [`${Number(value).toFixed(1)}°C`, 'Temperature']}
            labelStyle={{ color: '#333' }}
          />
          <Line 
            type="monotone" 
            dataKey="temperature" 
            stroke="#2563eb" 
            strokeWidth={2}
            dot={false}
          />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
}
