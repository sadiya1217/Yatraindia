package com.yatraindia.booking;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class BookingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /*
     * Registers a customer and returns the customer's JWT.
     */
    private String registerAndLogin(
            String email,
            String password) throws Exception {

        String registerRequest = """
                {
                    "fullName": "Booking Integration Test User",
                    "email": "%s",
                    "phone": null,
                    "password": "%s"
                }
                """.formatted(email, password);

        mockMvc.perform(
                post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequest))
                .andExpect(status().isCreated());

        String loginRequest = """
                {
                    "email": "%s",
                    "password": "%s"
                }
                """.formatted(email, password);

        String loginResponse = mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode responseJson =
                objectMapper.readTree(loginResponse);

        return responseJson.get("token").asText();
    }

    /*
     * Creates an administrator test account.
     *
     * The role change is made directly in the test database.
     * Public registration continues to create CUSTOMER accounts.
     */
    private String registerAndLoginAsAdmin(
            String email,
            String password) throws Exception {

        registerAndLogin(email, password);

        int updatedRows = jdbcTemplate.update(
                "UPDATE users SET role = ? WHERE email = ?",
                "ADMIN",
                email);

        if (updatedRows != 1) {
            throw new IllegalStateException(
                    "Could not prepare administrator test account");
        }

        // Log in again so the JWT contains the ADMIN role.
        String loginRequest = """
                {
                    "email": "%s",
                    "password": "%s"
                }
                """.formatted(email, password);

        String loginResponse = mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode responseJson =
                objectMapper.readTree(loginResponse);

        return responseJson.get("token").asText();
    }

    /*
     * Gets the user's database ID from the login response.
     */
    private Long getUserIdFromLogin(
            String email,
            String password) throws Exception {

        String loginRequest = """
                {
                    "email": "%s",
                    "password": "%s"
                }
                """.formatted(email, password);

        String loginResponse = mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode responseJson =
                objectMapper.readTree(loginResponse);

        return responseJson.get("userId").asLong();
    }

    /*
     * Creates a CAB booking for the authenticated customer.
     * The server is responsible for assigning the user ID.
     */
    private Long createCabBooking(
            String token) throws Exception {

        String bookingReference =
                "TEST-CAB-" + UUID.randomUUID();

        String bookingRequest = """
                {
                    "bookingReference": "%s",
                    "bookingType": "CAB",
                    "serviceId": 1,
                    "checkIn": "2026-09-25T10:00:00",
                    "checkOut": null,
                    "totalAmount": 1500.00,
                    "status": "PENDING",
                    "pickupLocation": "Hyderabad",
                    "dropLocation": "Secunderabad",
                    "passengers": 2
                }
                """.formatted(bookingReference);

        String response = mockMvc.perform(
                post("/api/bookings")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingRequest))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode bookingJson =
                objectMapper.readTree(response);

        return bookingJson.get("id").asLong();
    }

    // --------------------------------------------------
    // Administrator booking-read tests
    // --------------------------------------------------

    @Test
    void getAllBookingsShouldSucceed() throws Exception {

        String email =
                "admin-all-" + UUID.randomUUID() + "@yatraindia.com";

        String token =
                registerAndLoginAsAdmin(email, "Password@123");

        mockMvc.perform(
                get("/api/bookings")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingByIdShouldSucceed() throws Exception {

        String email =
                "admin-id-" + UUID.randomUUID() + "@yatraindia.com";

        String token =
                registerAndLoginAsAdmin(email, "Password@123");

        mockMvc.perform(
                get("/api/bookings/7")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(7)));
    }

    @Test
    void getBookingByReferenceShouldSucceed() throws Exception {

        String email =
                "admin-reference-" + UUID.randomUUID()
                        + "@yatraindia.com";

        String token =
                registerAndLoginAsAdmin(email, "Password@123");

        String response = mockMvc.perform(
                get("/api/bookings/7")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode booking =
                objectMapper.readTree(response);

        String bookingReference =
                booking.get("bookingReference").asText();

        mockMvc.perform(
                get("/api/bookings/reference/" + bookingReference)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.bookingReference",
                        is(bookingReference)));
    }

    @Test
    void getBookingsByUserShouldSucceed() throws Exception {

        String email =
                "admin-user-" + UUID.randomUUID() + "@yatraindia.com";

        String token =
                registerAndLoginAsAdmin(email, "Password@123");

        mockMvc.perform(
                get("/api/bookings/user/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsByTypeShouldSucceed() throws Exception {

        String email =
                "admin-type-" + UUID.randomUUID() + "@yatraindia.com";

        String token =
                registerAndLoginAsAdmin(email, "Password@123");

        mockMvc.perform(
                get("/api/bookings/type/CAB")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsByStatusShouldSucceed() throws Exception {

        String email =
                "admin-status-" + UUID.randomUUID()
                        + "@yatraindia.com";

        String token =
                registerAndLoginAsAdmin(email, "Password@123");

        mockMvc.perform(
                get("/api/bookings/status/COMPLETED")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsByServiceShouldSucceed() throws Exception {

        String email =
                "admin-service-" + UUID.randomUUID()
                        + "@yatraindia.com";

        String token =
                registerAndLoginAsAdmin(email, "Password@123");

        mockMvc.perform(
                get("/api/bookings/service/1")
                        .header("Authorization", "Bearer " + token)
                        .param("bookingType", "CAB"))
                .andExpect(status().isOk());
    }

    // --------------------------------------------------
    // Booking creation tests
    // --------------------------------------------------



@Test
void cabBookingShouldUseDatabaseFareInsteadOfClientFare()
        throws Exception {

    String email = "fare-test-" + UUID.randomUUID()
            + "@yatraindia.com";

    String token = registerAndLogin(email, "Password@123");
    String bookingReference = "FARE-" + UUID.randomUUID();

    String requestBody = """
            {
                "bookingReference": "%s",
                "bookingType": "CAB",
                "serviceId": 1,
                "checkIn": "2026-11-25T10:00:00",
                "checkOut": null,
                "totalAmount": 1.00,
                "status": "PENDING",
                "pickupLocation": "Hyderabad",
                "dropLocation": "Secunderabad",
                "passengers": 2
            }
            """.formatted(bookingReference);

    mockMvc.perform(
            post("/api/bookings")
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.totalAmount").value(100.00))
            .andExpect(jsonPath("$.status").value("PENDING"))
            .andExpect(jsonPath("$.bookingType").value("CAB"));
}

    @Test
    void createBookingShouldSucceed() throws Exception {

        String email =
                "create-" + UUID.randomUUID() + "@yatraindia.com";

        String token =
                registerAndLogin(email, "Password@123");

        String bookingReference =
                "TEST-" + UUID.randomUUID();

        String requestBody = """
                {
                    "bookingReference": "%s",
                    "bookingType": "CAB",
                    "serviceId": 1,
                    "checkIn": "2026-09-25T10:00:00",
                    "checkOut": null,
                    "totalAmount": 1500.00,
                    "status": "PENDING",
                    "pickupLocation": "Hyderabad",
                    "dropLocation": "Secunderabad",
                    "passengers": 2
                }
                """.formatted(bookingReference);

        mockMvc.perform(
                post("/api/bookings")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath(
                        "$.bookingReference",
                        is(bookingReference)))
                .andExpect(jsonPath("$.status", is("PENDING")));
    }

    @Test
    void duplicateBookingReferenceShouldFail() throws Exception {

        String email =
                "duplicate-" + UUID.randomUUID() + "@yatraindia.com";

        String token =
                registerAndLogin(email, "Password@123");

        String bookingReference =
                "DUP-" + UUID.randomUUID();

        String requestBody = """
                {
                    "bookingReference": "%s",
                    "bookingType": "CAB",
                    "serviceId": 1,
                    "checkIn": "2026-09-25T10:00:00",
                    "checkOut": null,
                    "totalAmount": 1500.00,
                    "status": "PENDING",
                    "pickupLocation": "Hyderabad",
                    "dropLocation": "Secunderabad",
                    "passengers": 2
                }
                """.formatted(bookingReference);

        mockMvc.perform(
                post("/api/bookings")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        mockMvc.perform(
                post("/api/bookings")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    // --------------------------------------------------
    // My Bookings test
    // --------------------------------------------------

    @Test
    void getMyBookingsShouldReturnAuthenticatedUsersBookings()
            throws Exception {

        String email =
                "booking-test-" + UUID.randomUUID()
                        + "@yatraindia.com";

        String token =
                registerAndLogin(email, "Password@123");

        mockMvc.perform(
                get("/api/bookings/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // --------------------------------------------------
    // Booking security tests
    // --------------------------------------------------

    @Test
    void bookingListShouldRejectUnauthenticatedRequest()
            throws Exception {

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isForbidden());
    }

    @Test
    void bookingCreationShouldRejectUnauthenticatedRequest()
            throws Exception {

        mockMvc.perform(
                post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void customerShouldNotReadAdministrativeBookingList()
            throws Exception {

        String email =
                "customer-read-" + UUID.randomUUID()
                        + "@yatraindia.com";

        String token =
                registerAndLogin(email, "Password@123");

        mockMvc.perform(
                get("/api/bookings")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void customerShouldNotChangeBookingStatus()
            throws Exception {

        String email =
                "customer-status-" + UUID.randomUUID()
                        + "@yatraindia.com";

        String token =
                registerAndLogin(email, "Password@123");

        Long bookingId = createCabBooking(token);

        mockMvc.perform(
                patch("/api/bookings/" + bookingId + "/status")
                        .header("Authorization", "Bearer " + token)
                        .param("status", "CONFIRMED"))
                .andExpect(status().isForbidden());
    }

    @Test
    void customerShouldNotCreateBookingForAnotherUser()
            throws Exception {

        String customerEmail =
                "customer-owner-" + UUID.randomUUID()
                        + "@yatraindia.com";

        String otherEmail =
                "other-owner-" + UUID.randomUUID()
                        + "@yatraindia.com";

        String customerToken =
                registerAndLogin(customerEmail, "Password@123");

        registerAndLogin(otherEmail, "Password@123");

        Long otherUserId =
                getUserIdFromLogin(otherEmail, "Password@123");

        String requestBody = """
                {
                    "bookingReference": "%s",
                    "userId": %d,
                    "bookingType": "CAB",
                    "serviceId": 1,
                    "checkIn": "2026-09-25T10:00:00",
                    "checkOut": null,
                    "totalAmount": 1500.00,
                    "status": "PENDING",
                    "pickupLocation": "Hyderabad",
                    "dropLocation": "Secunderabad",
                    "passengers": 2
                }
                """.formatted(
                        "OWNER-" + UUID.randomUUID(),
                        otherUserId);

        mockMvc.perform(
                post("/api/bookings")
                        .header(
                                "Authorization",
                                "Bearer " + customerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    // --------------------------------------------------
    // Booking status-transition test
    // --------------------------------------------------

    @Test
    void completedBookingShouldRemainCompleted()
            throws Exception {

        String password = "Password@123";

        String customerEmail =
                "completed-customer-" + UUID.randomUUID()
                        + "@yatraindia.com";

        String customerToken =
                registerAndLogin(customerEmail, password);

        Long bookingId =
                createCabBooking(customerToken);

        String adminEmail =
                "completed-admin-" + UUID.randomUUID()
                        + "@yatraindia.com";

        String adminToken =
                registerAndLoginAsAdmin(adminEmail, password);

        // PENDING -> CONFIRMED
        mockMvc.perform(
                patch("/api/bookings/" + bookingId + "/status")
                        .header(
                                "Authorization",
                                "Bearer " + adminToken)
                        .param("status", "CONFIRMED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.status",
                        is("CONFIRMED")));

        // CONFIRMED -> COMPLETED
        mockMvc.perform(
                patch("/api/bookings/" + bookingId + "/status")
                        .header(
                                "Authorization",
                                "Bearer " + adminToken)
                        .param("status", "COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.status",
                        is("COMPLETED")));

        // Verify that the completed booking remains completed.
        mockMvc.perform(
                get("/api/bookings/" + bookingId)
                        .header(
                                "Authorization",
                                "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.status",
                        is("COMPLETED")));
    }
}