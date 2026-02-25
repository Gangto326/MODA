import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate, Trend } from 'k6/metrics';

const errorRate = new Rate('errors');
const cardDuration = new Trend('card_creation_duration');

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

// 실제 크롤링 가능한 URL
const SSR_URLS = [
  'https://jojoldu.tistory.com/780',
  'https://jojoldu.tistory.com/781',
  'https://jojoldu.tistory.com/782',
  'https://jojoldu.tistory.com/783',
  'https://jojoldu.tistory.com/786',
  'https://jojoldu.tistory.com/787',
  'https://jojoldu.tistory.com/788',
  'https://jojoldu.tistory.com/789',
  'https://jojoldu.tistory.com/790',
  'https://jojoldu.tistory.com/791',
];

export const options = {
  scenarios: {
    // Scenario 1: 단건 SSR 요청
    single_ssr: {
      executor: 'shared-iterations',
      vus: 1,
      iterations: 1,
      exec: 'singleSSR',
      tags: { scenario: 'single_ssr' },
    },
    // Scenario 2: 동시 3건 SSR 요청
    concurrent_3: {
      executor: 'shared-iterations',
      vus: 3,
      iterations: 3,
      exec: 'concurrentSSR',
      startTime: '30s',
      tags: { scenario: 'concurrent_3' },
    },
    // Scenario 3: 지속 부하 — 2분간, 20초마다 1건
    sustained: {
      executor: 'constant-arrival-rate',
      rate: 3,
      timeUnit: '1m',
      duration: '2m',
      preAllocatedVUs: 3,
      maxVUs: 5,
      exec: 'sustainedLoad',
      startTime: '1m30s',
      tags: { scenario: 'sustained' },
    },
  },
  thresholds: {
    errors: ['rate<0.5'],
  },
};

let urlIndex = 0;

function createCard(url) {
  const payload = JSON.stringify({ url: url });
  const params = {
    headers: { 'Content-Type': 'application/json' },
    timeout: '180s',
  };
  const start = Date.now();
  const res = http.post(`${BASE_URL}/api/card`, payload, params);
  const duration = Date.now() - start;
  cardDuration.add(duration);

  const success = check(res, {
    'status is 200 or 201': (r) => r.status === 200 || r.status === 201,
  });
  errorRate.add(!success);
  console.log(`Card created: ${url} -> ${res.status} (${duration}ms)`);
  return res;
}

export function singleSSR() {
  createCard(SSR_URLS[urlIndex++ % SSR_URLS.length]);
  sleep(1);
}

export function concurrentSSR() {
  createCard(SSR_URLS[urlIndex++ % SSR_URLS.length]);
  sleep(1);
}

export function sustainedLoad() {
  createCard(SSR_URLS[urlIndex++ % SSR_URLS.length]);
}
