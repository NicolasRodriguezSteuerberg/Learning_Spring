package com.learning.__security.service.implementation;

import com.learning.__security.persistence.entities.RoleEntity;
import com.learning.__security.persistence.entities.UserEntity;
import com.learning.__security.persistence.repositories.RoleRepository;
import com.learning.__security.persistence.repositories.UserRepository;
import com.learning.__security.presentation.dto.request.AuthLoginRequest;
import com.learning.__security.presentation.dto.request.AuthRegisterRequest;
import com.learning.__security.presentation.dto.response.AuthResponse;
import com.learning.__security.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;

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

    public AuthResponse loginUser(AuthLoginRequest authLoginRequest) {
        String username = authLoginRequest.username();
        String password = authLoginRequest.password();

        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtils.createToken(authentication);

        return new AuthResponse(username, "user logged succesfully", accessToken, true);
    }

    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username or password");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }

    public AuthResponse createUser(AuthRegisterRequest authRegisterRequest) {
        String username = authRegisterRequest.username();
        String password = authRegisterRequest.password();

        List<String> rolesRequest = authRegisterRequest.roleRequest().roleListName();
        Set<RoleEntity> roleEntitySet = roleRepository.findRoleEntitiesByRoleEnumIn(rolesRequest)
                .stream()
                .collect(Collectors.toSet());

        if (roleEntitySet.isEmpty()) {
            throw new IllegalArgumentException("Specified roles dont exist");
        }
        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(roleEntitySet)
                .isEnabled(true)
                .accountNoExpired(true)
                .accountNoLocked(true)
                .credentialNoExpired(true)
                .build();

        UserEntity userCreated = userRepository.save(userEntity);

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        // añadimos los roles a la lista
        userCreated.getRoles().forEach(role ->
                authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name())))
        );

        // añadimos los permisos
        userCreated.getRoles()
                .stream()
                .flatMap(role -> role.getPermissionList().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userCreated.getUsername(),
                userCreated.getPassword(),
                authorityList
        );

        String accessToken = jwtUtils.createToken(authentication);

        return new AuthResponse(
                userCreated.getUsername(),
                "user created successfully",
                accessToken,
                true
        );
    }
}
