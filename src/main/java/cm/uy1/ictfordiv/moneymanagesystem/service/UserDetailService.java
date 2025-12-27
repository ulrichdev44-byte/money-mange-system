package cm.uy1.ictfordiv.moneymanagesystem.service;

import cm.uy1.ictfordiv.moneymanagesystem.entity.ProfileEntity;
import cm.uy1.ictfordiv.moneymanagesystem.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;

@Service
@Transactional
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    private final ProfileRepository profileRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

      ProfileEntity existingProfile = profileRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found"+email));

        return User
                .builder()
                .username(existingProfile.getEmail())
                .password(existingProfile.getPassword())
                .authorities(Collections.emptyList())
                .build();
    }
}
