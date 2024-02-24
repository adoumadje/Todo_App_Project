package com.example.todoapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UtilsServiceTest {
    private UtilsService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UtilsService();
    }

    @Test
    void shouldGenerateInitials() {
        // given
        String fullname = "Fede Valverde";

        // when
        // then
        assertThat(underTest.generateInitials(fullname))
                .isEqualTo("FV");
    }

    @Test
    void shouldGenerateTodoColor() {
        // given
        Integer status1 = 20;
        Integer status2 = 40;
        Integer status3 = 60;
        Integer status4 = 80;

        // when
        // then
        assertThat(underTest.generateTodoColor(status1))
                .isEqualTo("#ff3d32");
        assertThat(underTest.generateTodoColor(status2))
                .isEqualTo("#f97316");
        assertThat(underTest.generateTodoColor(status3))
                .isEqualTo("#14b8a6");
        assertThat(underTest.generateTodoColor(status4))
                .isEqualTo("#22c55e");
    }
}