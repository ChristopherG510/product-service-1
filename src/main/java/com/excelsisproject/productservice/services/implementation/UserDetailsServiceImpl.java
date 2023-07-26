package com.excelsisproject.productservice.services.implementation;

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
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        // Se recupera el usuario de la base de datos
        User userEntity = userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario" + login + "no existe"));

        // Se convierte el set de RoleEntity a un set de GrantedAuthorities
        Collection<? extends GrantedAuthority> authorities = userEntity.getRoles().stream()
                .map(role ->  new SimpleGrantedAuthority("ROLE_".concat(role.getName().name())))
                .collect(Collectors.toSet());

        // Se retorna un User de la clase User de spring
        return new org.springframework.security.core.userdetails.User(userEntity.getLogin(), userEntity.getPassword(),
                true,true,true,true,authorities
        );
    }
}
