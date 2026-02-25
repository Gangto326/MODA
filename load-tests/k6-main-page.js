import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate, Trend } from 'k6/metrics';

const errorRate = new Rate('errors');
const mainPageDuration = new Trend('mainpage_duration');

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const USER_ID = __ENV.USER_ID || '38d8eb3c-38ce-43a7-be5b-e30d89484cb2';

export const options = {
  scenarios: {
    // Scenario 1: 워밍업 단건 3회
    warmup: {
      executor: 'shared-iterations',
      vus: 1,
      iterations: 3,
      exec: 'singleRequest',
      tags: { scenario: 'warmup' },
    },
    // Scenario 2: 점진적 부하 증가 (1→10 VU, 2분)
    ramp_up: {
      executor: 'ramping-vus',
      startVUs: 1,
      stages: [
        { duration: '30s', target: 3 },
        { duration: '30s', target: 5 },
        { duration: '30s', target: 10 },
        { duration: '30s', target: 1 },
      ],
      exec: 'concurrentRequest',
      startTime: '15s',
      tags: { scenario: 'ramp_up' },
    },
    // Scenario 3: 10 VU 버스트
    burst: {
      executor: 'shared-iterations',
      vus: 10,
      iterations: 30,
      exec: 'burstRequest',
      startTime: '2m30s',
      tags: { scenario: 'burst' },
    },
  },
  thresholds: {
    errors: ['rate<0.3'],
    mainpage_duration: ['p(95)<5000'],
  },
};

function fetchMainPage() {
  const params = {
    headers: { 'Content-Type': 'application/json' },
    timeout: '30s',
  };
  const start = Date.now();
  const res = http.get(`${BASE_URL}/api/search/main?userId=${USER_ID}`, params);
  const duration = Date.now() - start;
  mainPageDuration.add(duration);

  const success = check(res, {
    'status is 200': (r) => r.status === 200,
  });
  errorRate.add(!success);
  return res;
}

export function singleRequest() {
  fetchMainPage();
  sleep(2);
}

export function concurrentRequest() {
  fetchMainPage();
  sleep(0.5);
}

export function burstRequest() {
  fetchMainPage();
  sleep(0.2);
}
