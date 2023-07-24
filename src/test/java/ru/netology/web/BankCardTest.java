package ru.netology.web;

import com.codeborne.selenide.conditions.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

public class BankCardTest {
    private String dateForMeet = "04.08.2023"; //дата встречи
    private String monthYear = "август 2023"; //месяц и год (в ".calendar__name")
    private String dayOfMonth = "4"; //число в дате (в ".calendar__day")
    private int n = 1; // сколько раз нажать на ">" (для счетчика)

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSendRequestManualInput() {

        $("[data-test-id='city'] input").sendKeys("Ростов-на-Дону");

        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id='date'] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").sendKeys(dateForMeet);

        $("[data-test-id='name'] input").sendKeys("Иван Иванов-Петров");
        $("[data-test-id='phone'] input").sendKeys("+79000000000");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();

        $("[data-test-id='notification']").shouldHave(exactText("Успешно! Встреча успешно забронирована на " + dateForMeet), Duration.ofSeconds(15));
    }

    @Test
    void shouldAutocompleteDateAndCity() {
        // город
        $("[data-test-id='city'] input").sendKeys("ка");
        $$(".input__menu .menu-item__control").find(exactText("Казань")).click();

        // календарь
        $(".icon_name_calendar").click();
        // счетчик для 'click':
        for (int i = 0; i < n; i++) {
            $("[data-step='1'].calendar__arrow").click();
        }
        $(".calendar__name").shouldHave(text(monthYear));
        $$(".calendar__day").find(exactText(dayOfMonth)).click();
        $("[data-test-id='date'] input").shouldHave(new Value(dateForMeet));

        $("[data-test-id='name'] input").sendKeys("Иван Иванов-Петров");
        $("[data-test-id='phone'] input").sendKeys("+79000000000");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();

        $("[data-test-id='notification']").shouldHave(exactText("Успешно! Встреча успешно забронирована на " + dateForMeet), Duration.ofSeconds(15));
    }
}
