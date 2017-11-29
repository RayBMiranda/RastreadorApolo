package com.apolo.webapp.ejb;

import com.apolo.webapp.model.RastreadorHistorico;
import java.util.Calendar;
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
    public List<RastreadorHistorico> buscarHistorico(Date dataAtual, Integer idRastreador) {
        List<RastreadorHistorico> lista = null;
        String consulta;
        try {
            consulta = "From RastreadorHistorico rh WHERE rh.dataHora Between ?1 AND ?2 ";  
            Query query = em.createQuery(consulta);
            Calendar c = Calendar.getInstance();
            c.setTime(dataAtual);
            c.add(Calendar.DAY_OF_MONTH, 1);
            query.setParameter(1, dataAtual, TemporalType.DATE);
            query.setParameter(2, c.getTime(), TemporalType.DATE);
            lista = query.getResultList();
        } catch (Exception e) {
            throw e;
        }
        return lista;
    }
    
}
