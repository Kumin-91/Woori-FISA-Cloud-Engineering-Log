# MVC Pattern과 Log4j를 적용한 미니 프로젝트

## 1. 프로젝트 목표

* Log4j 활용: 로그 기록 라이브러리를 통해 프로그램의 동작 과정을 콘솔 및 파일로 기록

* MVC Pattern 실습: 계층형 아키텍처 직접 구현 및 각 컴포넌트의 역할에 대해 이해

## 2. 프로젝트 설계 개요

### 2.1 DTO & Mock DB

* `Customer.java` - 계층 간 데이터 전송을 위한 객체

* `Database.java` - RDBMS를 대신하여 데이터를 저장하고 관리하는 저장소

### 2.2 Model

* `Model.java`- `Database` 클래스와 통신하여 CRUD 작업을 수행

### 2.3 View

* `StartView.java` - 메인 진입점 (main 메소드)으로, 사용자의 요청을 발생시키는 역할

* `EndView.java` - 요청이 성공적으로 처리되었을 때 결과를 출력

* `EndFailView.java` - 예외 상황 발생 시 에러 메시지를 출력

### 2.4 Controller

* `Controller.java` - 데이터의 유효성을 검증하고, `View`로부터 전달받은 요청을 `Model`로 전송하고, 결과에 따라 어떤 `View`를 보여줄지 결정

## 3. 구현 기능 요약

| 기능 | 상세 설명 | 주요 검증 및 비즈니스 로직 |
| --- | --- | --- |
사용자 추가 | 새로운 고객 객체를 생성하여 Database에 저장합니다. | 여유 저장 공간 확인 | 
전체 목록 조회 | 현재 저장된 모든 고객 정보를 리스트 형태로 출력합니다. | 전체 사용자 목록 출력 | 
특정 사용자 검색 | 계좌번호로 사용자를 검색합니다. | 미존재 계좌번호 입력 시 WARN 로그 및 예외 처리 | 
금액 입금 | 특정 계좌에 요청된 금액을 추가합니다. | 입금액의 양수 여부 확인 및 갱신 후 잔액 로그 기록 | 
금액 출금 | 특정 계좌에서 요청된 금액을 차감합니다. | 현재 잔액보다 큰 금액 출금 시 요청 거부 | 
금액 이체 | 출금 계좌에서 차감하여 입금 계좌로 금액을 송금합니다. | 양측 계좌 유효성 확인 | 

## 4. 로그 예시

```Plain text
2026-01-05 19:10:41 [main] DEBUG StartView = 8.1 금액 출금
2026-01-05 19:10:41 [main] INFO  Controller = 금액 출금 요청 처리
2026-01-05 19:10:41 [main] INFO  Model = AccountNumber로 은행 사용자 요청: 987-654-321
2026-01-05 19:10:41 [main] INFO  Database = AccountNumber로 은행 사용자 찾음: Customer(accountHolder=Bob, accountNumber=987-654-321, balance=3000)
2026-01-05 19:10:41 [main] INFO  Controller = 금액 출금 요청 처리 완료: 987-654-321, 거래 전 잔액: 3000
2026-01-05 19:10:41 [main] INFO  Model = 금액 출금 요청: 987-654-321, 금액: 1000
2026-01-05 19:10:41 [main] INFO  Database = 금액 출금 성공: 987-654-321, 거래 후 잔액: 2000
```

```Plain text
2026-01-05 19:10:41 [main] DEBUG StartView = 9.5 금액 이체
2026-01-05 19:10:41 [main] INFO  Controller = 금액 이체 요청 처리
2026-01-05 19:10:41 [main] INFO  Model = AccountNumber로 은행 사용자 요청: 987-654-321
2026-01-05 19:10:41 [main] INFO  Database = AccountNumber로 은행 사용자 찾음: Customer(accountHolder=Bob, accountNumber=987-654-321, balance=2000)
2026-01-05 19:10:41 [main] INFO  Model = AccountNumber로 은행 사용자 요청: 123-456-789
2026-01-05 19:10:41 [main] INFO  Database = AccountNumber로 은행 사용자 찾음: Customer(accountHolder=Alice, accountNumber=123-456-789, balance=7000)
2026-01-05 19:10:41 [main] WARN  EndFailView = 출금 금액이 잔액보다 클 수 없습니다.
```

## 5. 후기

이론으로만 접하던 MVC 패턴을 직접 코드로 구현하며 각 계층의 역할 분담을 명확히 이해할 수 있었습니다.

데이터 검증(Validation)의 위치에 대해서 많은 고민을 했습니다.

View에서의 검증은 사용자 페이지 로딩에 영향을 줄 수 있고, Model에서의 검증은 DB 리소스 낭비가 발생할 것 같아 Controller에서 1차 검증을 하도록 구현하였습니다.

Log4j2를 활용해 전체적인 프로그램의 흐름을 파일로 기록해, 실행 흐름 파악 및 트러블슈에 큰 도움이 되었습니다.