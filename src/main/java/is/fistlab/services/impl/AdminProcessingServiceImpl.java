package is.fistlab.services.impl;

import is.fistlab.database.entities.PotentialAdmin;
import is.fistlab.database.entities.User;
import is.fistlab.database.enums.UserRole;
import is.fistlab.database.repositories.PotentialAdminRepository;
import is.fistlab.database.repositories.UserRepository;
import is.fistlab.services.AdminProcessingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class AdminProcessingServiceImpl implements AdminProcessingService {
    private final UserRepository userRepository;
    private final PotentialAdminRepository adminRepository;

    @Override
    public boolean isAnyAdminExist() {
        return !userRepository.findAllByRole(UserRole.ROLE_ADMIN).isEmpty();
    }

    public List<PotentialAdmin> getAllPotentialAdmins() {
        return adminRepository.findAll();
    }

    @Override
    public Page<PotentialAdmin> getPotentialAdmins(final Pageable pageable) {
        return adminRepository.findAll(pageable);
    }

    @Override
    public void addUserToWaitingList(final User user) {
        PotentialAdmin admin = new PotentialAdmin();
        admin.setUser(user);
        admin.setWantedRole(UserRole.ROLE_ADMIN);
        adminRepository.save(admin);
    }

    @Override
    public void removeUserFromWaitingList(final User user) {
        assert user != null;
        adminRepository.removeByUser(user);
        log.info("user: {} deleted", user);
    }

    @Transactional
    @Override
    public void rejectUserForAdminRole(final User user) {
        assert user != null;
        adminRepository.removeByUser(user);
        log.info("user: {} rejected admin role", user);
    }

    /**
     * @param id - ид в очереди потенциальных админов, НЕ ИД ПОЛЬЗОВАТЕЛЯ
     */
    @Override
    @Transactional
    public void giveAdminRoleToUser(final Long id, final User user) {
        var potentialAdmin = adminRepository.findById(id);

        if (potentialAdmin.isEmpty()) {
            log.error("Potential admin with id {} does not exist", id);
            throw new RuntimeException("Potential admin with id " + id + " does not exist");
        }

        PotentialAdmin admin = potentialAdmin.get();
        var wantAdminRole = admin.getUser();
        var userFromRepThatWantedAdminRole = userRepository.findByUsername(wantAdminRole.getUsername());
        userFromRepThatWantedAdminRole
                .setRole(UserRole.ROLE_ADMIN);

        System.out.println(userFromRepThatWantedAdminRole);
        System.out.println(admin);
        userRepository.save(userFromRepThatWantedAdminRole);
        removeUserFromWaitingList(user);
    }

}
