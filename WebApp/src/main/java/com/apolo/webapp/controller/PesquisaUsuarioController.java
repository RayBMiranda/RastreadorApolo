package com.apolo.webapp.controller;

import com.apolo.webapp.ejb.UsuarioFacadeLocal;
import com.apolo.webapp.model.Rastreador;
import com.apolo.webapp.model.Usuario;
import com.apolo.webapp.util.Mensagens;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author raybm
 */
@ManagedBean(name = "PesquisaUsuarioController")
@ViewScoped
public class PesquisaUsuarioController implements Serializable {
    @EJB
    private UsuarioFacadeLocal usuarioEJB;
    private Rastreador rastreadorSelecionado;

    public Rastreador getRastreadorSelecionado() {
        return rastreadorSelecionado;
    }

    public void setRastreadorSelecionado(Rastreador rastreadorSelecionado) {
        this.rastreadorSelecionado = rastreadorSelecionado;
    }
    private Usuario usuarioSelecionado;
    private List<Usuario> usuariosFiltrados;
    
    public List<Usuario> getUsuariosFiltrados() {
        return usuariosFiltrados;
    }

    public Usuario getUsuarioSelecionado() {
        return usuarioSelecionado;
    }

    public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
        this.usuarioSelecionado = usuarioSelecionado;
    }
    
    public void pesquisar(){
        this.usuariosFiltrados = usuarioEJB.findAll();
    }
    
    public void adicionarRastreador(){
        if(!this.usuarioSelecionado.getRastreadores().contains(rastreadorSelecionado)){    
            this.usuarioSelecionado.getRastreadores().add(rastreadorSelecionado);
            this.usuarioEJB.edit(usuarioSelecionado);
            Mensagens.exibirMensagem("Rastreador " + rastreadorSelecionado.getNome() + " cadastrado no Usuário " + this.usuarioSelecionado.getId().getNome(), false);
        }
        else
        {
         Mensagens.exibirMensagem("Rastreador " + rastreadorSelecionado.getNome() + " já cadastrado no Usuário " + this.usuarioSelecionado.getId().getNome(), true);
        }
    }
    
    public void removerRastreador(){
 //       this.usuario = (Usuario)FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("usuarioSelecionado");
        if(this.usuarioSelecionado.getRastreadores().contains(rastreadorSelecionado)){    
            this.usuarioSelecionado.getRastreadores().remove(rastreadorSelecionado);
            this.usuarioEJB.edit(usuarioSelecionado);
            Mensagens.exibirMensagem("Rastreador " + rastreadorSelecionado.getNome() + " removido do Usuário " + this.usuarioSelecionado.getId().getNome(), false);
        }
        else
        {
         Mensagens.exibirMensagem("Rastreador " + rastreadorSelecionado.getNome() + " não removido do Usuário " + this.usuarioSelecionado.getId().getNome(), true);
        }
    }
  
    public void exibirDialogo(){
        RequestContext req = RequestContext.getCurrentInstance();
        req.execute("PF(':wdialogo').show()");
    }
    
    public void ocultarDialogo(){
        RequestContext req = RequestContext.getCurrentInstance();
        req.execute("PF(':wdialogo').hide()");
    }
      
}
