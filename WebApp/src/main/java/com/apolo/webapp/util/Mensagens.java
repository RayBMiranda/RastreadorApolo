/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apolo.webapp.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author raybm
 */
public class Mensagens{
    public static void exibirMensagem(String msg, boolean msgErro){
        FacesMessage.Severity nivel;
        String titulo;
        if(msgErro){
           titulo = "Erro";
           nivel = FacesMessage.SEVERITY_FATAL;
        }
        else
        {  
            titulo = "Aviso";
            nivel = FacesMessage.SEVERITY_INFO;
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(nivel, titulo, msg));
    }
}
