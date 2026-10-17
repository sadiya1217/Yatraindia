
package com.yatraindia.history;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yatraindia.auth.security.AuthenticatedUserService;

@RestController
@RequestMapping("/api/history")
public class TravelHistoryController {

    private final TravelHistoryService travelHistoryService;
    private final AuthenticatedUserService authenticatedUserService;

    public TravelHistoryController(
            TravelHistoryService travelHistoryService,
            AuthenticatedUserService authenticatedUserService) {
        this.travelHistoryService = travelHistoryService;
        this.authenticatedUserService = authenticatedUserService;
    }

    @GetMapping("/states")
    public ResponseEntity<List<String>> getVisitedStates() {

        Long userId = authenticatedUserService.getCurrentUserId();

        return ResponseEntity.ok(
                travelHistoryService.getVisitedStates(userId));
    }
}
