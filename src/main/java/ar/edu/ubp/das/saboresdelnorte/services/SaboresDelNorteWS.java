package ar.edu.ubp.das.saboresdelnorte.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ar.edu.ubp.das.saboresdelnorte.beans.ActualizarContenidosNoPublicadosRequestBean;
import ar.edu.ubp.das.saboresdelnorte.beans.ContenidoNoPublicadoResponseBean;
import ar.edu.ubp.das.saboresdelnorte.beans.CrearReservaConClienteRequestDTO;
import ar.edu.ubp.das.saboresdelnorte.beans.ObtenerDisponibilidadHorariaZonaResponseBean;
import ar.edu.ubp.das.saboresdelnorte.repositories.SaboresDelNorteRepository;
import ar.edu.ubp.das.saboresdelnorte.utils.Utils;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.xml.ws.RequestWrapper;
import jakarta.xml.ws.ResponseWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@WebService(serviceName = "SaboresDelNorteWS", targetNamespace = "http://services.saboresdelnorte.das.ubp.edu.ar/")
public class SaboresDelNorteWS {

  @Autowired
  private SaboresDelNorteRepository saboresdelnorteRepository;

  private static final Gson GSON = new Gson();

  @WebMethod(operationName = "RegistrarClickContenido")
  @RequestWrapper(localName = "RegistrarClickContenidoRequest")
  @ResponseWrapper(localName = "RegistrarClickContenidoResponse")
  public void registrarClickContenido(@WebParam(name = "Body") String body) {

    Integer nroCliente = null;
    // Transformar JSON String a Json
    JsonObject json = JsonParser.parseString(body).getAsJsonObject();
    if (json.has("nroCliente") && !json.get("nroCliente").isJsonNull()) {
      // 1) Insertar cliente
      nroCliente = saboresdelnorteRepository.insertarClienteDesdeRistorino(
          json.get("apellido").getAsString(),
          json.get("nombre").getAsString(),
          json.get("correo").getAsString(),
          json.get("telefonos").getAsString());
    }

    saboresdelnorteRepository.registrarClickContenido(
        json.get("codContenidoRestaurante").getAsString(),
        json.get("nroContenido").getAsInt(),
        json.get("nroClick").getAsInt(),
        json.get("fechaHoraRegistro").getAsString(),
        (json.has("nroCliente") && !json.get("nroCliente").isJsonNull())
            ? nroCliente
            : null,
        json.get("costoClick").getAsBigDecimal());
  }

  @WebMethod(operationName = "CrearReservaDesdeRistorino")
  @RequestWrapper(localName = "CrearReservaDesdeRistorinoRequest")
  @ResponseWrapper(localName = "CrearReservaDesdeRistorinoResponse")
  @WebResult(name = "SoapStringResponse")
  public String crearReservaDesdeRistorino(@WebParam(name = "Body") String body) {
    CrearReservaConClienteRequestDTO reservaCliente = GSON.fromJson(body, CrearReservaConClienteRequestDTO.class);

    List<ObtenerDisponibilidadHorariaZonaResponseBean> horariosPorZona = saboresdelnorteRepository
        .obtenerDisponibilidadHorariaZona(
            reservaCliente.getReserva().getNroSucursal(),
            reservaCliente.getReserva().getCodZona(),
            reservaCliente.getReserva().getFechaReserva());

    String horaRequest = reservaCliente.getReserva().getHoraReserva().toString();

    Optional<ObtenerDisponibilidadHorariaZonaResponseBean> opt = horariosPorZona.stream()
        .filter(h -> horaRequest.equals(h.getHoraDesde().substring(0, 5)))
        .findFirst();

    if (opt.isEmpty()) {
      // no existe turno para esa hora
      throw new RuntimeException("RESERVA_TURNO_INEXISTENTE");
    }

    ObtenerDisponibilidadHorariaZonaResponseBean turno = opt.get();

    // Validaciones sobre el encontrado
    if (turno.getHabilitado() != null && turno.getHabilitado() == 0) {
      throw new RuntimeException("RESERVA_TURNO_NO_HABILITADO");
    }
    if (turno.getCupoDisponible() == null || turno.getCupoDisponible() <= 0) {
      throw new RuntimeException("RESERVA_SIN_CUPO");
    }
    if (turno.getCupoDisponible() < reservaCliente.getReserva().getCantAdultos()
        + reservaCliente.getReserva().getCantMenores()) {
      throw new RuntimeException("RESERVA_CUPO_INSUFICIENTE");
    }

    String codReservaSucursal = Utils.generarCodigoReserva();

    // 1) Insertar cliente
    saboresdelnorteRepository.insertarClienteDesdeRistorino( // aca sacamos lo del cliente
        reservaCliente.getCliente().getApellido(),
        reservaCliente.getCliente().getNombre(),
        reservaCliente.getCliente().getCorreo(),
        reservaCliente.getCliente().getTelefonos());

    // 2) Insertar reserva
    saboresdelnorteRepository.crearReservaSucursal(
        codReservaSucursal,
        reservaCliente.getReserva().getNroCliente(),
        LocalDate.parse(reservaCliente.getReserva().getFechaReserva()),
        reservaCliente.getReserva().getNroSucursal(),
        reservaCliente.getReserva().getCodZona(),
        LocalTime.parse(reservaCliente.getReserva().getHoraReserva()),
        reservaCliente.getReserva().getCantAdultos(),
        reservaCliente.getReserva().getCantMenores(),
        reservaCliente.getReserva().getCostoReserva());

    return codReservaSucursal;
  }

  // OBTENER CONTENIDOS NO PUBLICADOS
  @WebMethod(operationName = "ObtenerContenidosNoPublicados")
  @RequestWrapper(localName = "ObtenerContenidosNoPublicadosRequest")
  @ResponseWrapper(localName = "ObtenerContenidosNoPublicadosResponse")
  @WebResult(name = "SoapStringResponse")
  public String obtenerContenidosNoPublicados() {
    List<ContenidoNoPublicadoResponseBean> contenidos = saboresdelnorteRepository.getContenidosNoPublicados();
    return new Gson().toJson(contenidos);
  }

  // ACTUALIZAR LOS CONTENIDOS NO PUBLICADOS A PUBLICADOS
  @WebMethod(operationName = "ActualizarContenidosNoPublicados")
  @RequestWrapper(localName = "ActualizarContenidosNoPublicadosRequest")
  @ResponseWrapper(localName = "ActualizarContenidosNoPublicadosResponse")
  public void ActualizarContenidosNoPublicados(
      @WebParam(name = "Body") String body) {
    ActualizarContenidosNoPublicadosRequestBean bean = GSON.fromJson(body,
        ActualizarContenidosNoPublicadosRequestBean.class);
    saboresdelnorteRepository.actualizarContenidoPublicado(new Gson().toJson(bean.getContenidos()));
  }

  // OBTENER DISPONIBILIDAD HORARIA ZONA
  @WebMethod(operationName = "ObtenerDisponibilidadHorariaZona")
  @RequestWrapper(localName = "ObtenerDisponibilidadHorariaZonaRequest")
  @ResponseWrapper(localName = "ObtenerDisponibilidadHorariaZonaResponse")
  @WebResult(name = "SoapStringResponse")
  public String obtenerDisponibilidadHorariaZona(@WebParam(name = "Body") String body) {
    JsonObject json = JsonParser.parseString(body).getAsJsonObject();
    return new Gson().toJson(saboresdelnorteRepository.obtenerDisponibilidadHorariaZona(
        json.get("nroSucursal").getAsInt(),
        json.get("codZona").getAsString(),
        json.get("fechaAReservar").getAsString()));
  }

}