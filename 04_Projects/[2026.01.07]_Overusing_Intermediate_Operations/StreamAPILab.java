package lab02;

import java.util.Arrays;
import java.util.List;

public class StreamAPILab {

    static List<String> names = Arrays.asList("James", "Mary", "Robert", "Patricia", "John", "Jennifer", "Michael", "Linda", "David", "Elizabeth", "William", "Barbara", "Richard", "Susan", "Joseph", "Jessica", "Thomas", "Sarah", "Christopher", "Karen", "Charles", "Lisa", "Daniel", "Nancy", "Matthew", "Betty", "Anthony", "Margaret", "Mark", "Sandra", "Donald", "Ashley", "Steven", "Kimberly", "Paul", "Emily", "Andrew", "Donna", "Joshua", "Michelle", "Kenneth", "Dorothy", "Kevin", "Carol", "Brian", "Amanda", "George", "Melissa", "Timothy", "Deborah", "Ronald", "Stephanie", "Edward", "Rebecca", "Jason", "Sharon", "Jeffrey", "Laura", "Gregory", "Cynthia", "Jacob", "Kathleen", "Gary", "Amy", "Nicholas", "Angela", "Eric", "Shirley", "Jonathan", "Anna", "Stephen", "Brenda", "Larry", "Pamela", "Justin", "Emma", "Scott", "Nicole", "Brandon", "Helen", "Benjamin", "Samantha", "Samuel", "Katherine", "Gregory", "Christine", "Alexander", "Debra", "Patrick", "Rachel", "Frank", "Carolyn", "Raymond", "Janet", "Jack", "Catherine", "Dennis", "Maria", "Jerry", "Heather");

    static void m1() {
        List<String> result = names.stream()
        .filter(name -> name.startsWith("A"))
        .filter(name -> name.length() > 3)
        .map(String::toUpperCase)
        .map(name -> name + " is a name")
        .toList();

        //System.out.println(result);
    }

    static void m2() {
        List <String> result = names.stream()
        .filter(name -> name.startsWith("A") && name.length() > 3)
        .map(name -> name.toUpperCase() + " is a name")
        .toList();

        //System.out.println(result);
    }

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
