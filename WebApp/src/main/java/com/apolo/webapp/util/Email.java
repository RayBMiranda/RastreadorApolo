package com.apolo.webapp.util;

import com.apolo.webapp.model.Mensagem;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 *
 * @author raybm
 */
public class Email extends Thread{

    private static final String HOSTNAME = "smtp.gmail.com";
    private static final String USERNAME = "apolorastreador@gmail.com";
    private static final String PASSWORD = "rastreadorsolar";
    private static final String EMAILORIGEM = "raybmiranda@gmail.com";
    private Mensagem mensagem;
    private String retorno;

    public String getRetorno() {
        return retorno;
    }
    
    public Mensagem getMensagem() {
        return mensagem;
    }

    public void setMensagem(Mensagem mensagem) {
        this.mensagem = mensagem;
    }
        
    public static SimpleEmail conectaEmail() throws EmailException {
        SimpleEmail email = new SimpleEmail();
        email.setHostName(HOSTNAME);
        email.setSmtpPort(587);
        email.setAuthenticator(new DefaultAuthenticator(USERNAME, PASSWORD));
        email.setStartTLSEnabled(true);
        email.setFrom(EMAILORIGEM);
        return email;
    }
 
    public static String enviaEmail(Mensagem mensagem) throws EmailException, InterruptedException {
       SimpleEmail email;
       email = conectaEmail();
       email.setSubject(mensagem.getTitulo());
       email.setMsg(mensagem.getMensagem());
       email.addTo(mensagem.getDestino());    
       email.send();
       return "E-mail enviado com sucesso para: " + mensagem.getDestino();
    }   
    
    @Override
    public void run() {
        try {
           retorno = enviaEmail(this.mensagem);
           Mensagens.exibirMensagem(retorno, false);
        } catch (EmailException | InterruptedException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
