import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate, Trend, Counter } from 'k6/metrics';

const errorRate = new Rate('errors');
const sameUrlDuration = new Trend('same_url_duration');
const duplicateErrors = new Counter('duplicate_url_errors');

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
// 모든 VU가 동일 URL 사용 → Future 공유 검증
const TEST_URL = __ENV.TEST_URL || 'https://jojoldu.tistory.com/795';

export const options = {
  scenarios: {
    // 5건 동시 동일 URL 요청
    same_url_5: {
      executor: 'shared-iterations',
      vus: 5,
      iterations: 5,
      exec: 'sameUrlRequest',
      tags: { scenario: 'same_url_5' },
    },
  },
  thresholds: {
    errors: ['rate<0.9'],
  },
};

export function sameUrlRequest() {
  const payload = JSON.stringify({ url: TEST_URL });
  const params = {
    headers: { 'Content-Type': 'application/json' },
    timeout: '180s',
  };
  const start = Date.now();
  const res = http.post(`${BASE_URL}/api/card`, payload, params);
  const duration = Date.now() - start;
  sameUrlDuration.add(duration);

  const is200 = check(res, {
    'status is 200 or 201': (r) => r.status === 200 || r.status === 201,
  });
  const isDuplicate = check(res, {
    'duplicate response': (r) => r.status === 409 || (r.body && r.body.includes('중복')),
  });

  if (isDuplicate) {
    duplicateErrors.add(1);
  }
  errorRate.add(!is200 && !isDuplicate);
  console.log(`VU${__VU}: ${res.status} (${duration}ms)`);
  sleep(1);
}
