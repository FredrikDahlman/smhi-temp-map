const TWO_HOURS_MS = 2 * 60 * 60 * 1000;

export function isStaleReading(timestamp: string | undefined, nowMs: number = Date.now()): boolean {
  if (!timestamp) return false;
  const parsed = Date.parse(timestamp);
  if (Number.isNaN(parsed)) return false;
  return nowMs - parsed > TWO_HOURS_MS;
}

export function getMarkerColorClass(
  temperature: number | undefined,
  timestamp: string | undefined,
  nowMs: number = Date.now()
): 'marker-yellow' | 'marker-blue' | 'marker-red' | 'marker-grey' {
  if (temperature === undefined) {
    return 'marker-grey';
  }

  if (isStaleReading(timestamp, nowMs)) {
    return 'marker-yellow';
  }

  return temperature > 0 ? 'marker-blue' : 'marker-red';
}
