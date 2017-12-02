package com.apolo.webapp.controller;

import com.apolo.webapp.ejb.RastreadorFacadeLocal;
import com.apolo.webapp.ejb.UsuarioFacadeLocal;
import com.apolo.webapp.model.Pessoa;
import com.apolo.webapp.model.Rastreador;
import com.apolo.webapp.model.Usuario;
import com.apolo.webapp.util.Mensagens;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
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
    private Integer codigoRastreadorSelecionado;

    private List<Usuario> usuarios;
    
    @PostConstruct
    public void init(){
        usuarios = usuarioEJB.findAll();
    }

    public Integer getCodigoRastreadorSelecionado() {
        return codigoRastreadorSelecionado;
    }

    public void setCodigoRastreadorSelecionado(Integer codigoRastreadorSelecionado) {
        this.codigoRastreadorSelecionado = codigoRastreadorSelecionado;
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
        System.out.println("passei aqui");
        usuarioEJB.edit(usuario);
        Mensagens.exibirMensagem("Registro Modificado", false);
    }
    
    public void registrar(){
    try {
          Calendar c = Calendar.getInstance();
          this.usuario.setId(pessoa);
          if(usuarioEJB.existeUsuario(usuario))
          {
            Mensagens.exibirMensagem("Email já Cadastrado", true);
            return;
          }
          Calendar dataAnterior = Calendar.getInstance();
          dataAnterior.add(Calendar.YEAR, -100);
          if(usuario.getId().getDataNascimento().compareTo(c.getTime()) >= 0 || usuario.getId().getDataNascimento().compareTo(dataAnterior.getTime()) <= 0)
          {
            Mensagens.exibirMensagem("Data de Nascimento Inválida", true);
            return;
          }
          usuarioEJB.create(usuario);
          usuario.setId(null);
          limpar();
          Mensagens.exibirMensagem("Usuário Registrado", false);
        } catch (Exception e) { 
          Mensagens.exibirMensagem("Erro ao Registrar Usuário", true);
        }
    }
    
    public void ler(Usuario usuarioSelecionado){
        this.usuario = usuarioSelecionado;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioSelecionado", this.usuario);
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
    
    public void remover(Rastreador ras){
        rastreadorEJB.remove(ras);
    }
    
    public void lerRastreador(Integer codigo){
        this.codigoRastreadorSelecionado = codigo;
    }
    
    public void adicionarRastreador(){
        this.usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioSelecionado");
        Rastreador rastreadorSelecionado = rastreadorEJB.find(codigoRastreadorSelecionado);
        if(!this.usuario.getRastreadores().contains(rastreadorSelecionado)){    
            this.usuario.getRastreadores().add(rastreadorSelecionado);
            this.usuarioEJB.edit(this.usuario);
            Mensagens.exibirMensagem("Rastreador " + rastreadorSelecionado.getNome() + " cadastrado no Usuário " + this.usuario.getId().getNome(), false);
        }
        else
        {
         Mensagens.exibirMensagem("Rastreador " + rastreadorSelecionado.getNome() + " já cadastrado no Usuário " + this.usuario.getId().getNome(), true);
        }
    }
    
    public void removerUsuario(Usuario usuario){
        try
            {
             usuarioEJB.remove(usuario);
             usuarios = usuarioEJB.findAll();
             Mensagens.exibirMensagem("Usuário " + this.usuario.getId().getNome() + " Removido ", false);     
            }
            catch(Exception e)
            {
                Mensagens.exibirMensagem("Erro ao Remover o Usuário " + this.usuario.getId().getNome() + " " + e.getMessage(), true);     
            }
    }
    
    public void validaNumero(FacesContext context, UIComponent toValidate, Object value) {
        boolean valida = false;
        if(value != null){      
           for (char letra : ((String) value).toCharArray()) { 
              if(letra < '0' || letra > '9') { 
                 valida = true;
                 break; 
            }  
         }
         ((UIInput) toValidate).setValid(!valida);
        FacesMessage message = new FacesMessage(" Valor com numeros!");
        message.setSeverity(FacesMessage.SEVERITY_ERROR);
        context.addMessage(toValidate.getClientId(context), message);
        }
    }
        
}    

