package com.apolo.webapp.ejb;

import com.apolo.webapp.model.Usuario;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author raybm
 */
@Local
public interface UsuarioFacadeLocal {

    void create(Usuario usuario);

    void edit(Usuario usuario);

    void remove(Usuario usuario);

    Usuario find(Object id);

    List<Usuario> findAll();

    List<Usuario> findRange(int[] range);

    int count();
    
    boolean existeUsuario(Usuario u);
    
    public Usuario iniciarSessao(Usuario us);
    
    public Usuario findEmail(Usuario us);
}
