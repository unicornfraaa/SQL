package ru.netology.sql.tests;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.sql.data.DataHelper;
import ru.netology.sql.data.SQLHelper;
import ru.netology.sql.page.DashboardPage;
import ru.netology.sql.page.LoginPage;
import ru.netology.sql.page.VerificationPage;

import static com.codeborne.selenide.Selenide.open;

public class VerificationTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        Configuration.holdBrowserOpen = true;
    }

    @AfterAll
    static void dropData() {
        SQLHelper.cleanDataBase();
    }

    @Test
    void shouldPassVerification() {
        LoginPage loginPage = new LoginPage();
        VerificationPage verificationPage = loginPage.validAuthInfo(DataHelper.getValidAuthInfo());
        DashboardPage dashboardPage = verificationPage.validVerify(SQLHelper.getVerificationCode());
        dashboardPage.shouldVisible();
    }

    @Test
    void shouldOtherPassVerification() {
        LoginPage loginPage = new LoginPage();
        VerificationPage verificationPage = loginPage.validAuthInfo(DataHelper.getValidOtherAuthInfo());
        DashboardPage dashboardPage = verificationPage.validVerify(SQLHelper.getVerificationCode());
        dashboardPage.shouldVisible();
    }

    @Test
    void shouldShowErrorMessageAfterInvalidPassword() {
        LoginPage loginPage = new LoginPage();
        loginPage.invalidAuthInfo(DataHelper.getInvalidAuthInfo());
        loginPage.findErrorMessage("Ошибка! Неверно указан логин или пароль");
    }

    @Test
    void shouldBlockedPageAfterThreefoldInvalidPassword() {
        LoginPage loginPage = new LoginPage();
        loginPage.invalidThreefoldPassword(DataHelper.getInvalidAuthInfo());
        loginPage.findPopUp("Вы превысили лимит попыток ввода. Возможность ввода возобновится через 1 минуту");
    }

    @Test
    void shouldShowErrorAfterInvalidCode() {
        LoginPage loginPage = new LoginPage();
        VerificationPage verificationPage = loginPage.validAuthInfo(DataHelper.getValidOtherAuthInfo());
        verificationPage.invalidVerify(DataHelper.generateFakerCode());
        verificationPage.findErrorMessage("Ошибка! Неверно указан код! Попробуйте ещё раз.");
    }
}