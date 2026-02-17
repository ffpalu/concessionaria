package com.ffpalu.concessionaria.entity;

import com.ffpalu.concessionaria.entity.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@With
public class Credential {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@NotNull(message = "Username cannot be null")
	@Size(min = 4, message = "Username too short")
	@Column(unique = true, nullable = false)
	private String username;

	@NotNull
	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	private Role role;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;


}
