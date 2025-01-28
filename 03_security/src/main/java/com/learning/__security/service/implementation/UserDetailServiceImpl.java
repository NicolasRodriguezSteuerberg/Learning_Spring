package com.learning.__security.service.implementation;

import com.learning.__security.persistence.entities.UserEntity;
import com.learning.__security.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       UserEntity userEntity = userRepository.findUserEntityByUsername(username).orElseThrow(
               () -> new UsernameNotFoundException("El usuario " + username + " no existe.")
       );
        // añadimos los roles permitidos
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        // cogemos los roles y los convertimos en GrantedAuthority
        userEntity.getRoles()
                .forEach(role -> authorityList.add(
                        // spring diferencia los roles con ROLE_ por eso se hace esto
                        new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))
                )
        );

        // agregamos los permisos
        userEntity.getRoles() // tenemos una lista que va a tener un listado
               .stream() // recorremos cada uno de los roles
               // cogemos los permisos que están dentro de los roles
               .flatMap(
                       role -> role.getPermissionList().stream()
               )
               .forEach(
                       permission -> authorityList.add(
                               new SimpleGrantedAuthority(permission.getName())
                       )
               );
        return new User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.isEnabled(),
                userEntity.isAccountNoExpired(),
                userEntity.isCredentialNoExpired(),
                userEntity.isAccountNoLocked(),
                authorityList
        );
    }
}
