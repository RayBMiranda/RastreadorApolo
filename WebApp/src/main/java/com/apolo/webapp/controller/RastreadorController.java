package com.apolo.webapp.controller;

import com.apolo.webapp.ejb.RastreadorFacadeLocal;
import com.apolo.webapp.model.Rastreador;
import com.apolo.webapp.model.Usuario;
import com.apolo.webapp.util.Criptografia;
import com.apolo.webapp.util.Mensagens;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

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
    
    public void download() throws IOException {
    //Pega a instancia 
    FacesContext fc = FacesContext.getCurrentInstance();

    //Pega o contexto de resposta
    HttpServletResponse ec = (HttpServletResponse) fc.getExternalContext().getResponse();

    //Zerando qualquer coisa que possa ter sido colocada na resposta
    ec.reset();
    
    //Colocando o tipo do arquivo, procure na internet os tipos disponiveis, no tipo abaixo será para TXT
    ec.setContentType("text/plain"); 
    
    //Caso queira mostrar o tamanho do download, setar o tamanho abaixo
    //ec.setContentLength(contentLength); 

    //Coloca o nome do arquivo
    ec.setHeader("Content-Disposition", "attachment; filename=\"Arquivo.txt\""); 

    //Pega o output para escrever no arquivo
    OutputStream output = ec.getOutputStream();   
 
    //Escrevendo TESTE
    output.write("teste".getBytes());

    //Para finalizar o processo
    output.flush();
    output.close();
    fc.responseComplete(); 
}
    
}
