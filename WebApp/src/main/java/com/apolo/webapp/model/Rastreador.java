package com.apolo.webapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 *
 * @author raybm
 */
@Entity
@Table(name="rastreador")
public class Rastreador implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idrastreador;
    
    @Column(name="nome", nullable = false)
    private String nome;
    
    @Column(name="x")
    private double x;
        
    @Column(name="y")
    private double y;
            
    @Column(name="potenciapaineis")
    private double potenciapaineis;  

    @ManyToMany(mappedBy="rastreadores")
    private List<Usuario> usuarios = new ArrayList<Usuario>();

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
        
    public List<Usuario> getUsuarios() { return usuarios; }
    
    @Column(name = "chave")
    private String chave;

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }
    
/*    public Collection<UsuarioRastreador> getUsuarios() {
        return usuarioRastreadorList;
    }

    public void setUsuarios(Collection<UsuarioRastreador> usuarioRastreadorList) {
        this.usuarioRastreadorList = usuarioRastreadorList;
    }
  */  
    
    public Integer getIdrastreador() {
        return idrastreador;
    }

    public void setIdrastreador(Integer idrastreador) {
        this.idrastreador = idrastreador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getPotenciapaineis() {
        return potenciapaineis;
    }

    public void setPotenciapaineis(double potenciapaineis) {
        this.potenciapaineis = potenciapaineis;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.idrastreador);
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
        final Rastreador other = (Rastreador) obj;
        if (!Objects.equals(this.idrastreador, other.idrastreador)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Rasteador{" + "idrastreador=" + idrastreador + ", nome=" + nome + ", x=" + x + ", y=" + y + ", potenciapaineis=" + potenciapaineis + '}';
    }
    
}
