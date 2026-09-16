package com.yatraindia.destination;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class DestinationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllDestinationsShouldSucceed() throws Exception {

        mockMvc.perform(
                get("/api/destinations"))
                .andExpect(status().isOk());
    }

    @Test
    void getDestinationByIdShouldReturnCorrectDestination()
            throws Exception {

        mockMvc.perform(
                get("/api/destinations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Hyderabad"))
                .andExpect(jsonPath("$.state").value("Telangana"))
                .andExpect(jsonPath("$.country").value("India"));
    }

    @Test
    void getDestinationBySlugShouldReturnCorrectDestination()
            throws Exception {

        mockMvc.perform(
                get("/api/destinations/slug/hyderabad"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Hyderabad"))
                .andExpect(jsonPath("$.slug").value("hyderabad"))
                .andExpect(jsonPath("$.state").value("Telangana"))
                .andExpect(jsonPath("$.country").value("India"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void getDestinationsByStateShouldSucceed() throws Exception {

        mockMvc.perform(
                get("/api/destinations/state/Telangana"))
                .andExpect(status().isOk());
    }

    @Test
    void searchDestinationsShouldSucceed() throws Exception {

        mockMvc.perform(
                get("/api/destinations/search")
                        .param("name", "hyd"))
                .andExpect(status().isOk());
    }

    @Test
    void invalidDestinationShouldReturnNotFound() throws Exception {

        mockMvc.perform(
                get("/api/destinations/999999"))
                .andExpect(status().isNotFound());
    }
}