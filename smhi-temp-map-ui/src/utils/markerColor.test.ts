import { describe, expect, it } from 'vitest';
import { getMarkerColorClass } from './markerColor';

const NOW = Date.parse('2026-03-07T12:00:00.000Z');

describe('getMarkerColorClass', () => {
  it('returns yellow for stale positive readings', () => {
    const staleTs = new Date(NOW - (2 * 60 * 60 * 1000 + 1)).toISOString();
    expect(getMarkerColorClass(5, staleTs, NOW)).toBe('marker-yellow');
  });

  it('returns yellow for stale negative readings', () => {
    const staleTs = new Date(NOW - (2 * 60 * 60 * 1000 + 1)).toISOString();
    expect(getMarkerColorClass(-3, staleTs, NOW)).toBe('marker-yellow');
  });

  it('returns blue for fresh positive readings', () => {
    const freshTs = new Date(NOW - (30 * 60 * 1000)).toISOString();
    expect(getMarkerColorClass(8, freshTs, NOW)).toBe('marker-blue');
  });

  it('returns red for fresh negative readings', () => {
    const freshTs = new Date(NOW - (30 * 60 * 1000)).toISOString();
    expect(getMarkerColorClass(-8, freshTs, NOW)).toBe('marker-red');
  });

  it('treats exactly 2 hours old reading as non-stale', () => {
    const boundaryTs = new Date(NOW - 2 * 60 * 60 * 1000).toISOString();
    expect(getMarkerColorClass(2, boundaryTs, NOW)).toBe('marker-blue');
  });
});
