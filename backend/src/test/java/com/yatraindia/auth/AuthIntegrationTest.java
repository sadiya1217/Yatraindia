package com.yatraindia.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void registrationShouldSucceed() throws Exception {

        String email = "test" + System.currentTimeMillis()
                + "@yatraindia.com";

        String request = """
                {
                    "fullName": "Automated Test User",
                    "email": "%s",
                    "phone": null,
                    "password": "TestPassword123!"
                }
                """.formatted(email);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message")
                        .value("User registered successfully"))
                .andExpect(jsonPath("$.email")
                        .value(email))
                .andExpect(jsonPath("$.role")
                        .value("CUSTOMER"));
    }


    @Test
    void loginShouldReturnJwtToken() throws Exception {

        String email = "login" + System.currentTimeMillis()
                + "@yatraindia.com";

        String registerRequest = """
                {
                    "fullName": "Login Test User",
                    "email": "%s",
                    "phone": null,
                    "password": "TestPassword123!"
                }
                """.formatted(email);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerRequest))
                .andExpect(status().isCreated());

        String loginRequest = """
                {
                    "email": "%s",
                    "password": "TestPassword123!"
                }
                """.formatted(email);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Login successful"))
                .andExpect(jsonPath("$.email")
                        .value(email))
                .andExpect(jsonPath("$.role")
                        .value("CUSTOMER"))
                .andExpect(jsonPath("$.token")
                        .isNotEmpty());
    }


    @Test
    void protectedApiShouldRejectRequestWithoutJwt() throws Exception {

        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isForbidden());
    }


    @Test
    void protectedApiShouldAcceptValidJwt() throws Exception {

        String email = "jwt" + System.currentTimeMillis()
                + "@yatraindia.com";

        String registerRequest = """
                {
                    "fullName": "JWT Integration User",
                    "email": "%s",
                    "phone": null,
                    "password": "TestPassword123!"
                }
                """.formatted(email);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerRequest))
                .andExpect(status().isCreated());

        String loginRequest = """
                {
                    "email": "%s",
                    "password": "TestPassword123!"
                }
                """.formatted(email);

        String loginResponse = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode responseJson =
                objectMapper.readTree(loginResponse);

        String token =
                responseJson.get("token").asText();

        mockMvc.perform(get("/api/auth/me")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Authenticated user"))
                .andExpect(jsonPath("$.email")
                        .value(email))
                .andExpect(jsonPath("$.status")
                        .value("AUTHENTICATED"));
    }


    @Test
    void getMyProfileShouldReturnAuthenticatedUser() throws Exception {

        String email = "profile" + System.currentTimeMillis()
                + "@yatraindia.com";

        String registerRequest = """
                {
                    "fullName": "Profile Test User",
                    "email": "%s",
                    "phone": null,
                    "password": "TestPassword123!"
                }
                """.formatted(email);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerRequest))
                .andExpect(status().isCreated());

        String loginRequest = """
                {
                    "email": "%s",
                    "password": "TestPassword123!"
                }
                """.formatted(email);

        String loginResponse = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode responseJson =
                objectMapper.readTree(loginResponse);

        String token =
                responseJson.get("token").asText();

        mockMvc.perform(get("/api/users/me")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email")
                        .value(email))
                .andExpect(jsonPath("$.fullName")
                        .value("Profile Test User"))
                .andExpect(jsonPath("$.role")
                        .value("CUSTOMER"))
                .andExpect(jsonPath("$.status")
                        .value("ACTIVE"));
    }


    @Test
    void getMyProfileShouldRejectRequestWithoutJwt() throws Exception {

        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isForbidden());
    }


    @Test
    void updateMyProfileShouldPersistChanges() throws Exception {

        String email = "update" + System.currentTimeMillis()
                + "@yatraindia.com";

        String registerRequest = """
                {
                    "fullName": "Original Name",
                    "email": "%s",
                    "phone": null,
                    "password": "TestPassword123!"
                }
                """.formatted(email);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerRequest))
                .andExpect(status().isCreated());

        String loginRequest = """
                {
                    "email": "%s",
                    "password": "TestPassword123!"
                }
                """.formatted(email);

        String loginResponse = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode responseJson =
                objectMapper.readTree(loginResponse);

        String token =
                responseJson.get("token").asText();

        /*
         * Generate a unique phone number so that this test
         * never conflicts with an existing database record.
         */
        String updatedPhone =
                "8" + (System.currentTimeMillis() % 100000000L);

        String updateRequest = """
                {
                    "fullName": "Updated Name",
                    "phone": "%s"
                }
                """.formatted(updatedPhone);

        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .put("/api/users/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Profile updated successfully"))
                .andExpect(jsonPath("$.fullName")
                        .value("Updated Name"))
                .andExpect(jsonPath("$.phone")
                        .value(updatedPhone));

        /*
         * Fetch the profile again to prove that the
         * updated data was actually persisted.
         */
        mockMvc.perform(get("/api/users/me")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName")
                        .value("Updated Name"))
                .andExpect(jsonPath("$.phone")
                        .value(updatedPhone));
    }
}