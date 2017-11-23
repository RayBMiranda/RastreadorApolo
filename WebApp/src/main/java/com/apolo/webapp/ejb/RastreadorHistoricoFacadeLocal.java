/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apolo.webapp.ejb;

import com.apolo.webapp.model.RastreadorHistorico;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author raybm
 */
@Local
public interface RastreadorHistoricoFacadeLocal {

    void create(RastreadorHistorico rastreadorHistorico);

    void edit(RastreadorHistorico rastreadorHistorico);

    void remove(RastreadorHistorico rastreadorHistorico);

    RastreadorHistorico find(Object id);

    List<RastreadorHistorico> findAll();

    List<RastreadorHistorico> findRange(int[] range);

    int count();
    
    public List<RastreadorHistorico> buscarHistorico(Date dataInicial, Date dataFinal, Integer idRastreador);
    
}
