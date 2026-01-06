package model;

import lombok.extern.slf4j.Slf4j;
import model.domain.Customer;

@Slf4j
public class Database {
    //은행 사용자를 저장하는 배열
    private static Customer[] customers = new Customer[10];
    //기본 생성자 막기
    private Database() {}

    //모든 은행 사용자 반환
    public static Customer[] getCustomers() {
        log.info("모든 은행 사용자 반환");
        return customers;
    }

    //ID로 은행 사용자 찾기
    public static Customer getCustomerByAccountNumber(String accountNumber) {
        //배열 순회하며 ID로 사용자 찾기
        for (Customer customer : customers) {
            //null 체크 및 ID 비교
            if (customer != null && customer.getAccountNumber().equals(accountNumber)) {
                log.info("AccountNumber로 은행 사용자 찾음: {}", customer);
                return customer;
            }
        }

        //사용자 못 찾음
        log.warn("AccountNumber로 은행 사용자를 찾지 못함: {}", accountNumber);
        return null;
    }

    //새 은행 사용자 추가
    public static boolean addCustomer(Customer newCustomer) {
        //빈 슬롯을 찾아 새 사용자 추가
        for (int i = 0; i < customers.length; i++) {
            if (customers[i] == null) {
                customers[i] = newCustomer;
                log.info("새 은행 사용자 추가: {}", newCustomer);
                return true;
            }
        }

        //빈 슬롯이 없으면 추가 실패
        log.error("새 은행 사용자 추가 실패, 공간 부족: {}", newCustomer);
        return false;
    }

    //금액 입금
    public static void depositAmount(String accountNumber, int amount) {
        for (int i = 0; i < customers.length; i++) {
            if (customers[i] != null && customers[i].getAccountNumber().equals(accountNumber)) {
                customers[i].setBalance(customers[i].getBalance() + amount);
                log.info("금액 입금 성공: {}, 거래 후 잔액: {}", accountNumber, customers[i].getBalance());
                return;
            }
        }
    }

    //금액 출금
    public static void withdrawAmount(String accountNumber, int amount) {
        for (int i = 0; i < customers.length; i++) {
            if (customers[i] != null && customers[i].getAccountNumber().equals(accountNumber)) {
                customers[i].setBalance(customers[i].getBalance() - amount);
                log.info("금액 출금 성공: {}, 거래 후 잔액: {}", accountNumber, customers[i].getBalance());
                return;
            }
        }
    }
}