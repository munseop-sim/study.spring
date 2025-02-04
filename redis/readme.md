# Redis

## docker-compose 구성

- redis-master
- redis-slave - 2개
- redis-sentinel - 3개 (sentinel의 경우에는 홀수개로 해야함)
- docker-compose 실행
    ```shell
     docker-compose -f redis-sentinel-env.yml -p redis-sentinel-env  up -d  --scale redis-sentinel=3 --scale redis-slave=2 
    ```

- spring에서 접근시에는 sentinel ip로 연결정보를 설정해야하나, 도커 설정관련하여 아직 모르는 부분이 많아서 spring에서의 직접 접근은 현재(2025-02-05)
  힘들다.
  따라서 현재는 이전과 같이 redis-master로 설정하고 연결한다.