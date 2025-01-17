# 1. PWA(Progressive Web App)란?

Progressive == 점진적인

1. 점진적으로 기능이 향상된다는 의미로 사용자의 브라우저나 기기가 지원하는 기능에 따라 점진적으로 더 나은 경험을 제공할 수 있다는 뜻.

2. 기존의 일반적인 웹사이트에서 시작하여 점진적으로 앱과 같은 경험으로 발전할 수 있다는 뜻.

Service Worker, Web App Manifest 등을 활용하여 모바일의 어플리케이션 (네이티브 앱)과 유사한 경험을 제공합니다.

<br>

# 2. Service Worker

브라우저와 네트워크 사이의 Proxy(대리인, 중계자) 역할을 함.

번외) 중간 과정의 존재로 얻을 수 있는 이점

1. IP주소 은폐 등의 보안상의 이점
2. 데이터를 중간 단계에 모아뒀다가 한 번에 보낼 수 있음
3. 한 번 사용한 데이터를 중간 단계에서 다시 가져올 수 있음 (Cache)
4. 부하 분산 가능
5. 한 쪽의 부재 시 중간 과정에서 데이터를 처리 & 전송 예약

<br>

브라우저 --> Service Worker --> 네트워크

브라우저에서 보낸 요청을 Service Worker에서 받고,
Service Worker에서 네트워크의 상황에 따라 맞춰 요청을 처리합니다.

따라서, Service Worker가 존재하면 네트워크가 오프라인 상태에서도 요청을 보낼 수 있고, 백그라운드에서 처리할 수 있습니다.

<br>

## 그러면 Service Worker는 서버인가요?

그렇다면 오프라인 상태에서도 결국 Service Worker는 요청을 받아야 하고,
만약 Service Worker가 서버라면 계속 실행되고 있어야 한다는 얘기입니다.

결론부터 말하자면, Service Worker는 서버가 아니라 특정 요청에 반응하는 이벤트 핸들러에 더 가깝습니다.

## Service Worker의 생명주기

#### Registration(등록)

``` js
export const register = async () => {
  // Service Worker가 지원되는 브라우저인지 확인합니다.
  if ('serviceWorker' in navigator) {
    try {
      // Service Worker 등록 시도
      // 여기서 service-worker.js 파일의 위치는 반드시 도메인의 루트에 위치해야 함 (Service Worker가 제어할 수 있는 페이지 Scope 설정)
      const registration = await navigator.serviceWorker.register('/service-worker.js');
      
      /**
       * 브라우저는 주기적으로(보통 24시간마다) Service Worker 파일의 변경여부를 확인합니다
       * 
       * service-worker.js 파일의 내용이 변경되거나,
       * Service Worker 내부에서 importScripts()로 가져오는 파일이 변경된 경우
       * "새로운 버전"으로 감지하고 Update 합니다.
      **/
      registration.addEventListener('updatefound', () => {

        /**
         * 새로운 Service Worker가 설치를 시작할 때 생성되는 참조.
         * 
         * installingWorker.state는 설치 진행상황에 따라 상태 변경을 값으로 가짐.
         *  - installing: 설치 시작
         *  - installed: 설치 완료
         *  - activating: 활성화 시작
         *  - activated: 활성화 완료
         * 
         * Service Worker의 업데이트 과정을 모니터링하고 각 단계에서 필요한 작업을 수행할 수 있음.
         */
        const installingWorker = registration.installing;
        
        // Service Worker의 상태 변경 감지
        installingWorker.addEventListener('statechange', () => {
          console.log('Service Worker 상태 변경:', installingWorker.state);
        });
      });
      
      console.log('Service Worker 등록 성공');
    } catch (error) {
      console.error('Service Worker 등록 실패:', error);
    }
  }
};
```

브라우저에 Service Worker를 등록하는 첫 단계로 Scope를 지정하며 HTTPS 환경에서만 등록할 수 있습니다.

React 앱에서는 보통 앱의 진입점에서 수행합니다.

<br>

#### Install (설치)

``` js
self.addEventListener('install', (event) => {
  console.log('Service Worker: 설치 중...');
  
  // waitUntil()을 사용하여 비동기 작업이 완료될 때까지 설치 단계 연장
  event.waitUntil(
    // 지정된 이름으로 캐시 스토리지 열기. 후술할 service-worker.js 의 설정
    caches.open(CACHE_NAME)
      .then(cache => {
        console.log('Service Worker: 파일 캐싱 중');
        // 미리 정의된 리소스들을 캐시에 저장
        return cache.addAll(urlsToCache);
      })
      .then(() => {
        // skipWaiting()을 호출하여 대기 단계를 건너뛰고 즉시 활성화
        console.log('Service Worker: 대기 단계 건너뛰기');
        return self.skipWaiting();
      })
  );
});
```

Service Worker에서 설정한 정적 리소스를 캐시에 저장합니다.

<br>

#### Activation (활성화)

``` js
self.addEventListener('activate', (event) => {
  console.log('Service Worker: 활성화 중...');
  
  event.waitUntil(
    // 모든 캐시 키를 가져와서 현재 버전과 다른 캐시를 삭제합니다.
    caches.keys().then(cacheNames => {
      return Promise.all(
        cacheNames.map(cache => {
          if (cache !== CACHE_NAME) {
            console.log('Service Worker: 이전 캐시 삭제 중');
            return caches.delete(cache);
          }
        })
      );
    })
    .then(() => {
      // clients.claim()을 호출하여 활성화 즉시 모든 클라이언트의 제어권 획득
      console.log('Service Worker: 클라이언트 제어권 획득');
      return clients.claim();
    })
  );
});
```

새로운 Service Worker로 교체하고, 이전 버전의 캐시 데이터가 존재한다면 삭제합니다.

<br>

#### Fetch (동작)

``` js
self.addEventListener('fetch', (event) => {
  console.log('Service Worker: 리소스 요청 감지', event.request.url);
  
  event.respondWith(
    // 요청된 리소스가 캐시에 있는지 확인
    caches.match(event.request)
      .then(response => {
        // 캐시에서 찾았으면 캐시된 응답 반환
        if (response) {
          console.log('Service Worker: 캐시에서 리소스 찾음', event.request.url);
          return response;
        }
        
        // 캐시에 없으면 네트워크로 요청
        console.log('Service Worker: 네트워크 요청 시작', event.request.url);
        return fetch(event.request)
          .then(response => {
            // 유효한 응답인지 확인
            // 기본적으로 같은 도메인의 응답만 캐시 --> 보안상의 위험 방지!
            if (!response || response.status !== 200 || response.type !== 'basic') {
              return response;
            }
            
            /**
             * 응답을 캐시에 저장하기 위해 복제합니다.
             * 
             * Fetch API는 Response 객체를 Stream형식으로 반환환합니다.
             * Stream은 물의 흐름으로 비유할 수 있는데, 쉬운 예시로 100MB의 파일을 1MB씩 받으며 순차적으로 처리하는 방식을 들 수 있습니다.
             * 
             * 메모리의 효율성이 좋고, 초기 응답을 빠르게 처리할 수 있다는 장점이 있으나,
             * 한 번 지나간 데이터는 다시 읽을 수 없다는 치명적인 단점이 존재합니다.
             * 
             * 따라서 응답 스트림은 복제가 필요합니다.
            **/
            const responseToCache = response.clone();
            caches.open(CACHE_NAME)
              .then(cache => {
                cache.put(event.request, responseToCache);
                console.log('Service Worker: 새로운 리소스를 캐시에 저장', event.request.url);
              });
            
            return response;
          });
          // 오프라인의 경우 예외 처리를 진행할 수 있습니다.
          .catch(error => {
            console.log('Service Worker: 네트워크 요청 실패 (오프라인)', error);
          });
      })
  );
});
```

브라우저의 모든 네트워크 요청을 가로채서 처리하며,
캐시 리소스 유무 및 네트워크의 온 오프라인 상황에 따른 로직을 구현합니다.

<br>

#### Update (업데이트)

```js
// src/components/ServiceWorkerManager.js
import React, { useEffect, useState } from 'react';

const ServiceWorkerManager = () => {
  // 새 버전 존재 여부 상태
  const [newVersionAvailable, setNewVersionAvailable] = useState(false);
  // Service Worker 등록 객체 참조 저장
  const [registration, setRegistration] = useState(null);

  useEffect(() => {
    // Service Worker 등록 함수
    const registerSW = async () => {
      if ('serviceWorker' in navigator) {
        try {
          // 브라우저가 서버에서 service-worker.js 파일을 다시 다운로드하여 Update 여부를 확인합니다.
          const reg = await navigator.serviceWorker.register('/service-worker.js');
          setRegistration(reg);
          
          // 새로운 Service Worker 감지
          reg.addEventListener('updatefound', () => {
            // 이 시점에서 새로운 Service Worker가 설치를 시작!
            const installingWorker = reg.installing;
            
            // 설치 상태 변경 감지 과정 (모니터링)
            installingWorker.addEventListener('statechange', () => {
              // 새 버전이 설치 완료되고 && 이전 버전이 활성화된 상태일 때
              if (installingWorker.state === 'installed' && navigator.serviceWorker.controller) {
                setNewVersionAvailable(true);
              }
            });
          });
          
        } catch (error) {
          console.error('Service Worker 등록 실패:', error);
        }
      }
    };

    registerSW();
  }, []);

  // 새 버전 Service Worker 활성화 함수
  const updateServiceWorker = () => {
    if (registration && registration.waiting) {
      // waiting 상태의 Service Worker에게 활성화 메시지 전송
      registration.waiting.postMessage({ type: 'SKIP_WAITING' });
      
      // 새로운 Service Worker 활성화를 위해 페이지 새로고침
      window.location.reload();
    }
  };

  return (
    <div>
      업데이트에 관한 사용자 알림 및 업데이트 설치 수락 버튼 표시.
    </div>
  );
};

export default ServiceWorkerManager;
```

Service Worker는 자동 업데이트가 가능하지만 사용자가 작성중인 데이터가 있거나 진행 중인 작업 등을 보호 등의 이유로
사용자의 수락을 받고 업데이트를 진행하는 것을 권장합니다.

<br>

### Service Worker가 네트워크와의 통신 없이 각 요청에 대한 로직을 백그라운드에서 처리할 수 있는 이유는 Install 과정의 캐시 저장과, Fetch 과정의 캐시를 확인하는 과정에 있습니다.

<br>

## Service Worker와 Cache Storage

우리가 PWA로 구현된 사이트에 접속하면 해당 사이트는 Service Worker를 사용자의 로컬 기기 내 브라우저에 설치합니다. (후술할 Web App Manifest 를 설치하는 것이 아닙니다.)

따라서 Service Worker의 설정에서 Cache Storage에 파일을 저장할 수 있습니다.

``` js
// service-worker.js
const CACHE_NAME = 'my-app-v1';
const RESOURCES_TO_CACHE = [
  '/',                    // 메인 페이지
  '/index.html',          
  '/static/js/main.js',   // 주요 JavaScript 파일
  '/static/css/style.css',// 스타일시트
  '/images/logo.png',     // 이미지
  '/service-worker.js',   // Service Worker 자신
  // 기타 필요한 리소스들
];
```

따라서 각 기기 내에 백그라운드에서 실행할 코드가 존재하기에 Service Worker가 로직을 처리할 수 있습니다.

<br>

## Cache Storage에 저장된 로직이 삭제된다면 어떻게 되나요?

Service Worker는 페이지에 접속 시 Cache Storage에 저장되어야 하는 데이터가 있는지 확인합니다. (activate | fetch)
확인 후 존재하지 않으면 사용자의 동의 없이 데이터를 캐싱할 수 있습니다. (PWA의 Update 로직과 유사합니다.)

<br>

## 브라우저의 저장공간

### Cache Storage API<br>IndexedDB

- 공통점:

    1. 두 가지 모두 사용자의 로컬 기기 내의 브라우저 저장소에 저장.
    2. 브라우저 데이터 / 캐시 삭제 시 함께 삭제됨.

- 차이점:

    1. Cache Storage는 용량 초과 시 오래된 데이터부터 자동 삭제 / IndexedDB는 에러 발생. 주기적으로 확인 및 삭제 필요.
    2. Cache Storage는 HTTP 요청, 응답으로 저장 / IndexedDB는 NoSQL(Not Only SQL) DB

<br>

## LocalStorage / SessionStorage 와 비슷한건가요?

LocalStorage와 SessionStorage는 문자열 (JSON 형식의 객체) 형식만 저장이 가능하다는 한계가 있습니다.

또한, LocalStorage와 SessionStorage는 동기적으로 동작하기에 부하가 심할 경우 저장 로직이 끝날 때 까지 UI가 멈추는 불상사가 발생할 수 있습니다.

<br>

## Cache Storage와 IndexedDB의 저장 공간은 얼마나 되나요?

사용 가능한 디스크 공간의 일정 비율로 제한됩니다.

Cache Storage는 다른 출처와 공유하여 사용, IndexedDB는 각 출처별 독립적인 저장소를 가짐.
But, IndexedDB는 경우도 저장소는 독립적이지만 전체 용량은 공유하는 구조이므로 크게 다르지 않습니다.

<br>

## 그러면 백그라운드 로직 코드를 전부 Cache Storage에 저장하나요?

### Cache Storage와 Service Worker에는 절대로 민감한 로직이나 정보가 저장되면 안됩니다!

<br>

Chrome 개발자 도구 등의 간단한 접근으로도 코드가 전부 노출되는 위험이 있습니다.

저희 서비스는 AI 처리를 백그라운드에서 할 계획이므로 AI API KEY가 노출이 됩니다.

따라서, Background Sync를 활용하려합니다.

<br>

## Background Sync

### Background Sync 동작 방식

- 서비스에 접속하지 않아도 백그라운드에서 동작 가능

- 단, PWA가 최근에 사용되지 않거나, Android 시스템의 배터리 최적화 정책 등에 의한 제한이 있을 수 있음

- Background Sync의 실행 시점은 Android 시스템이 결정한다는 점에 유의

``` js
// Service Worker
self.addEventListener('fetch', async event => {
  if (event.request.url.endsWith('/share-target')) {
    event.respondWith(
      (async () => {
        try {
          const token = await getTokenFromIndexedDB(); // PWA에 저장된 인증 토큰 확인.
          
          if (!token) {
            // 로그인이 필요한 경우 로그인 페이지로 redirect
            return Response.redirect('/login');
          }

          const formData = await event.request.formData();
          const url = formData.get('url');

          // IndexedDB에 저장
          await saveToIndexedDB({
            url,
            token,
            status: 'pending',
            timestamp: Date.now()
          });

          // Background Sync 등록
          const registration = await navigator.serviceWorker.ready;
          await registration.sync.register('process-url');

        } catch (error) {
          console.error('Error processing share:', error);
        }
      })()
    );
  }
});


// Background Sync 실행 - 사용자 접속 없이도 실행됨
// self == Service Worker의 전역 Scope
self.addEventListener('sync', event => {

  // 발생한 sync 이벤트의 태그가 'process-url'인지 확인하는 과정
  if (event.tag === 'process-url') {
    event.waitUntil(processPendingUrls());
  }
});

async function processPendingUrls() {
  try {
    // pending 상태인 데이터 가져오기
    const pendingData = await getPendingUrl();
    if (!pendingData) return;

    // 서버로 요청
    const response = await fetch('https://우리서비스.com/api/RequestUrl', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${pendingData.token}`
      },
      body: JSON.stringify({ url: pendingData.url })
    });

    if (!response.ok) {
      throw new Error('Failed to process URL');
    }

    // 결과 저장
    const result = await response.json();
    await updateUrlStatus(pendingData.url, 'completed', result);

  } catch (error) {
    console.error('Sync failed:', error);
    // 에러를 throw하면 시스템이 나중에 재시도
    throw error;
  }
}




// Background Sync 실행 - 사용자 접속 없이도 실행됨
// self == Service Worker의 전역 Scope
self.addEventListener('sync', event => {

  // 발생한 sync 이벤트의 태그가 'process-url'인지 확인하는 과정
  if (event.tag === 'process-url') {
    event.waitUntil(
      (async () => {
        // 1. IndexedDB에서 pending 상태인 데이터와 토큰 가져오기
        const { url, token } = await getPendingUrl();
        
        // 2. 서버로 요청
        const response = await fetch('https://우리서비스.com/api/처리URL', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`  // 토큰으로 사용자 식별
          },
          body: JSON.stringify({ url })
        });
        
        // 3. 서버 응답 처리
        if (response.ok) {
          const result = await response.json();
          // IndexedDB 상태 업데이트
          await updateUrlStatus(url, 'completed', result);
        } else {
          // 실패 시 재시도를 위해 에러 throw
          throw new Error('Failed to process URL');
        }
      })()
    );
  }
});

async function processPendingUrls() {
  // 1. IndexedDB에서 처리할 URL 가져오기 (getPendingUrl(): status가 'pending'인 데이터 탐색. pending 데이터가 여러개일 경우를 고려하여 로직 보완해야 함)
  const pendingUrl = await getPendingUrl();
  
  // 2. 서버에 요청 (API 키는 서버에서 관리)
  const response = await fetch('https://우리서비스.com/요청URL', {
    method: 'POST',
    body: JSON.stringify({ url: pendingUrl.url })
  });
  
  // 3. 처리 결과를 IndexedDB에 저장
  await updateUrlStatus(pendingUrl.id, 'completed', await response.json());
}
```

<br>

## 여담이지만... 사용자의 동의 없이 Service Worker를 설치하는 것이 합법인가요?

Service Worker 설치는 웹 표준의 일부이며 다양한 제약이 있습니다.

1. HTTPS가 필수로 요구됩니다.
2. 제한된 범위 내에서만 동작할 수 있습니다.
3. 푸쉬 알림과 같은 경우는 사용자의 허가를 받아야만 합니다.
4. 카메라, 마이크 등 민감한 하드웨어는 접근할 수 없습니다.
5. 데이터 접근 또한 제한됩니다.
6. 브라우저의 개발자 도구에서 확인이 가능하며 수동으로 제거할 수 있습니다.

<br>

## 우리 서비스의 백그라운드 처리 로직 예상

URL 공유 -> IndexedDB에 URL 임시 저장 -> Service Worker에서 Background Sync API 활용 -> 서버에 Request -> AI API 호출 -> 결과 저장 -> 서비스 접속 시 IndexedDB의 데이터 바로 사용 (RDBMS에 데이터 전송 및 IndexedDB의 데이터 제거)

Service Worker를 사용하여 시간이 오래 걸리는 AI API 호출 및 정보 가공을 마치 오프라인 상태의 서버에서 처리하는 듯한 느낌을 전합니다.

iOS 에서는 fetch API만 사용 가능 (fetch 이벤트 핸들러는 보통 30초 동안만 실행 가능)
긴 시간이 필요한 작업은 Background Sync API 사용 필요

<br>

# 3. Web App Manifest

PWA의 핵심 구성 요소로 웹 애플리케이션의 메타데이터를 정의하는 JSON 파일이며, PWA 앱 설치를 위한 기술입니다.

브라우저에서 설치 프롬프트를 표시하고 사용자가 명시적으로 설치해야 합니다.
설치 시 홈 화면에 앱 아이콘이 추가됩니다.

```json
// manifest.json
{
  "short_name": ,    // 홈 화면에 표시될 짧은 이름
  "name":   // 전체 애플리케이션 이름
  "icons": [
    {
      "src": ,
      "sizes": ,
      "type": ,
      "purpose": "any maskable"  // 안드로이드의 적응형 아이콘 지원
    },
    {
      "src": ,  // PWA 설치에 필요한 큰 아이콘
      "sizes": ,
      "type": 
    }
  ],
  "start_url": ".",         // 앱 실행 시 시작 URL
  "display": "standalone",   // 브라우저 UI 없이 독립 실행,  "browser" // 일반 브라우저와 동일하게 표시
  "theme_color": , // 상태바 색상
  "background_color":   // 초기 로딩 화면의의 배경색
}
```

우리가 활용할 Web Share Target API (모바일 내의 공유하기) 설정 또한 manifest.json에 추가합니다. (iOS의 경우 지원하지 않습니다. 따라서 일반적인 공유 시트를 통해 공유합니다.)

```json
// manifest.json
{
  "share_target": {
    "action": "/share-target",
    "method": "POST",
    "enctype": "multipart/form-data",
    "params": {
      "title": ,      // 공유된 콘텐츠의 제목
      "text": ,        // 공유된 텍스트
      "url": ,          // 공유된 URL
      "files": [{           // 공유된 파일 (이미지 등)
        "name": "media",
        "accept": ["image/*", "video/*"]
      }]
    }
  }
}
```