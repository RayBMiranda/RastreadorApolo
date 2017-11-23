package com.apolo.webapp.controller;

import com.apolo.webapp.ejb.RastreadorFacadeLocal;
import com.apolo.webapp.ejb.RastreadorHistoricoFacadeLocal;
import com.apolo.webapp.model.Rastreador;
import com.apolo.webapp.model.RastreadorHistorico;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

/**
 *
 * @author raybm
 */
@ManagedBean(name = "RastreadorHistoricoController")
@ViewScoped
public class RastreadorHistoricoController implements Serializable{
    @EJB
    private RastreadorHistoricoFacadeLocal rastreadorHistoricoEJB;
    
    @EJB
    private RastreadorFacadeLocal rastreadorEJB;
    private List<RastreadorHistorico> listaRastreadorHistorico = null;
    
    private LineChartModel lineModelGeracao = null;
    
    @Inject
    private RastreadorHistorico rastreadorHistorico;
    
    private Date dataInicial; 
    private Date dataFinal;
    private Integer idRastreador;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    
    public Integer getIdRastreador() {
        return idRastreador;
    }

    public void setIdRastreador(Integer idRastreador) {
        this.idRastreador = idRastreador;
    }
    
    public RastreadorHistorico getRastreadorHistorico() {
        return rastreadorHistorico;
    }

    public void setRastreadorHistorico(RastreadorHistorico rastreadorHistorico) {
        this.rastreadorHistorico = rastreadorHistorico;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public LineChartModel getLineModelGeracao() {
        listaRastreadorHistorico = rastreadorHistoricoEJB.buscarHistorico(dataInicial, dataFinal, idRastreador);
        if(lineModelGeracao != null)
            lineModelGeracao.clear();
        lineModelGeracao = initGeracaoModel();
        lineModelGeracao.setTitle("Histórico de Geração");
        lineModelGeracao.setLegendPosition("e");
        lineModelGeracao.setShowPointLabels(true);
        lineModelGeracao.getAxes().put(AxisType.X, new CategoryAxis("Data"));
        Axis yAxis = lineModelGeracao.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setMax(50);
        return lineModelGeracao;
    }

    public void setLineModelGeracao(LineChartModel lineModelGeracao) {
        this.lineModelGeracao = lineModelGeracao;
    }
     
    @PostConstruct
    public void init(){
        // data final igual a hoje
        dataFinal = new Date();
        // usa calendar para subtrair data
        Calendar calendarData = Calendar.getInstance();
        calendarData.setTime(dataFinal);
        int numeroDiasParaSubtrair = 4;
        // achar data de início
        calendarData.add(Calendar.DATE, numeroDiasParaSubtrair*-1);
        dataInicial = calendarData.getTime();  
    }
    
     private LineChartModel initGeracaoModel() {
        LineChartModel model = new LineChartModel();
 
        ChartSeries volts = new ChartSeries();
        volts.setLabel("Volts");
        ChartSeries amperes = new ChartSeries();
        amperes.setLabel("Amperes");
        ChartSeries watts = new ChartSeries();
        watts.setLabel("Watts");
        if(listaRastreadorHistorico != null)
        {
            for (RastreadorHistorico rastreadorH : listaRastreadorHistorico) {
                String dataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rastreadorH.getDataHora());
                Double tensao = rastreadorH.getTensao();
                Double corrente = rastreadorH.getCorrente();
                Double potencia = tensao * corrente;
                volts.set(dataHora, tensao);  
                amperes.set(dataHora, corrente);
                watts.set(dataHora, potencia);   
            }
        }
        else
        {
                volts.set("", 0);  
                amperes.set("", 0);
                watts.set("", 0);   
        }
        model.addSeries(volts);
        model.addSeries(amperes);
        model.addSeries(watts);
         
        return model;
    }

    public void gravar() throws Exception{
        Rastreador r = null;
        r = rastreadorEJB.findChave(key);
        if(r != null){
            rastreadorHistorico.setIdRastreador(r);
            rastreadorHistorico.setDataHora(Calendar.getInstance().getTime());
            if(rastreadorHistorico.getIdRastreador() != null)
                rastreadorHistoricoEJB.create(rastreadorHistorico);
        }    
    } 
     
}
