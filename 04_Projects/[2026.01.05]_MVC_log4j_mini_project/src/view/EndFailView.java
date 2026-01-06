package view;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EndFailView {
    public static void displayFailMessage(String message) {
        log.warn(message);
        System.out.println(message);
    }
}
