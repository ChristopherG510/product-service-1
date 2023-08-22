package com.excelsisproject.productservice.services;


import com.excelsisproject.productservice.entities.Roles;
import com.excelsisproject.productservice.entities.User;
import com.excelsisproject.productservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    //Método para obtener un set de autoridades por medio de un set de roles
    public Collection<GrantedAuthority> mapToAuthorities(Set<Roles> roles){
        return roles.stream().map(role-> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());
    }


    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Método para obtener un usuario con todos sus datos por medio de su login
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login).orElseThrow(()-> new UsernameNotFoundException("User not found"));
        Set<Roles> roles = new HashSet<>();
        for(Roles role : user.getRoles()){
            roles.add(role);
        }
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), mapToAuthorities(roles));
    }
}
