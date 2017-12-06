package com.apolo.webapp.relatorio;

import com.apolo.webapp.model.RastreadorHistorico;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import org.omnifaces.util.Faces;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author raybm
 */
public class Relatorio {
   private HttpServletResponse response;
   private FacesContext context;
   private ByteArrayOutputStream baos;
   private InputStream stream;
   
   public Relatorio(){
       this.context = FacesContext.getCurrentInstance();
       this.response = (HttpServletResponse) context.getExternalContext().getResponse();
   }
   
   public void getRelatorio(List<RastreadorHistorico> lista){
       //String path = "/report/relatorioGeracao.jasper"; 
        stream = this.getClass().getResourceAsStream("/report/relatorioGeracao.jasper");
       Timestamp dataInicio = Timestamp.valueOf("2017-11-28 00:00:00.000"); 
       Timestamp dataTermino = Timestamp.valueOf("2017-11-28 23:59:59.999");
       String caminho = Faces.getRealPath("/restrito/relatorios/relatorioGeracao.jasper");
       File file = new File(caminho);
      // stream = this.getClass().getResourceAsStream("report/relatorioGeracao.jasper");
       InputStream image = this.getClass().getResourceAsStream("images/apolo-logo.png");
       Map<String, Object> params = new HashMap<String, Object>();
       params.put("dataInicio", dataInicio);
       params.put("dataTermino", dataTermino);
       params.put("idRastreador", 1);
       params.put("lista", lista);
       
       baos = new ByteArrayOutputStream();
       try {
           JasperReport report = (JasperReport) JRLoader.loadObject(file);
         //  JasperPrint print = JasperFillManager.fillReport(report, params);
           JasperPrint print = JasperFillManager.fillReport(report, params, new JRBeanCollectionDataSource(lista));
           JasperExportManager.exportReportToPdfStream(print, baos);
           
           response.reset();
           response.setContentType("application/pdf");
           response.setContentLength(baos.size());
           //inline
           response.setHeader("Content-disposition", "attachment; filename=relatorio.pdf");
           response.getOutputStream().write(baos.toByteArray());
           response.getOutputStream().flush();
           response.getOutputStream().close();
           
           context.responseComplete();
       } catch (JRException ex) {
           Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
       } catch (IOException ex) {
           Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
       }
   }
}
