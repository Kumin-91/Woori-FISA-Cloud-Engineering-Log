package view;

import lombok.extern.slf4j.Slf4j;
import model.domain.Customer;

@Slf4j
public class EndView {
    //모든 은행 사용자 목록 출력
    public static void displayAllView(Customer[] customers) {
        log.info("모든 은행 사용자 목록 출력");
        System.out.println("=== 모든 은행 사용자 목록 ===");
        for (Customer customer : customers) {
            if (customer != null) {
                System.out.println(customer);
            }
        }
    }

    //단일 은행 사용자 정보 출력
    public static void displayView(Customer customer) {
        log.info("단일 은행 사용자 정보 출력");
        System.out.println("=== 은행 사용자 정보 ===");
        System.out.println(customer);
    }

    //새 은행 사용자 추가 결과 출력
    public static void displayAddResult() {
        log.info("새 은행 사용자 추가 성공 출력");
        System.out.println("새 은행 사용자가 성공적으로 추가되었습니다.");
    }
}
