package com.apolo.webapp.model;

import com.apolo.webapp.ejb.SQLInjectionSafe;
import com.apolo.webapp.util.Criptografia;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author raybm
 */
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable{
     
    @Id
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="id", nullable = false)
    private Pessoa id;
    
    @Column(name="email", unique=true, nullable=false)
    private @SQLInjectionSafe String email;
    
    @Column(name="senha", nullable=false)
    private @SQLInjectionSafe String senha;
    
    @Column(name="tipo", nullable=false)
    private String tipo;
    
    @ManyToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
    @JoinTable(name="usuariorastreador",
        joinColumns=
            @JoinColumn(name="idusuario", referencedColumnName="id"),
        inverseJoinColumns=
            @JoinColumn(name="idrastreador", referencedColumnName="idrastreador")
        )
    public List<Rastreador> rastreadores = new ArrayList<Rastreador>();
    
    public List<Rastreador> getRastreadores() { return rastreadores; }

    public void setRastreadores(List<Rastreador> rastreadores) {
        this.rastreadores = rastreadores;
    }
    
    public Pessoa getId() {
        return id;
    }

    public void setId(Pessoa id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        try {
            this.senha = Criptografia.criptografarSHA1(senha);
        } catch (Exception ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id);
        hash = 29 * hash + Objects.hashCode(this.email);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Usuario other = (Usuario) obj;

        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + '}';
    }
    
}
