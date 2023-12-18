package com.company.springframework.controller;

import com.company.springframework.model.Empleado;
import com.company.springframework.service.EmpleadoService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/empleado")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    public List<Empleado> listarTodosLosEmpleados() {
        return empleadoService.listarTodosLosEmpleados();
    }

    @GetMapping("/{id}")
    public Empleado obtenerEmpleado(@PathVariable("id") Long id) {
        return empleadoService.obtenerEmpleado(id);
    }

    @PostMapping
    public Empleado guardarEmpleado(@RequestBody Empleado empleado) {
        return empleadoService.guardarEmpleado(empleado);
    }

    @PutMapping("/{id}")
    public Empleado actualizarEmpleado(@PathVariable("id") Long id, @RequestBody Empleado empleado) {
        empleado.setId(id);
        return empleadoService.actualizarEmpleado(empleado);
    }

    @DeleteMapping("/{id}")
    public void eliminarEmpleado(@PathVariable("id") Long id) {
        empleadoService.eliminarEmpleado(id);
    }

    @GetMapping("/reporte01")
    public ResponseEntity<byte[]> reporte01() {
        try{
            List<Empleado> empleados = empleadoService.listarTodosLosEmpleados();
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(empleados) ;
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("titulo", "Reporte de Empleados");
            JasperPrint jasperPrint = JasperFillManager.fillReport("src/main/resources/reportes/reporte01.jasper", parameters, dataSource);
            byte[] reporte = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline","reporte01.pdf");
            return ResponseEntity.ok().headers(headers).body(reporte);

        }
        catch(JRException e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/reporte02")
    public ResponseEntity<byte[]> reporte02() throws JRException, IOException {
        JasperReport report = JasperCompileManager.compileReport("src/main/resources/reportes/reporte01.jrxml");
        JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(empleadoService.listarTodosLosEmpleados());
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("parametro1", "Hola mundo");
        JasperPrint print = JasperFillManager.fillReport(report, parameters, source);
        byte [] data = JasperExportManager.exportReportToPdf(print);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + "empleado1.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(data.length)
                .body(data);

    }



}
