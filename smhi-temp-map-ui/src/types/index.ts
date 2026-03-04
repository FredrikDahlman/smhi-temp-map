export interface Station {
  id: number;
  stationId: string;
  name: string;
  latitude: number;
  longitude: number;
  temperature?: number;
  timestamp?: string;
}

export interface TemperaturePoint {
  temperature: number;
  timestamp: string;
}

export interface TemperatureHistory {
  stationId: number;
  stationName: string;
  readings: TemperaturePoint[];
}
