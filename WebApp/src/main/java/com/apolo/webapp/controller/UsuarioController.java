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
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 *
 * @author raybm
 */
@ManagedBean(name = "UsuarioController")
@SessionScoped
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
        FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().put("usuarioSelecionado", usuarioSelecionado);
        this.usuario = usuarioSelecionado;
    }
    
    public void exibirDialogo(){
        RequestContext req = RequestContext.getCurrentInstance();
        req.execute("PF('wdialogo').show()");
    }
    
    public void ocultarDialogo(){
        RequestContext req = RequestContext.getCurrentInstance();
        req.execute("PF('wdialogo').hide()");
    }
    
    public void adicionarRastreador(){
        Integer idRastreador = (Integer) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("rastreadorSelecionado");
        Rastreador r = rastreadorEJB.find(idRastreador);
        this.usuario = (Usuario)FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("usuarioSelecionado");
        if(!this.usuario.getRastreadores().contains(r)){    
            this.usuario.getRastreadores().add(r);
            this.usuarioEJB.edit(usuario);
            Mensagens.exibirMensagem("Rastreador " + r.getNome() + " cadastrado no Usuário " + this.usuario.getId().getNome(), false);
        }
        else
        {
         Mensagens.exibirMensagem("Rastreador " + r.getNome() + " já cadastrado no Usuário " + this.usuario.getId().getNome(), true);
        }
        ocultarDialogo();
    } 
}
