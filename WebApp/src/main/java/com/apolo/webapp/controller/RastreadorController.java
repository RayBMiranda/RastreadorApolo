package com.apolo.webapp.controller;

import com.apolo.webapp.ejb.RastreadorFacadeLocal;
import com.apolo.webapp.model.Rastreador;
import com.apolo.webapp.model.Usuario;
import com.apolo.webapp.util.Criptografia;
import com.apolo.webapp.util.Mensagens;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author raybm
 */
@ManagedBean(name="RastreadorController")
@ViewScoped
public class RastreadorController implements Serializable{
    @EJB
    private RastreadorFacadeLocal rastreadorEJB;
    @Inject
    private Rastreador rastreador;
    private List<Rastreador> rastreadores;
    private Integer codigoRastreador;
    private String acao = "R";
    @Inject
    private Usuario usuarioSelecionado;

    public Usuario getUsuarioSelecionado() {
        return usuarioSelecionado;
    }

    public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
        this.usuarioSelecionado = usuarioSelecionado;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }
    
    public Integer getCodigoRastreador() {
        return codigoRastreador;
    }

    public void setCodigoRastreador(Integer codigoRastreador) {
        this.codigoRastreador = codigoRastreador;
    }

    public List<Rastreador> getRastreadores() {
        return rastreadores;
    }

    public void setRastreadores(List<Rastreador> rastreadores) {
        this.rastreadores = rastreadores;
    }
    
    public Rastreador getRastreador() {
        return rastreador;
    }

    public void setRastreador(Rastreador rastreador) {
        this.rastreador = rastreador;
    }
    
    @PostConstruct
    public void init(){
        rastreadores = rastreadorEJB.findAll();
    }
    
    public void registrar(){
        try {
            this.rastreador.setChave(Criptografia.getKey());
            rastreadorEJB.create(this.rastreador);
            Mensagens.exibirMensagem("Rastreador " + rastreador.getNome() + " cadastrado com sucesso !", false);
            this.limpar();
        } catch (Exception e) {
            Mensagens.exibirMensagem("Erro ao cadastrar rastreador ! \n" + e.getMessage(), true);
        }
    }
    
    public void editar(Rastreador ras){
        try {
            rastreadorEJB.edit(ras);
            Mensagens.exibirMensagem("Rastreador " + ras.getNome() + " alterado com sucesso !", false);
        } catch (Exception e) {
            Mensagens.exibirMensagem("Erro ao alterar rastreador ! \n" + e.getMessage(), true);
        }
    }
    
    public void remover(){
        try {
            rastreadorEJB.remove(rastreador);
            Mensagens.exibirMensagem("Rastreador " + rastreador.getNome() + " removido com sucesso !", false);
        } catch (Exception e) {
            if(!rastreador.getUsuarios().isEmpty()){
                String usuarios = "";
                for(Usuario usu : rastreador.getUsuarios() )
                {
                      usuarios += usu.getId().getNome();
                      usuarios += " " + usu.getId().getSobrenome();
                      usuarios += " | ";
                }
                Mensagens.exibirMensagem("Rastreador cadastrado para os usuarios \n" + usuarios, true);
            }
            else
            Mensagens.exibirMensagem("Erro ao remover rastreador ! \n" + e.getMessage(), true);
        }
    }
    
    public void listar(){
        try {
            rastreadores = rastreadorEJB.findAll();
        } catch (Exception e) {
            Mensagens.exibirMensagem("Erro ao atualizar lista de rastreadores ! \n" + e.getMessage(), true);
        }
    }
    
    public void limpar(){
        try {
            this.rastreador = null;
        } catch (Exception e) {
            Mensagens.exibirMensagem("Erro ao limpar rastreador ! \n" + e.getMessage(), true);
        }
    }
    
    public void removerUsuario(Usuario usu){
        try {
            usuarioSelecionado = usu;
            this.rastreador.getUsuarios().remove(usuarioSelecionado);
            rastreadorEJB.edit(this.rastreador);
            Mensagens.exibirMensagem("Usuário " + usuarioSelecionado.getId().getNome() + " removido com sucesso !", false);
        } catch (Exception e) {
            Mensagens.exibirMensagem("Usuário " + usuarioSelecionado.getId().getNome() + " não pode ser removido !", true);    
        }
    }
    
    public void ler(int codigoRastreador){
        this.codigoRastreador = codigoRastreador;
        this.rastreador = rastreadorEJB.find(codigoRastreador);
        this.rastreador.getUsuarios();
    }
    
}
