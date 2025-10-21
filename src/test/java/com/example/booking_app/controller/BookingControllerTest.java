package com.example.booking_app.controller;

import com.example.booking_app.dto.booking.BookingDto;
import com.example.booking_app.dto.booking.BookingRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class BookingControllerTest {

    private static MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
        @Autowired WebApplicationContext wac,
        @Autowired DataSource dataSource
    ) throws Exception {
        mockMvc = webAppContextSetup(wac)
            .apply(springSecurity())
            .build();

        teardown(dataSource);

        try (Connection c = dataSource.getConnection()) {
            c.setAutoCommit(true);
            ScriptUtils.executeSqlScript(c, new ClassPathResource("database/booking/insert-users.sql"));
            ScriptUtils.executeSqlScript(c, new ClassPathResource("database/accommodation/insert-three-accommodation.sql"));
            ScriptUtils.executeSqlScript(c, new ClassPathResource("database/booking/insert-bookings.sql"));
        }
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    static void teardown(DataSource dataSource) {
        try (Connection c = dataSource.getConnection()) {
            c.setAutoCommit(true);
            ScriptUtils.executeSqlScript(c, new ClassPathResource("database/booking/delete-bookings.sql"));
            ScriptUtils.executeSqlScript(c, new ClassPathResource("database/accommodation/delete-accommodation.sql"));
            ScriptUtils.executeSqlScript(c, new ClassPathResource("database/booking/delete-users.sql"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("POST /bookings — створення бронювання (CUSTOMER)")
    @WithMockUser(username = "customer@example.com", roles = "CUSTOMER")
    void createBooking_Valid_ReturnsOkOrCreated() throws Exception {
        BookingRequestDto req = new BookingRequestDto();
        req.setAccommodationId(1L);
        req.setCheckInDate(LocalDate.of(2025, 12, 1));
        req.setCheckOutDate(LocalDate.of(2025, 12, 3));

        String json = objectMapper.writeValueAsString(req);

        MvcResult res = mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andReturn();

        BookingDto dto = objectMapper.readValue(res.getResponse().getContentAsString(), BookingDto.class);
        assertThat(dto.getAccommodationId()).isEqualTo(1L);
        assertThat(dto.getCheckInDate()).isEqualTo(req.getCheckInDate());
        assertThat(dto.getCheckOutDate()).isEqualTo(req.getCheckOutDate());
    }

    @Test
    @DisplayName("GET /bookings — менеджер фільтрує за userId/status")
    @WithMockUser(username = "manager@example.com", roles = "MANAGER")
    void getBookings_AsManager_ReturnsOk() throws Exception {
        mockMvc.perform(get("/bookings")
                .param("userId", "1001")
                .param("status", "PENDING")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /bookings/my — поточні бронювання користувача (CUSTOMER)")
    @WithMockUser(username = "customer@example.com", roles = "CUSTOMER")
    void getMyBookings_ReturnsOk() throws Exception {
        mockMvc.perform(get("/bookings/my"))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /bookings/{id} — отримати бронювання (CUSTOMER/MANAGER)")
    @WithMockUser(username = "customer@example.com", roles = "CUSTOMER")
    void getBookingById_Valid_ReturnsOk() throws Exception {
        mockMvc.perform(get("/bookings/{id}", 2001L))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /bookings/{id} — оновити бронювання (CUSTOMER)")
    @WithMockUser(username = "customer@example.com", roles = "CUSTOMER")
    void updateBooking_Valid_ReturnsOk() throws Exception {
        BookingRequestDto req = new BookingRequestDto();
        req.setAccommodationId(1L);
        req.setCheckInDate(LocalDate.of(2025, 12, 10));
        req.setCheckOutDate(LocalDate.of(2025, 12, 12));

        String json = objectMapper.writeValueAsString(req);

        MvcResult res = mockMvc.perform(put("/bookings/{id}", 2001L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andReturn();

        BookingDto dto = objectMapper.readValue(res.getResponse().getContentAsString(), BookingDto.class);
        assertThat(dto.getCheckInDate()).isEqualTo(req.getCheckInDate());
        assertThat(dto.getCheckOutDate()).isEqualTo(req.getCheckOutDate());
    }

    @Test
    @DisplayName("PATCH /bookings/{id} — часткове оновлення (CUSTOMER)")
    @WithMockUser(username = "customer@example.com", roles = "CUSTOMER")
    void partiallyUpdateBooking_Valid_ReturnsOk() throws Exception {
        BookingRequestDto req = new BookingRequestDto();
        req.setAccommodationId(1L);
        req.setCheckInDate(LocalDate.of(2025, 12, 15));
        req.setCheckOutDate(LocalDate.of(2025, 12, 16));

        String json = objectMapper.writeValueAsString(req);

        mockMvc.perform(patch("/bookings/{id}", 2001L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /bookings/{id} — скасувати (CUSTOMER) -> 204, далі 404")
    @WithMockUser(username = "customer@example.com", roles = "CUSTOMER")
    void cancelBooking_ReturnsNoContent_ThenNotFound() throws Exception {
        mockMvc.perform(delete("/bookings/{id}", 2002L))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/bookings/{id}", 2002L))
            .andExpect(status().isNotFound());
    }
}
