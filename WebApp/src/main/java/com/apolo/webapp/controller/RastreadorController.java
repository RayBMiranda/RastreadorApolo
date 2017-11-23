package com.apolo.webapp.controller;

import com.apolo.webapp.ejb.RastreadorFacadeLocal;
import com.apolo.webapp.model.Rastreador;
import com.apolo.webapp.util.Criptografia;
import com.apolo.webapp.util.Mensagens;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author raybm
 */
@ManagedBean(name="RastreadorController")
@SessionScoped
public class RastreadorController implements Serializable{
    @EJB
    private RastreadorFacadeLocal rastreadorEJB;
    
    @Inject
    private Rastreador rastreador;
    
    private List<Rastreador> rastreadores;
    
    private Integer codigoRastreador;

    public Integer getCodigoRastreador() {
        return codigoRastreador;
    }

    public void setCodigoRastreador(Integer codigoRastreador) {
        FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().put("rastreadorSelecionado", codigoRastreador);
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
            rastreador.setChave(Criptografia.getKey());
            rastreadorEJB.create(rastreador);
            Mensagens.exibirMensagem("Rastreador " + rastreador.getNome() + " cadastrado com sucesso !", false);
        } catch (Exception e) {
            Mensagens.exibirMensagem("Erro ao Cadastra Rastreado ! \n" + e.getMessage(), true);
        }
    }
    
    public void ler(int codigoRastreador){
        this.rastreador = rastreadorEJB.find(codigoRastreador);
        FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().put("rastreadorSelecionado", rastreador);
    }
    
}
