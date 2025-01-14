## 0\. 서론

여러 컴퓨터에서 동일한 환경 아래 프로젝트를 실행하기 위해 사용되는 docker를 사용한다.

기존에 작업했던 Spring Boot 프로젝트를 dockerizing하는 과정을 기술한다.

기본적으로 docker desktop 설치와 docker hub에 repository 생성법 등은 생략하고 진행한다.

### 0-1. Dockerizing 순서

Dockerizing을 하기 위한 일반적인 순서는 다음과 같다.

1.  Dockerfile 작성
2.  .dockerignore 작성
3.  Docker image 생성 및 실행
4.  Docker image 푸시

하지만 mysql 기반 데이터베이스 컨테이너도 생성하여 서버에서 db 연결까지 진행하기 위해 다음과 같이 진행한다.

1.  Dockerfile 작성
2.  배포용 설정 파일 작성
3.  .dockerignore 작성
4.  docker-compose.yml 작성
5.  Docker image 생성 및 실행
6.  Docker image 푸시

Dockerizing 과정에서 작성하는 파일들은 기본적으로 프로젝트 루트 폴더에 생성하면 된다.

---

## 1\. Dockerfile 작성

Docker image를 생성하기 위해 필요한 파일로 반드시 "Dockerfile" 이름으로 지어져있어야 한다.Dockerfile은 항상 "FROM" 명령으로 시작해야 하며, FROM이 여러개 있는 경우 멀티 스테이지 빌드라고 부른다.다음은 내 프로젝트를 빌드하기 위해 작성한 Dockerfile이다.

```
## 첫번째 스테이지
# 빌드를 위해 자바 17 이미지 사용
FROM openjdk:17-jdk-slim AS build

# 필수 패키지 설치 및 작업 디렉토리 설정
RUN apt-get update

# 작업 디렉토리 설정
WORKDIR /app

# 소스 코드 및 Maven Wrapper 복사
COPY ./mvnw ./mvnw
COPY ./.mvn ./.mvn
COPY ./pom.xml ./pom.xml
COPY ./src ./src

# Maven 빌드 실행 (테스트 제외를 원하면 -DskipTests)
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

## 두번째 스테이지
# 실행을 위해 자바 17 slim 이미지 사용
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# wait-for-it 스크립트 다운로드 및 실행 권한 부여
ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x /app/wait-for-it.sh

# jar파일 복사
COPY --from=build /app/target/*.jar app.jar

# wait-for-it을 사용하여 DB 연결 확인 후 앱 시작
ENTRYPOINT ["/bin/sh", "-c", "./wait-for-it.sh db:3306 -t 180 -- java -jar app.jar"]
```

### 1-1. 첫번째 스테이지

첫번째 스테이지는 컨테이너에서 내 프로젝트를 빌드하여 jar파일을 만들기 위해 작성되었다.

해당 스테이지가 존재하지 않는다면 로컬에서 프로젝트를 개발한 후에 jar파일을 수동으로 만들어주는 불편함이 생겨 작성했다.

```
# 빌드를 위해 자바 17 이미지 사용
FROM openjdk:17-jdk-slim AS build
```

베이스 이미지로 JDK 17이 설치된 경량화된 이미지를 사용하겠다는 의미이며, "AS build"로 이 스테이지에 'build'라는 이름을 지정해 나중에 참조할 수 있게 해주었다.

```
# 작업 디렉토리 설정
WORKDIR /app
```

컨테이너 내부에서 작업할 기본 디렉토리 설정으로, 이후에 실행되는 모든 명령어는 이 디렉토리를 기준으로 실행된다.

```
# 소스 코드 및 Maven Wrapper 복사
COPY ./mvnw ./mvnw
COPY ./.mvn ./.mvn
COPY ./pom.xml ./pom.xml
COPY ./src ./src
```

Maven 프로젝트로 진행했기 때문에 Maven 빌드에 필요한 파일들을 컨테이너로 복사하는 과정이다.

-   mvnw: Maven Wrapper 실행 스크립트
-   .mvn: Maven Wrapper 관련 설정 파일들
-   pom.xm: Maven 프로젝트 설정 파일
-   src: 프로젝트 소스 코드

```
# Maven 빌드 실행 (테스트 제외를 원하면 -DskipTests)
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests
```

Maven Wrapper 스크립트인 mvnw에 실행 권한을 부여한 뒤, 프로젝트를 빌드하여 jar파일이 생성되게 된다.

여기서 "-DskipTests"는 프로젝트 빌드 시에 테스트는 스킵하라는 의미이다.

#### Gradle 프로젝트인 경우

더보기

Gradle 프로젝트인 경우 아래 내용으로 수정하여 실행시키면 된다.

```
# 소스 코드 및 Gradle 관련 파일 복사
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .
COPY ./gradle ./gradle
COPY ./src ./src

# Gradle 빌드 실행 (테스트 제외를 원하면 "-x test")
RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar -x test
```

### 1-2. 두번째 스테이지

두번째 스테이지는 컨테이너를 생성하고 실행하기 위해 작성되었다.

첫번째 스테이지와 동일한 부분은 생략하고 설명한다.

```
# wait-for-it 스크립트 다운로드 및 실행 권한 부여
ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x /app/wait-for-it.sh
```

wait-for-it 스크립트를 다운로드하고 실행 권한을 부여한다.

해당 스크립트는 나중에 데이터베이스가 준비될 때까지 애플리케이션 실행을 대기하는 역할을 해준다.

```
# jar파일 복사
COPY --from=build /app/target/*.jar app.jar
```

"--from=build"를 속성으로 첫번째 스테이지에서 생성된 jar 파일을 현재 스테이지로 복사한다는 의미이다.

```
# wait-for-it을 사용하여 DB 연결 확인 후 앱 시작
ENTRYPOINT ["/bin/sh", "-c", "./wait-for-it.sh db:3306 -t 180 -- java -jar app.jar"]
```

ENTRYPOINT는 해당 컨테이너가 시작 시 실행되는 부분이다.

wait-for-it.sh를 이용하여 db:3306이 준비될 때까지 최대 180초 대기하게 되며, 데이터베이스가 준비되면 app.jar를 실행하도록 작성하였다.

---

## 2\. 배포용 설정 파일 작성

Spring Boot의 설정 파일인 application.properties에는 노출 시 위험한 api키들이 작성되어 있기 때문에 그대로 배포하게 되면 위험하게 된다.

따라서 api키를 숨기지 않고 로컬에서만 사용할 설정 파일과 git에 올리거나 docker에 배포해도 문제가 되지 않는 배포용 설정 파일을 분리하여 사용해야 한다.

저는 배포용으로 applicaion.properties를, 로컬용으로는 application-local.properties로 결정했다.

(배포용으로 application.properties를 사용하지 않으면 Dockerfile을 수정해야 한다.)

```
spring.ai.openai.api-key=${OPENAI_API_KEY}
```

배포를 위한 설정파일에 외부 노출이 위험한 값들은 모두 위와 같이 환경변수로 바꾸어서 작성한다.

이후 docker 컨테이너에 환경변수를 추가하게 되면 알아서 환경변수로부터 값을 읽어온다.

(이 과정은 4. docker-compose.yml 작성 시에 설정한다.)

### 2-1. 로컬 설정 파일로 실행하는 법

Intellij를 기준으로 다음과 같다.

실행 버튼 옆 더보기 → Edit... → Active profiles에 local 작성 → Apply

[##_Image|kage@CTM9C/btsLDU0LCuW/TXzWXySRpaJKMbsyouLLx1/img.png|CDM|1.3|{"originWidth":934,"originHeight":286,"style":"alignCenter","width":400,"height":122}_##][##_Image|kage@ybJ0r/btsLEqZr0d3/7xDELY3fKfmtkvbrR1LO71/img.png|CDM|1.3|{"originWidth":1196,"originHeight":1084,"style":"alignCenter","width":400,"height":363}_##]

---

## 3\. .dockerignore 작성

dockerignore는 gitignore와 유사하게 Dockerfile에서 COPY를 진행할 때 복사가 되지 않을 파일을 작성해주면 된다.

여기서 컨테이너에 properties가 2개 이상인 경우 에러가 생기는 이슈가 발생했었다.

따라서 컨테이너 내부에 application.properties만 존재하도록 local을 빼주었다.

```
# Git 관련 파일 제외
.git
.gitignore

# 빌드 산출물 제외
target/

# 로컬 설정 제외
application-local.properties
```

---

## 4\. docker-compose.yml 작성

docker-compose.yml은 다음과 같은 역할을 하는 컨테이너 오케스트레이션 설정 파일이다.

-   Spring Boot 애플리케이션과 MySQL을 설정
-   두 서비스 간의 네트워크 연결 구성 (networks)
-   데이터베이스 초기화 및 데이터 영구 저장 설정 (volumes)
-   환경변수를 통한 각종 설정값 주입 (environment)
-   서비스의 상태 체크 및 의존성 관리 (depneds\_on)

```
services:
  app:
    build:
      context: . # 현재 디렉토리를 빌드 컨텍스트로 사용
      dockerfile: Dockerfile  # 사용할 Dockerfile 지정
    image: retripver-backend  # 빌드될 이미지 이름
    container_name: retripver-backend     # 실행될 컨테이너 이름
    ports:
      - "8080:8080" # 호스트의 8080 포트를 컨테이너의 8080 포트에 매핑
    environment: #환경변수 등록
      SPRING_DATASOURCE_URL: jdbc:mysql://db:3306/{DB 스키마명, (선택)TimeZone 등의 설정}
      SPRING_DATASOURCE_USERNAME: {DB 사용자명}
      SPRING_DATASOURCE_PASSWORD: {DB 비밀번호}
      OPENAI_API_KEY: 0
    depends_on:
      db:
        condition: service_healthy  # db가 healthy 상태가 될 때까지 기다림
    networks:
      - backend

  db:
    image: mysql:8.0
    healthcheck: # 데이터베이스 상태 체크
      test: [ "CMD", "mysqladmin", "ping", "-h", "localhost" ]
      interval: 10s # 10초마다 체크
      timeout: 5s   # 5초 타임아웃
      retries: 5    # 5번 재시도
    environment:
      LANG: C.UTF-8
      MYSQL_ROOT_PASSWORD: {root 계정 비밀번호}
      MYSQL_DATABASE: {생성할 데이터베이스명}
      MYSQL_USER: {생성할 사용자명}
      MYSQL_PASSWORD: {사용자 비밀번호}
    volumes:
      - db_data:/var/lib/mysql # 데이터 영구 저장용 볼륨
      # 초기 데이터베이스 설정 스크립트
      - ./src/main/resources/database/schema.sql:/docker-entrypoint-initdb.d/01.schema.sql
      - ./src/main/resources/database/trip-data.sql:/docker-entrypoint-initdb.d/02.trip-data.sql
      - ./src/main/resources/database/data.sql:/docker-entrypoint-initdb.d/03.data.sql
    networks:
      - backend

networks:
  backend: # backend라는 이름의 네트워크 생성

volumes:
  db_data: # 데이터베이스 데이터 저장용 볼륨 정의
```

---

## 5\. Docker image 생성 및 실행

```
# image 생성
# 태그는 버전과 비슷하며 생략이 가능하다.
docker build -t {이미지이름:태그} {Dockerfile 경로}

# image 생성 확인
docker images

# image 실행 = 컨테이너 생성
docker run {이미지 이름}

# 컨테이너 실행 확인
docker ps

# 컨테이너 종료
# docker stop {컨테이너 ID | 컨테이너명}

# 컨테이너 삭제
docker rm {컨테이너 ID | 컨테이너명}
```

### 5-1. docker-compose를 사용하는 경우

```
# 기본 실행
docker-compose up

# 빌드 후 실행
docker-compose up --build

# 컨테이너 중지
docker-compose stop

# 컨테이너 중지 및삭제
docker-compose down

# 컨테이너 및 볼륨 삭제
docker-compose down -v
```

---

## 6\. Docker image 푸시

이제 마지막으로 생성한 image를 repository에 푸시하는 방법이다.

먼저 Docker Hub에서 repository를 만든다.

Docker Hub에서 repository명은 이미지명이라고 생각해도 무방하다.

 [Docker Hub Container Image Library | App Containerization

Increase your reach and adoption on Docker Hub With a Docker Verified Publisher subscription, you'll increase trust, boost discoverability, get exclusive data insights, and much more.

hub.docker.com](https://hub.docker.com/)

기본적으로 docker hub에는 1서비스 1레포지토리를 권장하고 있다.

그래서 만약 프론트 서버, 백 서버를 모두 올리고 싶은 경우 레포지토리 2개 만드는 것이 좋다.

```
docker login
```

Docker Hub에 로그인을 먼저 해준다.

```
docker tag {로컬 이미지명} {Docker Hub 사용자명}/{레포지토리명}
```

로컬 이미지에 태그를 달아 어떤 repository에 올릴 지 설정한다.

```
docker push {Docker Hub 사용자명}/{레포지토리명}
```