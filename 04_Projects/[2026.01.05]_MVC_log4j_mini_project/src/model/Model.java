package model;

import lombok.extern.slf4j.Slf4j;
import model.domain.Customer;

@Slf4j
public class Model {
    //1. Database 클래스의 모든 은행 사용자 반환 메서드 호출
    public static Customer[] getAllCustomers() {
        log.info("모든 은행 사용자 요청");
        return Database.getCustomers();
    }

    //2. Database 클래스의 ID로 은행 사용자 찾기 메서드 호출
    public static Customer findCustomerByAccountNumber(String accountNumber) {
        log.info("AccountNumber로 은행 사용자 요청: {}", accountNumber);
        return Database.getCustomerByAccountNumber(accountNumber);
    }

    //3. Database 클래스의 새 은행 사용자 추가 메서드 호출
    public static boolean addNewCustomer(Customer newCustomer) {
        log.info("새 은행 사용자 추가 요청: {}", newCustomer);
        return Database.addCustomer(newCustomer);
    }

    //4. 금액 입금 처리 메서드
    public static void depositToAccount(String accountNumber, int amount) {
        log.info("금액 입금 요청: {}, 금액: {}", accountNumber, amount);
        Database.depositAmount(accountNumber, amount);
    }

    //5. 금액 출금 처리 메서드
    public static void withdrawFromAccount(String accountNumber, int amount) {
        log.info("금액 출금 요청: {}, 금액: {}", accountNumber, amount);
        Database.withdrawAmount(accountNumber, amount);
    }
}
