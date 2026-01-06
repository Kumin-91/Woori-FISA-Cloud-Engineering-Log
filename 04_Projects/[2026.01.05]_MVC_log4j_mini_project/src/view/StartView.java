package view;

import lombok.extern.slf4j.Slf4j;
import model.domain.Customer;

@Slf4j
public class StartView {
    public static void main(String[] args) {
        //전체 사용자 목록 조회
        log.debug("1. 전체 사용자 목록 조회");
        controller.Controller.processAllView();

        //사용자 추가
        log.debug("2.1 사용자 추가");
        controller.Controller.processAdd(new Customer("Alice", "123-456-789", 5000));
        log.debug("2.2 사용자 추가");
        controller.Controller.processAdd(new Customer("Bob", "987-654-321", 3000));
        log.debug("2.3 사용자 추가");
        controller.Controller.processAdd(new Customer("Charlie", "555-666-777", 7000));
        log.debug("2.4 사용자 추가");
        controller.Controller.processAdd(new Customer("Daisy", "111-222-333", 4000));
        log.debug("2.5 사용자 추가");
        controller.Controller.processAdd(new Customer("Ethan", "444-555-666", 6000));
        log.debug("2.6 사용자 추가");
        controller.Controller.processAdd(new Customer("Fiona", "777-888-999", 8000));
        
        //전체 사용자 목록 조회
        log.debug("3. 전체 사용자 목록 조회");
        controller.Controller.processAllView();

        //특정 사용자 조회
        log.debug("4.1 특정 사용자 조회");
        //Charlie
        controller.Controller.processView("555-666-777");
        log.debug("4.2 특정 사용자 조회");
        //존재하지 않는 사용자
        controller.Controller.processView("999-000-111");

        //사용자 추가
        log.debug("5.1 사용자 추가");
        controller.Controller.processAdd(new Customer("George", "000-111-222", 9000));
        log.debug("5.2 사용자 추가");
        controller.Controller.processAdd(new Customer("Hannah", "333-444-555", 10000));
        log.debug("5.3 사용자 추가");
        controller.Controller.processAdd(new Customer("Ian", "666-777-888", 11000));
        log.debug("5.4 사용자 추가");
        controller.Controller.processAdd(new Customer("Jane", "999-000-111", 12000));
        log.debug("5.5 사용자 추가");
        //사용자 추가 에러
        controller.Controller.processAdd(new Customer("Kevin", "222-333-444", 13000));

        //전체 사용자 목록 조회
        log.debug("6. 전체 사용자 목록 조회");
        controller.Controller.processAllView();

        //금액 입금
        log.debug("7.1 금액 입금");
        //Alice
        controller.Controller.processDeposit("123-456-789", 2000);
        log.debug("7.2 금액 입금");
        //잘못된 금액
        controller.Controller.processDeposit("987-654-321", -500);
        log.debug("7.3 금액 입금");
        //존재하지 않는 사용자
        controller.Controller.processDeposit("000-999-888", 1000);

        //금액 출금
        log.debug("8.1 금액 출금");
        //Bob
        controller.Controller.processWithdraw("987-654-321", 1000);
        log.debug("8.2 금액 출금");
        //잘못된 금액
        controller.Controller.processWithdraw("555-666-777", 0);
        log.debug("8.3 금액 출금");
        //존재하지 않는 사용자
        controller.Controller.processWithdraw("888-777-666", 500);
        //잔액 부족 출금 시도
        log.debug("8.4 금액 출금");
        controller.Controller.processWithdraw("111-222-333", 10000);
    
        //금액 이체
        log.debug("9.1 금액 이체");
        //Daisy -> Ethan
        controller.Controller.processTransfer("111-222-333", "444-555-666", 2000);
        log.debug("9.2 금액 이체");
        //잘못된 금액
        controller.Controller.processTransfer("123-456-789", "987-654-321", -300);
        log.debug("9.3 금액 이체");
        //존재하지 않는 출금 사용자
        controller.Controller.processTransfer("000-111-222", "555-666-777", 500);
        log.debug("9.4 금액 이체");
        //존재하지 않는 입금 사용자
        controller.Controller.processTransfer("444-555-666", "999-888-777", 400);
        log.debug("9.5 금액 이체");
        //잔액 부족 이체 시도
        controller.Controller.processTransfer("987-654-321", "123-456-789", 50000);
    }
}
