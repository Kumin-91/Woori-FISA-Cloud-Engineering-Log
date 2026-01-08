# Overusing Intermediate Operations: Chaining too many intermediate operations (like filter() and map()) can introduce performance overhead.

> 4. Overusing Intermediate Operations:Mistake: Chaining too many intermediate operations (like filter() and map()) can introduce performance overhead.

## 1. 개요: 중간 연산 과다 사용 (Overusing Intermediate Operations)
스트림 API 사용 시 `filter()`나 `map()`과 같은 중간 연산을 여러개 사용 하는 것은 코드의 가독성을 높일 수 있으나, 시스템의 성능에 영향을 미칠 가능성이 있다.

### 로직 구현 방식 비교
    
* m1 (비효율): 연산을 단계별로 분리하여 4개의 중간 스트림 생성

    ```Java
    static void m1() {
        List<String> result = names.stream()
        .filter(name -> name.startsWith("A"))
        .filter(name -> name.length() > 3)
        .map(String::toUpperCase)
        .map(name -> name + " is a name")
        .toList();
    }
    ```

* m2 (효율): 논리 연산자 (`&&`)와 문자열 결합을 통해 중간 스트림을 2개로 줄임

    ```Java
    static void m2() {
        List <String> result = names.stream()
        .filter(name -> name.startsWith("A") && name.length() > 3)
        .map(name -> name.toUpperCase() + " is a name")
        .toList();
    }
    ```



## 2. 성능 측정 실험 (Benchmark)

### 2.1 실험 환경

* 반복 횟수: 10, 100, 1000, 10000회 실행 (나노초 단위 측정)

* 워밍업: 20000회 (JIT 컴파일러 활성화 유도)

```Java
public class StreamAPILab {
    //예제 데이터 (부분 생략)
    static List<String> names = Arrays.asList("James", "Mary", "Robert", ...);

    public static void main(String[] args) {
        long totalTime = 0, beforeTime, afterTime;
        long maxTime = Long.MIN_VALUE, minTime = Long.MAX_VALUE;
        //반복 횟수
        final int START = 10000000;

        //워밍업
        for (int i = 0; i < 20000; i++) {
            m1();
            //m2();
        }

        //측정
        for (int i = 0; i < START; i++) {
            beforeTime = System.nanoTime();
            m1();
            //m2();
            afterTime = System.nanoTime();

            //시간 기록
            totalTime += afterTime - beforeTime;
            maxTime = Math.max(maxTime, afterTime - beforeTime);
            minTime = Math.min(minTime, afterTime - beforeTime);
        }

        System.out.println("Avg: " + (totalTime / START) + "ns");
        System.out.println("Max: " + maxTime + "ns");
        System.out.println("Min: " + minTime + "ns");
    }
}
```

### 2.2 데이터 규모별 성능 측정 결과 (Avg, ns)

| 데이터 크기 | 비효율 (m1) | 효율 (m2) | 효율 개선율 % |
| --- | --- | --- | --- |
| 10건 | 65 | 47 | 27.7% |
| 100건 | 134 | 107 | 20.2% |
| 1,000건 | 804 | 778 | 3.3% |
| 10,000건 | 6722 | 6716 | 0.1% |

두 함수 모두 최대 시간과 최소 시간이 100배 이상 차이남

ex) 데이터가 1000건일 경우 m2에서 max 622481ns min 403ns 

Why? -> 노이즈 

## 3. 심층 분석: max값과 min값의 차이가 왜이렇게 큰가?

1. GC 발생 -> 가장 많은 시간 발생. 이것으로 추정중

2. JIT 재컴파일

3. OS 스레드 preemption

4. CPU 캐시 flush

5. TLAB 재할당

6. class loading / inline cache miss

## 3.1 JMH가 필요하다 

사실 우리가 한 실험은 실제 "함수의 기능"만 포함된 것이 아니다.

JMH (Java Microbenchmark Harness) 를 사용해서 벤치마킹을 해야한다!

JMH는 자동으로:

1. 워밍업

2. GC 분리

3. outlier 제거

4. 통계적 신뢰 구간

5. dead-code elimination 방지 를 해준다.





### 4. 왜 결국 두 함수의 시간차이가 없어졌나? => JVM의 자동 최적화 (JIT Inlining)

실제 연산 비용이 지배적으로 커져서 m1/m2 구조 차이가 의미를 잃는다.

왜 구조적 차이를 잃어버리게 될까?

워밍업을 한후 JIT가
1. 람다 인라이닝
2. 루프 합치기
3. 분기 예측 최적화

따라서 결국 Stream 파이프라인의 구조적 차이"가 기계어 수준에서 거의 사라졌기 때문이다.


즉 데이터 크기가 커지면, 연산 오버헤드가 Stream구조에서 오는 이점을 덮어버린다.


## 5. 결론 및 인사이트


* max/min의 극단적 차이는 노이즈 때문이다 이것이 평균을 더 흐리게 만든다.

* 데이터가 적을 때는 로직 개선(중간 연산 통합)이 전체 성능의 큰 비중을 차지

* 데이터 규모가 커질수록 개선 불가능한 고정 비용(연산비용)이 큰 비중을 차지

* 결과: 데이터가 10,000건에 도달하면 메모리 I/O와 최종 결과 도출 시간이 전체의 대부분을 점유하여 로직 개선 효과가 미미해짐

## 6. 추가로 알게 된 점
        
* 10,000개의 짧은 문자열 자체의 크기는 작지만, 이를 하드코딩하면 JVM이 이를 관리하기 위해 수십 배의 메모리 관리 비용을 지불하게 됨...
```JAVA
static List<String> names = Arrays.asList("James", "Mary", "Robert", ...);

```

    
![00_5](./Images/00_6.png)

## 7. 추가로 생각해 보아야 할것

* 만약 StreamAPI를 병렬 stream으로 생성해서 데이터를 처리하도록 한다면?

* JMH를 사용해서 노이즈를 최대한 제거한 후 함수"기능"만의 시간은?