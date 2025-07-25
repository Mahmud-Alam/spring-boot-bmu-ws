package com.mahmudalam.bmu_ws.security.userdetails;

import com.mahmudalam.bmu_ws.model.User;
import com.mahmudalam.bmu_ws.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            System.out.println("User Not Found!");
            throw new UsernameNotFoundException("User Not Found!");
        }

        return new CustomUserDetails(user);
    }
}
