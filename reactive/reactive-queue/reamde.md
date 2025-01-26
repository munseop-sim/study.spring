# spring webflux+redis를 활용한 접속자 대기열 시스템

- 개발언어 : Java21

## 구성

### 서버1: 접속대상 서버

- 기능:실제 접속 대상 서버로서, `index`접속요청시에 대기열 체크후에 대기열이 존재한다면 대기열 서버로 `redirect`한다.
- spring mvc
- thymeleaf
- 코드 경로: `reactive/reactive-queue/website`
- 아키텍처: layered

### 서버2: 접속자 대기열 서버

- 기능: 접속대기열 관리 서버로서, 대기열 수행후에 서비스를 이용할 수 있는 상태라고 판단이 되면 요청서버로 redirect
    - process
        1. 접속자 대기열 등록(큐 등록), 큐 = `redis sorted set` 활용
        2. 프론트페이지에서 큐 등록 순위 확인 (3초에 한번씩..)
        3. 우선순위가 음수로 되어 진다면...접근가능 쿠키 생성
        4. 접근가능 쿠키로 서비스 진입 확인
        5. 다시 waiting-room으로 접근하여 접속대상 서버로 리다이렉트 수행
    - 관리 큐
        - users:queue:[큐이름]:wait
            - 접속자 대기열 큐.
            - 서비스 요청 허가시에 해당 큐에서 pop
        - users:queue:[큐이름]:proceed
            - `users:queue:[큐이름]:wait` pop하고 해당 sortedSet에 추가
            - 접근가능여부 판단시에 최초 접근시에 발급된 토큰(쿠키)와 함께 해당 큐에 데이터가 있어야 요청서버에 접근가능
- spring webflux, redis
- thymeleaf
- 코드 경로: reactive/reactive-queue/waiting-room
- 아키텍처: layered
- `SpringWebExchage`: Spring WebFlux에서 사용하는 핵심 인터페이스 중 하나. HTTP 요청 및 응답 모델을 여기에 포함시키며, 추상화된 서버측 HTTP
  요청-응답 상호작용을 제공한다.

    1. `ServerHttpRequest`: 사용자의 요청을 나타내며, 헤더, 쿠키, URI 등의 정보를 제공.
    2. `ServerHttpResponse`: 서버의 응답을 나타내며, 응답을 위한 헤더, 상태 코드 등의 정보를 제공하고 응답 본문을 작성할 수 있는 기능을 제공.
    3. `WebSession`: 현재 웹 세션을 나타냅니다. 이를 통해 세션을 생성하거나 이미 존재하는 세션에 접근가능.
    4. `ServerWebInput`: 사용자가 전송한 모든 HTTP 요청 데이터를 포함.
    5. `Model`: 데이터를 뷰에 전달하는 데 사용.

    - **_Netty나 다른 논블로킹 기반 서버와 연계된 WebFlux의 리액티브 컨텍스트를 처리하기 위한 객체_**

### local-redis 설정

- docker-compose 실행: `docker-compose -f reactive-queue-tool.yaml up -d`

```shell
# 1초마다  현재시간과 'users:queue:default:wait', 'users:queue:default:proceed' 두개의 키의 count를 출력하는 shell script 
while[true];
do date;
redis-cli zcard users:queue:default:wait;
redis-cli zcard users:queue:default:proceed;
sleep 1;
done;
```