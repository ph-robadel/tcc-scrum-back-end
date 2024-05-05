package br.ufes.facade;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import br.ufes.domain.usuario.Usuario;
import br.ufes.domain.usuario.UsuarioDTO;
import br.ufes.repository.UsuarioRepository;

@Component
public class UsuarioFacade {
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	public void cadastrarUsuario(UsuarioDTO usuarioDTO) throws Exception {
		if (usuarioRepository.findByNomeUsuario(usuarioDTO.getNomeUsuario()) != null) {
			throw new RuntimeException("O nome de usuário já cadastrado");
		}

		String senhaCriptografada = new BCryptPasswordEncoder().encode(usuarioDTO.getSenha());
		var usuario = modelMapper.map(usuarioDTO, Usuario.class);
		usuario.setSenha(senhaCriptografada);
		
		usuarioRepository.save(usuario);
	}

}
