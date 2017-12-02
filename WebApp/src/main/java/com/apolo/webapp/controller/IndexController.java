package com.apolo.webapp.controller;

import com.apolo.webapp.ejb.UsuarioFacadeLocal;
import com.apolo.webapp.model.Mensagem;
import com.apolo.webapp.model.Usuario;
import com.apolo.webapp.util.Criptografia;
import com.apolo.webapp.util.Email;
import com.apolo.webapp.util.Mensagens;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 *
 * @author raybm
 */
@ManagedBean(name = "IndexController")
@ViewScoped
public class IndexController implements Serializable{
    @EJB
    private UsuarioFacadeLocal EJBUsuario;
    
    @Inject
    private Usuario usuario; 
    
    @PostConstruct
    public void init(){
       // usuario = new Usuario();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public String iniciarSessao(){
        Usuario us;
        String redirecionar = null;
        try {
            us = EJBUsuario.iniciarSessao(usuario);
            if(us != null){
                // Armazenar na sessão de JSF 
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", us);
                redirecionar = "/restrito/principal?faces-redirect=true";
            }
            else
            {
               Mensagens.exibirMensagem("Usuário e/ou Senha Incorretos", true);    
            }
        } catch (Exception e) {
          Mensagens.exibirMensagem("Erro ao Registrar Usuário", true);    
        }
        return redirecionar;
    }
    
    public String recuperarSenha() throws Exception{
        usuario = EJBUsuario.findEmail(usuario);
        if(usuario != null){
            String senha = Criptografia.getKey();
            usuario.setSenha(senha);
            EJBUsuario.edit(usuario);
            Mensagem mensagem = new Mensagem();
            mensagem.setDestino(usuario.getEmail());
            mensagem.setMensagem("Sua nova senha é : " + senha);
            mensagem.setTitulo("Alteração de Senha Apolo");
            RequestContext.getCurrentInstance().execute("swal({\n" +
"  title: 'Envio de Mensagem!',\n" +
"  text: 'Enviando Mensagem...',\n" +
"  timer: 5000,\n" +
"  onOpen: () => {\n" +
"    swal.showLoading()\n" +
"  }\n" +
"}).then((result) => {\n" +
"  if (result.dismiss === 'timer') {\n" +
"    console.log('I was closed by the timer')\n" +
"  }\n" +
"})");
            Email e = new Email();
            e.setMensagem(mensagem);
            e.start();            
//            String retorno = Email.enviaEmail(mensagem);      
//            Mensagens.exibirMensagem(retorno, false);      
    }
   return "";  
}

}