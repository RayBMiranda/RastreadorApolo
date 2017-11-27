package com.apolo.webapp.controller;

import com.apolo.webapp.ejb.RastreadorFacadeLocal;
import com.apolo.webapp.ejb.UsuarioFacadeLocal;
import com.apolo.webapp.model.Pessoa;
import com.apolo.webapp.model.Rastreador;
import com.apolo.webapp.model.Usuario;
import com.apolo.webapp.util.Mensagens;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 *
 * @author raybm
 */
@ManagedBean(name = "UsuarioController")
@ViewScoped
public class UsuarioController implements Serializable{
    @EJB
    private UsuarioFacadeLocal usuarioEJB;
    @EJB 
    private RastreadorFacadeLocal rastreadorEJB;
    @Inject
    private Usuario usuario;
    @Inject
    private Pessoa pessoa;
    
    private List<Usuario> usuarios;
    
    @PostConstruct
    public void init(){
        usuarios = usuarioEJB.findAll();
    }
    
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
    
    public void editar(Usuario usuario){
        usuarioEJB.edit(usuario);
        Mensagens.exibirMensagem("Registro Modificado", false);
    }
    
    public void registrar(){
    try {
          this.usuario.setId(pessoa);
          if(usuarioEJB.existeUsuario(usuario))
          {
            Mensagens.exibirMensagem("Email já Cadastrado", true);
            return;
          }
          usuarioEJB.create(usuario);
          Mensagens.exibirMensagem("Usuário Registrado", false);
        } catch (Exception e) { 
          Mensagens.exibirMensagem("Erro ao Registrar Usuário", true);
        }
    }
    
    public void ler(Usuario usuarioSelecionado){
        this.usuario = usuarioSelecionado;
        this.usuario.getRastreadores();
  //      FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().put("usuarioSelecionado", usuarioSelecionado);
    }
    
    public void exibirDialogo(){
        RequestContext req = RequestContext.getCurrentInstance();
        req.execute("PF('wdialogo').show()");
    }
    
    public void ocultarDialogo(){
        RequestContext req = RequestContext.getCurrentInstance();
        req.execute("PF('wdialogo').hide()");
    }
    
    public void limpar(){
        this.usuario = null;
    }
}
