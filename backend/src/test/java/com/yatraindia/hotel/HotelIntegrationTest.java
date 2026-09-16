package com.yatraindia.hotel;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class HotelIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllHotelsShouldSucceed() throws Exception {
        mockMvc.perform(get("/api/hotels"))
                .andExpect(status().isOk());
    }

    @Test
    void getHotelByIdShouldReturnCorrectHotel() throws Exception {
        mockMvc.perform(get("/api/hotels/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name")
                        .value("Grand Hyderabad Hotel"));
    }

    @Test
    void getHotelBySlugShouldReturnCorrectHotel() throws Exception {
        mockMvc.perform(
                get("/api/hotels/slug/grand-hyderabad-hotel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name")
                        .value("Grand Hyderabad Hotel"));
    }

    @Test
    void getHotelsByCityShouldSucceed() throws Exception {
        mockMvc.perform(get("/api/hotels/city/Hyderabad"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].city")
                        .value("Hyderabad"));
    }

    @Test
    void getHotelsByStateShouldSucceed() throws Exception {
        mockMvc.perform(get("/api/hotels/state/Telangana"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].state")
                        .value("Telangana"));
    }

    @Test
    void searchHotelsShouldSucceed() throws Exception {
        mockMvc.perform(get("/api/hotels/search")
                .param("name", "Grand"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name")
                        .value("Grand Hyderabad Hotel"));
    }

    @Test
    void invalidHotelShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/hotels/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status")
                        .value(404));
    }
}