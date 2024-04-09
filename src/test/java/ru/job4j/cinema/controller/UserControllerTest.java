package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserService userService;

    private UserController userController;

    @BeforeEach
    public void initServices() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    public void whenRequestUserRegistrationPageThenGetRegistrationPage() {
        var view = userController.getRegistrationPage();

        assertThat(view).isEqualTo("users/register");
    }

    @Test
    public void whenRequestLoginPageThenGetLoginPage() {
        var view = userController.getLoginPage();

        assertThat(view).isEqualTo("users/login");
    }

    @Test
    public void whenPostRegistrationUserInfoThenSameDataAndRedirect() {
        var user = new User("ivan", "ya@ya.ru",  "12345");
        var userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        when(userService.save(userArgumentCaptor.capture())).thenReturn(Optional.of(user));

        var model = new ConcurrentModel();
        var httpServletRequest = mock(HttpServletRequest.class);
        var view = userController.register(user, model, httpServletRequest);
        var actualUser = userArgumentCaptor.getValue();

        assertThat(view).isEqualTo("users/login");
        assertThat(actualUser).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    public void whenPostRegistrationUserInfoThenRegistrationError() {
        var expectedMessage = "Пользователь с таким email уже зарегистрирован";

        when(userService.save(any())).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var httpServletRequest = mock(HttpServletRequest.class);
        var view = userController.register(new User(), model, httpServletRequest);
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("/users/register");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void whenLogoutThenRedirect() {
        var httpSession = mock(HttpSession.class);

        var view = userController.logout(httpSession);

        assertThat(view).isEqualTo("redirect:/users/login");
    }

    @Test
    public void whenLoginUserThenAccessIsApprovedAndRedirect() {
        var user = new User("ivan", "ya@ya.ru", "12345");
        var emailArgumentCaptor = ArgumentCaptor.forClass(String.class);
        var passwordArgumentCaptor = ArgumentCaptor.forClass(String.class);

        when(userService.findByEmailAndPassword(
                emailArgumentCaptor.capture(),
                passwordArgumentCaptor.capture()
        )).thenReturn(Optional.of(user));


        var model = new ConcurrentModel();
        var httpServletRequest = mock(HttpServletRequest.class);

        when(httpServletRequest.getSession()).thenReturn(new MockHttpSession());

        var view = userController.loginUser(user, model, httpServletRequest);
        var actualEmail = emailArgumentCaptor.getValue();
        var actualPassword = passwordArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/films");
        assertThat(actualEmail).isEqualTo(user.getEmail());
        assertThat(actualPassword).isEqualTo(user.getPassword());
    }

    @Test
    public void whenLoginUserThenLoginErrorMessage() {
        var expectedErrorMessage = "Почта или пароль введены неверно";

        when(userService.findByEmailAndPassword(any(), any())).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var httpServletRequest = mock(HttpServletRequest.class);
        var view = userController.loginUser(new User(), model, httpServletRequest);
        var actualErrorMessage = model.getAttribute("error");

        assertThat(view).isEqualTo("users/login");
        assertThat(actualErrorMessage).isEqualTo(expectedErrorMessage);
    }
}
