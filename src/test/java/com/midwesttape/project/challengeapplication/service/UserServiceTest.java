package com.midwesttape.project.challengeapplication.service;

import com.midwesttape.project.challengeapplication.model.User;
import com.midwesttape.project.challengeapplication.model.UserNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final Long USER_ID = 1234L;

    @Mock
    private JdbcTemplate template;

    private UserService userService;

    @BeforeEach
    public void beforeEach() {
        userService = new UserService(template);
    }

    @Test
    public void should_get_user() throws UserNotFoundException {
        final User user = new User();

        when(template.queryForObject(anyString(), isA(BeanPropertyRowMapper.class), eq(USER_ID))).thenReturn(user);

        final User resultUser = userService.user(USER_ID);

        assertEquals(user, resultUser);
    }

    @Test
    public void user_not_found() throws UserNotFoundException {
        final User user = new User();

        when(template.queryForObject(anyString(), isA(BeanPropertyRowMapper.class), eq(USER_ID))).thenThrow(new EmptyResultDataAccessException(1));

        try {
            final User resultUser = userService.user(USER_ID);
            fail();
        } catch (final UserNotFoundException e) {
            assertEquals(USER_ID, e.getUserId());
        }
    }

}