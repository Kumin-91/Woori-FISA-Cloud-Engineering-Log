package controller;

import lombok.extern.slf4j.Slf4j;
import model.domain.Customer;
import model.Model;
import view.EndView;
import view.EndFailView;

@Slf4j
public class Controller {
    //1. 모든 은행 사용자 요청 처리
    public static void processAllView() {
        log.info("모든 은행 사용자 요청 처리");
        EndView.displayAllView(Model.getAllCustomers());
    }

    //2. ID로 은행 사용자 요청 처리
    public static void processView(String accountNumber) {
        log.info("AccountNumber로 은행 사용자 요청 처리");
        Customer customer = Model.findCustomerByAccountNumber(accountNumber);
        if (customer != null) {
            EndView.displayView(customer);
        } else {
            EndFailView.displayFailMessage("해당 AccountNumber의 은행 사용자를 찾을 수 없습니다.");
        }
    }

    //3. 새 은행 사용자 추가 요청 처리
    public static void processAdd(Customer newCustomer) {
        log.info("새 은행 사용자 추가 요청 처리");
        boolean isSuccess = Model.addNewCustomer(newCustomer);
        if (isSuccess) {
            EndView.displayAddResult();
        } else {
            EndFailView.displayFailMessage("새 은행 사용자 추가에 실패했습니다. 공간이 부족합니다.");
        }
    }

    //4. 금액 입금 요청 처리
    public static void processDeposit(String accountNumber, int amount) {
        log.info("금액 입금 요청 처리");
        //금액 예외 처리
        if (amount <= 0) {
            EndFailView.displayFailMessage("입금 금액은 0보다 커야 합니다.");
            return;
        }

        Customer customer = Model.findCustomerByAccountNumber(accountNumber);

        //사용자를 찾지 못한 경우 처리
        if (customer == null) {
            EndFailView.displayFailMessage("해당 AccountNumber의 은행 사용자를 찾을 수 없습니다.");
            return;
        }

        //입금 처리
        log.info("금액 입금 요청 처리 완료: {}, 거래 전 잔액: {}", customer.getAccountNumber(), customer.getBalance());
        Model.depositToAccount(accountNumber, amount);
    }

    //5. 금액 출금 요청 처리
    public static void processWithdraw(String accountNumber, int amount) {
        log.info("금액 출금 요청 처리");
        //금액 예외 처리
        if (amount <= 0) {
            EndFailView.displayFailMessage("출금 금액은 0보다 커야 합니다.");
            return;
        }

        Customer customer = Model.findCustomerByAccountNumber(accountNumber);

        //사용자를 찾지 못한 경우 처리
        if (customer == null) {
            EndFailView.displayFailMessage("해당 AccountNumber의 은행 사용자를 찾을 수 없습니다.");
            return;
        }

        //출금 가능 금액 체크
        if (customer.getBalance() < amount) {
            EndFailView.displayFailMessage("출금 금액이 잔액보다 클 수 없습니다.");
            return;
        }

        //출금 처리
        log.info("금액 출금 요청 처리 완료: {}, 거래 전 잔액: {}", customer.getAccountNumber(), customer.getBalance());
        Model.withdrawFromAccount(accountNumber, amount);
    }

    //6. 금액 이체 요청 처리
    public static void processTransfer(String fromAccountNumber, String toAccountNumber, int amount) {
        log.info("금액 이체 요청 처리");
        //금액 예외 처리
        if (amount <= 0) {
            EndFailView.displayFailMessage("이체 금액은 0보다 커야 합니다.");
            return;
        }

        Customer fromCustomer = Model.findCustomerByAccountNumber(fromAccountNumber);
        Customer toCustomer = Model.findCustomerByAccountNumber(toAccountNumber);

        //출금 계좌 사용자 체크
        if (fromCustomer == null) {
            EndFailView.displayFailMessage("출금 계좌의 은행 사용자를 찾을 수 없습니다.");
            return;
        }
        //입금 계좌 사용자 체크
        if (toCustomer == null) {
            EndFailView.displayFailMessage("입금 계좌의 은행 사용자를 찾을 수 없습니다.");
            return;
        }

        //출금 가능 금액 체크
        if (fromCustomer.getBalance() < amount) {
            EndFailView.displayFailMessage("출금 금액이 잔액보다 클 수 없습니다.");
            return;
        }

        //이체 처리
        log.info("금액 이체 요청 처리 완료: {} -> {}, 금액: {}", fromAccountNumber, toAccountNumber, amount);
        Model.withdrawFromAccount(fromAccountNumber, amount);
        Model.depositToAccount(toAccountNumber, amount);
    }
}
