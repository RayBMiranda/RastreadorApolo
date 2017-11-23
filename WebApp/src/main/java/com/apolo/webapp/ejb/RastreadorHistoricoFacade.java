/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apolo.webapp.ejb;

import com.apolo.webapp.model.RastreadorHistorico;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 *
 * @author raybm
 */
@Stateless
public class RastreadorHistoricoFacade extends AbstractFacade<RastreadorHistorico> implements RastreadorHistoricoFacadeLocal {

    @PersistenceContext(unitName = "RastreadorPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RastreadorHistoricoFacade() {
        super(RastreadorHistorico.class);
    }
              
    @Override
    public List<RastreadorHistorico> buscarHistorico(Date dataInicial, Date dataFinal, Integer idRastreador) {
        List<RastreadorHistorico> lista = null;
        String consulta;
        try {
            consulta = "From RastreadorHistorico rh WHERE rh.dataHora between ?1 and ?2 and rh.idRastreado.idrastreador = ?3";  
            Query query = em.createQuery(consulta);
            query.setParameter(1, dataInicial, TemporalType.DATE);
            query.setParameter(2, dataFinal, TemporalType.DATE);
            query.setParameter(3, idRastreador);
            lista = query.getResultList();
        } catch (Exception e) {
            throw e;
        }
        return lista;
    }
    
}
