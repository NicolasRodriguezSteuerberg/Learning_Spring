package com.learning.__security;

import com.learning.__security.persistence.entities.PermissionEntity;
import com.learning.__security.persistence.entities.RoleEntity;
import com.learning.__security.persistence.entities.RoleEnum;
import com.learning.__security.persistence.entities.UserEntity;
import com.learning.__security.persistence.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository) {
		return args -> {
			// create permissions
			PermissionEntity createPermission = PermissionEntity.builder()
					.name("CREATE")
					.build();
			PermissionEntity readPermission = PermissionEntity.builder()
					.name("READ")
					.build();
			PermissionEntity updatePermission = PermissionEntity.builder()
					.name("UPDATE")
					.build();
			PermissionEntity deletePermission = PermissionEntity.builder()
					.name("DELETE")
					.build();
			PermissionEntity refactorPermission = PermissionEntity.builder()
					.name("REFACTOR")
					.build();

			// create roles
			RoleEntity roleAdmin = RoleEntity.builder()
					.roleEnum(RoleEnum.ADMIN)
					.permissionList(Set.of(createPermission, readPermission, updatePermission, deletePermission))
					.build();

			RoleEntity roleUser = RoleEntity.builder()
					.roleEnum(RoleEnum.USER)
					.permissionList(Set.of(createPermission, readPermission))
					.build();

			RoleEntity roleInvited = RoleEntity.builder()
					.roleEnum(RoleEnum.INVITED)
					.permissionList(Set.of(readPermission))
					.build();

			RoleEntity roleDeveloper = RoleEntity.builder()
					.roleEnum(RoleEnum.DEVELOPER)
					.permissionList(Set.of(createPermission, readPermission, updatePermission, deletePermission, refactorPermission))
					.build();

			// create users
			UserEntity userNico = UserEntity.builder()
					.username("nico")
					.password("$2a$10$xlt9AMmsw2Q2CLa/xk3uT.5KYL4saXRcCXfBO2qnIcXcAseGllmzW")
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleAdmin))
					.build();

			UserEntity userIrene = UserEntity.builder()
					.username("irene")
					.password("$2a$10$xlt9AMmsw2Q2CLa/xk3uT.5KYL4saXRcCXfBO2qnIcXcAseGllmzW")
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleDeveloper))
					.build();

			UserEntity userDaniel = UserEntity.builder()
					.username("daniel")
					.password("$2a$10$xlt9AMmsw2Q2CLa/xk3uT.5KYL4saXRcCXfBO2qnIcXcAseGllmzW")
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleUser))
					.build();

			UserEntity userYisus = UserEntity.builder()
					.username("yisus")
					.password("$2a$10$xlt9AMmsw2Q2CLa/xk3uT.5KYL4saXRcCXfBO2qnIcXcAseGllmzW")
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleInvited))
					.build();

			userRepository.saveAll(List.of(userNico, userYisus, userIrene, userDaniel));
		};
	}

}
