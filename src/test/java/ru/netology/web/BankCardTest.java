package ru.netology.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;


import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class BankCardTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    public String generateDate(int addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test
    void shouldSendRequestManualInput() {

        String planningDate = generateDate(4, "dd.MM.yyyy");

        $("[data-test-id='city'] input").sendKeys("Ростов-на-Дону");

        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id='date'] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").sendKeys(planningDate);

        $("[data-test-id='name'] input").sendKeys("Иван Иванов-Петров");
        $("[data-test-id='phone'] input").sendKeys("+79000000000");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $$(".notification__title").find(exactText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $$(".notification__content").find(exactText("Встреча успешно забронирована на " + planningDate)).shouldBe(visible);
    }
    @Test
    void shouldAutocompleteDateAndCity() {
        String planningDayOfDate = generateDate(7, "d");
        String planningDate = generateDate(7, "dd.MM.yyyy");

        // город
        $("[data-test-id='city'] input").sendKeys("ле");
        $$("div .menu.menu_mode_radio-check.input__menu .menu-item__control").find(exactText("Салехард")).click();
        // календарь
        $("button .icon_name_calendar").click();

        if(!generateDate(3,"MM").equals(generateDate(7, "MM"))) {
            $(".calendar__arrow[data-step='1']").click();
        }
        $$(".calendar__day").find(exactText(planningDayOfDate)).click();
        // остальные поля
        $("[data-test-id='name'] input").sendKeys("Иван Иванов-Петров");
        $("[data-test-id='phone'] input").sendKeys("+79000000000");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        //уведомление
        $$(".notification__title").find(exactText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $$(".notification__content").find(exactText("Встреча успешно забронирована на " + planningDate)).shouldBe(visible);
    }
}
