/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apolo.webapp.controller;

import com.apolo.webapp.ejb.RastreadorFacadeLocal;
import com.apolo.webapp.model.Rastreador;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

/**
 *
 * @author raybm
 */
@ManagedBean(name = "PesquisaRastreadorController")
@ViewScoped
public class PesquisaRastreadorController implements Serializable{
    @EJB
    private RastreadorFacadeLocal rastreadorEJB;
    private List<Rastreador> rastreadoresFiltrados;
    
    public List<Rastreador> getRastreadoresFiltrados() {
        return rastreadoresFiltrados;
    }
    
    public void pesquisar(){
        rastreadoresFiltrados = rastreadorEJB.findAll();
    }
    
}
