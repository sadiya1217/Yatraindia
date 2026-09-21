package com.yatraindia.booking;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class BookingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllBookingsShouldSucceed() throws Exception {
        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingByIdShouldSucceed() throws Exception {
        mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getBookingByUserShouldSucceed() throws Exception {
        mockMvc.perform(get("/api/bookings/user/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsByTypeShouldSucceed() throws Exception {
        mockMvc.perform(get("/api/bookings/type/CAB"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsByStatusShouldSucceed() throws Exception {
        mockMvc.perform(get("/api/bookings/status/CONFIRMED"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsByServiceShouldSucceed() throws Exception {
        mockMvc.perform(
                get("/api/bookings/service/1")
                        .param("bookingType", "CAB"))
                .andExpect(status().isOk());
    }

    @Test
    void invalidBookingTypeShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/bookings/type/FLIGHT"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void invalidBookingStatusShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/bookings/status/INVALID"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void invalidBookingIdShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/bookings/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void invalidBookingReferenceShouldReturnNotFound()
            throws Exception {

        mockMvc.perform(
                get("/api/bookings/reference/DOES-NOT-EXIST"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

  @Test
void completedBookingShouldNotBeCancelled() throws Exception {
    mockMvc.perform(
            patch("/api/bookings/7/status")
                    .param("status", "CANCELLED"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400));
}

@Test
void completedBookingShouldRemainCompleted() throws Exception {
    mockMvc.perform(
            patch("/api/bookings/7/status")
                    .param("status", "COMPLETED"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status")
                    .value("COMPLETED"));
}}