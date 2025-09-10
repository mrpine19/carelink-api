package br.com.healthtech.imrea.usuario.service;

import br.com.healthtech.imrea.usuario.domain.Usuario;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsuarioService {

    public Usuario buscarUsuarioTeste(){
        return Usuario.findById(1);
    }
}
