package usta.motos.infrastructure.adapter.in.web;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import usta.motos.application.port.in.ClientUseCase;
import usta.motos.application.port.in.MaintenanceUseCase;
import usta.motos.application.port.in.MotorcycleUseCase;
import usta.motos.domain.exception.BusinessRuleException;
import usta.motos.domain.model.Client;
import usta.motos.domain.model.Maintenance;
import usta.motos.domain.model.Motorcycle;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Path("/")
@ApplicationScoped
public class WebResource {

    @Inject Template index;             // index.html
    @Inject Template motorcycles;       // motorcycles.html
    @Inject Template motorcycleForm;    // motorcycleForm.html
    @Inject Template clients;           // clients.html
    @Inject Template clientForm;        // clientForm.html
    @Inject Template maintenances;      // maintenances.html

    @Inject ClientUseCase clientUseCase;
    @Inject MotorcycleUseCase motorcycleUseCase;
    @Inject MaintenanceUseCase maintenanceUseCase;

    // 1. Página Principal
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Tag(name = "Inicio", description = "Página principal del taller")
    @Operation(summary = "Muestra la página principal", description = "Menú de acceso a motos, clientes y mantenimientos")
    public TemplateInstance home(@QueryParam("ok") String ok, @QueryParam("error") String error) {
        return index.data("success", ok)
                .data("error", error);
    }

    // Motos
    @GET
    @Path("/motorcycles")
    @Produces(MediaType.TEXT_HTML)
    @Tag(name = "Motocicletas", description = "Vistas para gestionar las motos del taller")
    @Operation(summary = "Lista las motocicletas", description = "Muestra todas las motos con su propietario")
    public TemplateInstance listMotorcycles(@QueryParam("ok") String ok, @QueryParam("error") String error) {
        return motorcycles.data("motorcycles", motorcycleUseCase.getAllMotorcycles())
                .data("success", ok)
                .data("error", error);
    }

    @GET
    @Path("/motorcycles/new")
    @Produces(MediaType.TEXT_HTML)
    @Tag(name = "Motocicletas")
    @Operation(summary = "Formulario para registrar una moto")
    public TemplateInstance newMotorcycleForm() {
        return motorcycleForm.data("motorcycle", new Motorcycle())
                .data("clients", clientUseCase.getAllClients())
                .data("action", "/motorcycles/save");
    }

    @POST
    @Path("/motorcycles/save")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Tag(name = "Motocicletas")
    @Operation(summary = "Registra una moto", description = "Valida placa (formato y única), marca, línea y propietario")
    public Response saveMotorcycle(@FormParam("plate") String plate,
                                   @FormParam("brand") String brand,
                                   @FormParam("modelLine") String modelLine,
                                   @FormParam("clientId") Long clientId) {
        try {
            Motorcycle motorcycle = Motorcycle.builder()
                    .plate(plate)
                    .brand(brand)
                    .modelLine(modelLine)
                    .client(findClient(clientId))
                    .build();

            motorcycleUseCase.saveMotorcycle(motorcycle);
            return redirect("/motorcycles", "ok", "Moto registrada exitosamente");
        } catch (BusinessRuleException e) {
            return redirect("/motorcycles", "error", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return redirect("/motorcycles", "error", "Error al registrar la moto");
        }
    }

    @GET
    @Path("/motorcycles/edit/{id}")
    @Produces(MediaType.TEXT_HTML)
    @Tag(name = "Motocicletas")
    @Operation(summary = "Formulario para editar una moto")
    public Object editMotorcycleForm(@PathParam("id") Long id) {
        Motorcycle motorcycle = motorcycleUseCase.getMotorcycleById(id);
        if (motorcycle == null) {
            return redirect("/motorcycles", "error", "La moto no existe");
        }
        return motorcycleForm.data("motorcycle", motorcycle)
                .data("clients", clientUseCase.getAllClients())
                .data("action", "/motorcycles/update");
    }

    @POST
    @Path("/motorcycles/update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Tag(name = "Motocicletas")
    @Operation(summary = "Actualiza una moto", description = "Aplica las mismas reglas que el registro")
    public Response updateMotorcycle(@FormParam("id") Long id,
                                     @FormParam("plate") String plate,
                                     @FormParam("brand") String brand,
                                     @FormParam("modelLine") String modelLine,
                                     @FormParam("clientId") Long clientId) {
        try {
            Motorcycle motorcycle = Motorcycle.builder()
                    .id(id)
                    .plate(plate)
                    .brand(brand)
                    .modelLine(modelLine)
                    .client(findClient(clientId))
                    .build();

            motorcycleUseCase.updateMotorcycle(motorcycle);
            return redirect("/motorcycles", "ok", "Moto actualizada exitosamente");
        } catch (BusinessRuleException e) {
            return redirect("/motorcycles", "error", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return redirect("/motorcycles", "error", "Error al actualizar la moto");
        }
    }

    @GET
    @Path("/motorcycles/delete/{id}")
    @Tag(name = "Motocicletas")
    @Operation(summary = "Elimina una moto", description = "No permite eliminar motos con mantenimientos registrados")
    public Response deleteMotorcycle(@PathParam("id") Long id) {
        try {
            motorcycleUseCase.deleteMotorcycle(id);
            return redirect("/motorcycles", "ok", "Moto eliminada exitosamente");
        } catch (BusinessRuleException e) {
            return redirect("/motorcycles", "error", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return redirect("/motorcycles", "error", "Error al eliminar la moto");
        }
    }

    // Clientes
    @GET
    @Path("/clients")
    @Produces(MediaType.TEXT_HTML)
    @Tag(name = "Clientes", description = "Vistas para gestionar los propietarios de las motos")
    @Operation(summary = "Lista los clientes")
    public TemplateInstance listClients(@QueryParam("ok") String ok, @QueryParam("error") String error) {
        return clients.data("clients", clientUseCase.getAllClients())
                .data("success", ok)
                .data("error", error);
    }

    @GET
    @Path("/clients/new")
    @Produces(MediaType.TEXT_HTML)
    @Tag(name = "Clientes")
    @Operation(summary = "Formulario para registrar un cliente")
    public TemplateInstance newClientForm() {
        return clientForm.data("client", new Client());
    }

    @POST
    @Path("/clients/save")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Tag(name = "Clientes")
    @Operation(summary = "Registra un cliente", description = "Valida identificación única y numérica, nombre y teléfono")
    public Response saveClient(@FormParam("identification") String identification,
                               @FormParam("fullName") String fullName,
                               @FormParam("phone") String phone) {
        try {
            Client client = Client.builder()
                    .identification(identification)
                    .fullName(fullName)
                    .phone(phone)
                    .build();

            clientUseCase.saveClient(client);
            return redirect("/clients", "ok", "Cliente registrado exitosamente");
        } catch (BusinessRuleException e) {
            return redirect("/clients", "error", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return redirect("/clients", "error", "Error al registrar el cliente");
        }
    }

    @GET
    @Path("/clients/edit/{id}")
    @Produces(MediaType.TEXT_HTML)
    @Tag(name = "Clientes")
    @Operation(summary = "Formulario para editar un cliente")
    public Object editClientForm(@PathParam("id") Long id) {
        Client client = clientUseCase.getClientById(id);
        if (client == null) {
            return redirect("/clients", "error", "El cliente no existe");
        }
        return clientForm.data("client", client);
    }

    @POST
    @Path("/clients/update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Tag(name = "Clientes")
    @Operation(summary = "Actualiza un cliente")
    public Response updateClient(@FormParam("id") Long id,
                                 @FormParam("identification") String identification,
                                 @FormParam("fullName") String fullName,
                                 @FormParam("phone") String phone) {
        try {
            Client client = Client.builder()
                    .id(id)
                    .identification(identification)
                    .fullName(fullName)
                    .phone(phone)
                    .build();

            clientUseCase.updateClient(client);
            return redirect("/clients", "ok", "Cliente actualizado exitosamente");
        } catch (BusinessRuleException e) {
            return redirect("/clients", "error", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return redirect("/clients", "error", "Error al actualizar el cliente");
        }
    }

    @GET
    @Path("/clients/delete/{id}")
    @Tag(name = "Clientes")
    @Operation(summary = "Elimina un cliente", description = "No permite eliminar clientes con motos asociadas")
    public Response deleteClient(@PathParam("id") Long id) {
        try {
            clientUseCase.deleteClient(id);
            return redirect("/clients", "ok", "Cliente eliminado exitosamente");
        } catch (BusinessRuleException e) {
            return redirect("/clients", "error", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return redirect("/clients", "error", "Error al eliminar el cliente");
        }
    }

    // Mantenimientos
    @GET
    @Path("/maintenances")
    @Produces(MediaType.TEXT_HTML)
    @Tag(name = "Mantenimientos", description = "Historial de mantenimiento de cada moto")
    @Operation(summary = "Lista el historial de mantenimientos", description = "Con el parámetro plate filtra el historial de una sola moto")
    public TemplateInstance maintenanceHistory(@QueryParam("plate") String plate,
                                               @QueryParam("ok") String ok,
                                               @QueryParam("error") String error) {
        List<Maintenance> maintenanceList = (plate != null && !plate.isBlank())
                ? maintenanceUseCase.getMaintenanceHistoryByPlate(plate)
                : maintenanceUseCase.getAllMaintenancesWithMotorcycle();

        return maintenances.data("maintenances", maintenanceList)
                .data("motorcycles", motorcycleUseCase.getAllMotorcycles())
                .data("maintenance", new Maintenance())
                .data("selectedPlate", plate)
                .data("success", ok)
                .data("error", error);
    }

    @POST
    @Path("/maintenances/save")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Tag(name = "Mantenimientos")
    @Operation(summary = "Registra un mantenimiento", description = "Valida fecha no futura, costo mayor que cero, mecánico y moto existente")
    public Response registerMaintenance(@FormParam("maintenanceDate") String maintenanceDate,
                                        @FormParam("description") String description,
                                        @FormParam("cost") String cost,
                                        @FormParam("mechanicName") String mechanicName,
                                        @FormParam("motorcycleId") Long motorcycleId) {
        try {
            Maintenance maintenance = Maintenance.builder()
                    .maintenanceDate(parseDate(maintenanceDate))
                    .description(description)
                    .cost(parseCost(cost))
                    .mechanicName(mechanicName)
                    .motorcycle(findMotorcycle(motorcycleId))
                    .build();

            maintenanceUseCase.registerMaintenance(maintenance);
            return redirect("/maintenances", "ok", "Mantenimiento registrado exitosamente");
        } catch (BusinessRuleException e) {
            return redirect("/maintenances", "error", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return redirect("/maintenances", "error", "Error al registrar el mantenimiento");
        }
    }

    @GET
    @Path("/maintenances/edit/{id}")
    @Produces(MediaType.TEXT_HTML)
    @Tag(name = "Mantenimientos")
    @Operation(summary = "Carga un mantenimiento en el formulario para editarlo")
    public Object editMaintenanceForm(@PathParam("id") Long id) {
        Maintenance maintenanceToEdit = maintenanceUseCase.getMaintenanceById(id);
        if (maintenanceToEdit == null) {
            return redirect("/maintenances", "error", "El mantenimiento no existe");
        }
        return maintenances.data("maintenances", maintenanceUseCase.getAllMaintenancesWithMotorcycle())
                .data("motorcycles", motorcycleUseCase.getAllMotorcycles())
                .data("maintenance", maintenanceToEdit) // Carga los datos en el formulario izquierdo
                .data("selectedPlate", null)
                .data("success", null)
                .data("error", null);
    }

    @POST
    @Path("/maintenances/update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Tag(name = "Mantenimientos")
    @Operation(summary = "Actualiza un mantenimiento", description = "Aplica las mismas reglas que el registro")
    public Response updateMaintenance(@FormParam("id") Long id,
                                      @FormParam("maintenanceDate") String maintenanceDate,
                                      @FormParam("description") String description,
                                      @FormParam("cost") String cost,
                                      @FormParam("mechanicName") String mechanicName,
                                      @FormParam("motorcycleId") Long motorcycleId) {
        try {
            Maintenance maintenance = Maintenance.builder()
                    .id(id)
                    .maintenanceDate(parseDate(maintenanceDate))
                    .description(description)
                    .cost(parseCost(cost))
                    .mechanicName(mechanicName)
                    .motorcycle(findMotorcycle(motorcycleId))
                    .build();

            maintenanceUseCase.updateMaintenance(maintenance);
            return redirect("/maintenances", "ok", "Mantenimiento actualizado exitosamente");
        } catch (BusinessRuleException e) {
            return redirect("/maintenances", "error", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return redirect("/maintenances", "error", "Error al actualizar el mantenimiento");
        }
    }

    @GET
    @Path("/maintenances/delete/{id}")
    @Tag(name = "Mantenimientos")
    @Operation(summary = "Elimina un mantenimiento")
    public Response deleteMaintenance(@PathParam("id") Long id) {
        try {
            maintenanceUseCase.deleteMaintenance(id);
            return redirect("/maintenances", "ok", "Mantenimiento eliminado exitosamente");
        } catch (BusinessRuleException e) {
            return redirect("/maintenances", "error", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return redirect("/maintenances", "error", "Error al eliminar el mantenimiento");
        }
    }


    private Response redirect(String path, String param, String message) {
        String encoded = URLEncoder.encode(message, StandardCharsets.UTF_8);
        return Response.seeOther(URI.create(path + "?" + param + "=" + encoded)).build();
    }

    private Client findClient(Long clientId) {
        return (clientId != null) ? clientUseCase.getClientById(clientId) : null;
    }

    private Motorcycle findMotorcycle(Long motorcycleId) {
        return (motorcycleId != null) ? motorcycleUseCase.getMotorcycleById(motorcycleId) : null;
    }


    private LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            throw new BusinessRuleException("La fecha no tiene un formato válido");
        }
    }


    private double parseCost(String value) {
        if (value == null || value.isBlank()) {
            throw new BusinessRuleException("El costo es obligatorio");
        }
        try {
            return Double.parseDouble(value.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            throw new BusinessRuleException("El costo debe ser un número");
        }
    }
}
