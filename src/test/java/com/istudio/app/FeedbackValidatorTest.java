package com.istudio.app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackValidatorTest {

    @Test
    void validNameShouldPass() {
        assertTrue(FeedbackValidator.isValidName("Dinesh"));
    }

    @Test
    void emptyNameShouldFail() {
        assertFalse(FeedbackValidator.isValidName(""));
    }

    @Test
    void validEmailShouldPass() {
        assertTrue(FeedbackValidator.isValidEmail("dinesh@example.com"));
    }

    @Test
    void invalidEmailShouldFail() {
        assertFalse(FeedbackValidator.isValidEmail("dinesh"));
    }

    @Test
    void validMessageShouldPass() {
        assertTrue(FeedbackValidator.isValidMessage("Good application"));
    }
}
