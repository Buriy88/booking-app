package com.example.booking_app.controller;

import static com.example.booking_app.util.AccommodationTestConstants.INVALID_ID;
import static com.example.booking_app.util.AccommodationTestConstants.VALID_ID;
import static com.example.booking_app.util.AccommodationTestUtil.createAccommodationRequest;
import static com.example.booking_app.util.AccommodationTestUtil.createInvalidAccommodationRequestDto;
import static com.example.booking_app.util.AccommodationTestUtil.createKyivApartment;
import static com.example.booking_app.util.AccommodationTestUtil.createUpdateAccommodationDto;
import static com.example.booking_app.util.AccommodationTestUtil.getAllTestAccommodations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.booking_app.dto.AccommodationDto;
import com.example.booking_app.dto.CreateAccommodationRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class AccommodationControllerTest {

    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
        @Autowired WebApplicationContext webApplicationContext,
        @Autowired DataSource dataSource
    ) throws Exception {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            //.apply(springSecurity())
            .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                connection,
                new ClassPathResource("database/accommodation/insert-three-accommodation.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                connection,
                new ClassPathResource("database/accommodation/delete-accommodation.sql")
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("POST \"/accommodations\" - \"Create a new accommodation\"")
    void Accommodation_ValidRequest() throws Exception {
        CreateAccommodationRequestDto createAccommodationRequestDto = createAccommodationRequest();

        String json = objectMapper.writeValueAsString(createAccommodationRequestDto);

        MvcResult result = mockMvc.perform(
                post("/api/accommodations")
                    .content(json)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andReturn();
        AccommodationDto expected = new AccommodationDto()
            .setType(createAccommodationRequestDto.getType())
            .setLocation(createAccommodationRequestDto.getLocation())
            .setSize(createAccommodationRequestDto.getSize())
            .setAmenities(createAccommodationRequestDto.getAmenities())
            .setDailyRate(createAccommodationRequestDto.getDailyRate())
            .setAvailability(createAccommodationRequestDto.getAvailability());
        AccommodationDto actual = objectMapper
            .readValue(
                result.getResponse().getContentAsString(), AccommodationDto.class);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("GET api/accommodations/{id} - Get accommodation by ID")
        //@WithMockUser(username = "admin", roles = "ADMIN")
    void getAccommodationById_ValidId_ReturnsBook() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/accommodations/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        AccommodationDto actual = objectMapper
            .readValue(result.getResponse().getContentAsString(), AccommodationDto.class);
        AccommodationDto expected = createKyivApartment();

        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("PUT /api/accommodations/{id} - Update Accommodation")
        // @WithMockUser(username = "admin", roles = "ADMIN")
    void updateBook_ValidRequest() throws Exception {
        CreateAccommodationRequestDto updateDto = createUpdateAccommodationDto();

        String json = objectMapper.writeValueAsString(updateDto);

        MvcResult result = mockMvc.perform(
                put("/api/accommodations/{id}", 2L)
                    .content(json)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
        AccommodationDto expected = new AccommodationDto()
            .setType(updateDto.getType())
            .setLocation(updateDto.getLocation())
            .setSize(updateDto.getSize())
            .setAmenities(updateDto.getAmenities())
            .setDailyRate(updateDto.getDailyRate())
            .setAvailability(updateDto.getAvailability());

        AccommodationDto actual = objectMapper
            .readValue(result.getResponse().getContentAsString(), AccommodationDto.class);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Get all Accommodations")
        // @WithMockUser(username = "admin", roles = "ADMIN")
    void getAllAccommodations_ValidRequest() throws Exception {
        List<AccommodationDto> expected = getAllTestAccommodations();

        MvcResult result = mockMvc.perform(get("/api/accommodations")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        AccommodationDto[] actual = objectMapper.readValue(
            responseJson,
            AccommodationDto[].class
        );

        assertEquals(3, actual.length);
        assertEquals(expected, Arrays.stream(actual).toList());

    }

    @Test
    @DisplayName("DELETE api/accommodations/{id} - Delete book")
        //@WithMockUser(username = "admin", roles = "ADMIN")
    void deleteAccommodations_ValidId_RemovesAccommodations() throws Exception {
        mockMvc.perform(delete("/api/accommodations/{id}", VALID_ID))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/accommodations/{id}", VALID_ID))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /books - Invalid request (missing required fields)")
        //@WithMockUser(username = "admin", roles = "ADMIN")
    void createAccommodations_InvalidRequest_ReturnsBadRequest() throws Exception {
        CreateAccommodationRequestDto invalidBook = createInvalidAccommodationRequestDto();

        String json = objectMapper.writeValueAsString(invalidBook);

        mockMvc.perform(post("/api/accommodations")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET api/accommodations/{id} - Non-existing accommodations")
        //@WithMockUser(username = "admin", roles = "ADMIN")
    void getAccommodationsById_NotFound() throws Exception {
        mockMvc.perform(get("/api/accommodations/{id}", INVALID_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT api/accommodations/{id} - Invalid update request")
        //@WithMockUser(username = "admin", roles = "ADMIN")
    void updateBook_InvalidRequest_ReturnsBadRequest() throws Exception {
        CreateAccommodationRequestDto invalidUpdate = createInvalidAccommodationRequestDto();

        String json = objectMapper.writeValueAsString(invalidUpdate);

        mockMvc.perform(put("/api/accommodations/{id}", VALID_ID)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE api/accommodations/{id} - Non-existing accommodations")
        //@WithMockUser(username = "admin", roles = "ADMIN")
    void deleteBook_NotFound() throws Exception {
        mockMvc.perform(delete("/api/accommodations/{id}", INVALID_ID))
            .andExpect(status().isNotFound());
    }


}