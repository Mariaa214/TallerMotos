package usta.motos.infrastructure.adapter.in.web;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import usta.motos.application.port.in.MaintenanceUseCase;
import usta.motos.domain.model.Maintenance;

import java.util.List;


@Path("/reports")
@ApplicationScoped
@Tag(name = "Reportes", description = "Reportes PDF generados con OpenPDF")
public class ReportResource {

    @Inject
    MaintenanceUseCase maintenanceUseCase;

    @GET
    @Path("/maintenances/pdf")
    @Produces("application/pdf")
    @Operation(summary = "Descarga el reporte PDF de mantenimientos",
            description = "Sin parámetros incluye todos los mantenimientos; con plate genera el historial de una sola moto")
    public Response downloadMaintenancePdf(@QueryParam("plate") String plate) {
        boolean byPlate = plate != null && !plate.isBlank();
        List<Maintenance> maintenances = byPlate
                ? maintenanceUseCase.getMaintenanceHistoryByPlate(plate)
                : maintenanceUseCase.getAllMaintenances();

        String subtitle = byPlate
                ? "Historial de mantenimientos de la moto " + plate.trim().toUpperCase()
                : "Reporte general de mantenimientos";
        byte[] pdfBytes = PdfReports.generateMaintenanceReport(maintenances, subtitle);

        String fileName = byPlate ? "historial_" + plate.trim().toUpperCase() + ".pdf" : "reporte_mantenimientos.pdf";
        return Response.ok(pdfBytes)
                .header("Content-Disposition", "attachment; filename=" + fileName)
                .build();
    }
}
