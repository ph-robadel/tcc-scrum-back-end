package br.ufes.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.ufes.enums.PerfilUsuarioEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USUARIO")
public class Usuario implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_USUARIO")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long Id;

	private String nomeCompleto;

	private String nomeUsuario;

	private String senha;

	private String email;

	private Boolean ativo;

	@Enumerated(EnumType.STRING)
	private PerfilUsuarioEnum perfil;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		for (PerfilUsuarioEnum perfilEnum : PerfilUsuarioEnum.values()) {
			if (perfilEnum.equals(this.perfil)) {
				return List.of(new SimpleGrantedAuthority(perfilEnum.name()));
			}
		}
		return null;
	}

	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		return nomeUsuario;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return Boolean.TRUE.equals(this.ativo);
	}

}
