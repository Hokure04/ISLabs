package is.fistlab.controllers;

import is.fistlab.database.entities.PotentialAdmin;
import is.fistlab.database.entities.User;
import is.fistlab.services.AdminProcessingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@Slf4j
@RequestMapping("/api/v1/admin/console")
@AllArgsConstructor
public class AdminProcessingController {
    private final AdminProcessingService adminProcessingService;

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response<Page<PotentialAdmin>>> getAllPotentialAdmin(
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int size) {
        final Pageable pageable = PageRequest.of(page, size);
        Page<PotentialAdmin> personPage = adminProcessingService
                .getPotentialAdmins(pageable);
        return ResponseEntity.ok(
                new Response<>(personPage));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/approve/{id}")
    public ResponseEntity<Response<User>> approveAdminRole(
            @PathVariable final Long id,
            @RequestBody final PotentialAdmin potentialAdmin) {
        adminProcessingService.giveAdminRoleToUser(id, potentialAdmin.getUser());
        return ResponseEntity.ok(
                new Response<>("Пользователю " + potentialAdmin.getUser().getUsername() + " успешна выдана роль админа",
                        potentialAdmin.getUser()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/reject")
    public ResponseEntity<Response<User>> rejectAdminRole(@RequestBody final PotentialAdmin potentialAdmin) {
        adminProcessingService
                .rejectUserForAdminRole(potentialAdmin.getUser());
        return ResponseEntity.ok(new Response<>("Пользователь "
                        + potentialAdmin.getUser().getUsername()
                        + " успешно получил отказ"));
    }
}
