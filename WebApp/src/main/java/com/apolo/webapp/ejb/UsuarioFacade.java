package com.apolo.webapp.ejb;

import com.apolo.webapp.model.Usuario;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author raybm
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> implements UsuarioFacadeLocal {

    @PersistenceContext(unitName = "RastreadorPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }
    
    @Override
    public boolean existeUsuario(Usuario u){
        Usuario usuario = null;
        String consulta;
        try {
            consulta = "From Usuario u WHERE u.email = ?1";  
            Query query = em.createQuery(consulta);
            query.setParameter(1, u.getEmail());
            List<Usuario> lista = query.getResultList();
            if(!lista.isEmpty()){
                usuario = lista.get(0);
            }
        } catch (Exception e) {
              throw e;
        }
        return usuario != null;
    }
    
    @Override
    public Usuario iniciarSessao(Usuario us){
        Usuario usuario = null;
        String consulta;
        try {
            consulta = "From Usuario u WHERE u.email = ?1 and u.senha = ?2";  
            Query query = em.createQuery(consulta);
            query.setParameter(1, us.getEmail());
            query.setParameter(2, us.getSenha());

            List<Usuario> lista = query.getResultList();
            if(!lista.isEmpty()){
                usuario = lista.get(0);
            }
        } catch (Exception e) {
            throw e;
        }
        return usuario;
    }

    @Override
    public Usuario findEmail(Usuario us) {
    Usuario usuario = null;
        String consulta;
        try {
            consulta = "From Usuario u WHERE u.email = ?1";  
            Query query = em.createQuery(consulta);
            query.setParameter(1, us.getEmail());

            List<Usuario> lista = query.getResultList();
            if(!lista.isEmpty()){
                usuario = lista.get(0);
            }
        } catch (Exception e) {
            throw e;
        }
        return usuario;    
    }
    
}
