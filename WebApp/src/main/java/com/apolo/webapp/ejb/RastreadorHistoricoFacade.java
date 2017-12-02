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
            Calendar toDate = Calendar.getInstance();
            toDate.setTime(dataAtual);
            toDate.add(Calendar.HOUR_OF_DAY, 0);
            toDate.add(Calendar.MINUTE, 0);
            toDate.add(Calendar.SECOND, 0);
            toDate.add(Calendar.MILLISECOND, 0);
            dataAtual = toDate.getTime();
            java.sql.Timestamp date1 = new java.sql.Timestamp(dataAtual.getTime());
            
            toDate.setTime(dataAtual);
            toDate.add(Calendar.HOUR_OF_DAY, 23);
            toDate.add(Calendar.MINUTE, 59);
            toDate.add(Calendar.SECOND, 59);
            toDate.add(Calendar.MILLISECOND, 999);
            
            java.sql.Timestamp date2 = new java.sql.Timestamp(toDate.getTime().getTime());
            
            consulta = "From RastreadorHistorico rh WHERE rh.idRastreador.idrastreador = ?1 AND rh.dataHora BETWEEN ?2 AND ?3";  
            Query query = em.createQuery(consulta);
            
            query.setParameter(1, idRastreador);
            query.setParameter(2, date1, TemporalType.TIMESTAMP);
            query.setParameter(3, date2, TemporalType.TIMESTAMP);
            lista = query.getResultList();
            
        } catch (Exception e) {
            throw e;
        }
        return lista;
    }
    
}
