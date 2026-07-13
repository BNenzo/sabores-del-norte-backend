package ar.edu.ubp.das.saboresdelnorte.endpoint;

import ar.edu.ubp.das.saboresdelnorte.services.SaboresDelNorteWS;
import ar.edu.ubp.das.saboresdelnorte.services.jaxws.RegistrarClickContenido;
import ar.edu.ubp.das.saboresdelnorte.services.jaxws.RegistrarClickContenidoResponse;
import ar.edu.ubp.das.saboresdelnorte.services.jaxws.CrearReservaDesdeRistorino;
import ar.edu.ubp.das.saboresdelnorte.services.jaxws.CrearReservaDesdeRistorinoResponse;
import ar.edu.ubp.das.saboresdelnorte.services.jaxws.ActualizarContenidosNoPublicados;
import ar.edu.ubp.das.saboresdelnorte.services.jaxws.ActualizarContenidosNoPublicadosResponse;
import ar.edu.ubp.das.saboresdelnorte.services.jaxws.ObtenerContenidosNoPublicados;
import ar.edu.ubp.das.saboresdelnorte.services.jaxws.ObtenerContenidosNoPublicadosResponse;
import ar.edu.ubp.das.saboresdelnorte.services.jaxws.ObtenerDisponibilidadHorariaZona;
import ar.edu.ubp.das.saboresdelnorte.services.jaxws.ObtenerDisponibilidadHorariaZonaResponse;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class SaboresDelNorteEndpoint {

  private static final String NAMESPACE_URI = "http://services.saboresdelnorte.das.ubp.edu.ar/";
  private SaboresDelNorteWS saboresdelnorteService;

  public SaboresDelNorteEndpoint(SaboresDelNorteWS saboresdelnorteService) {
    this.saboresdelnorteService = saboresdelnorteService;
  }

  // REGISTAR CLICK DE UN CONTENIDO
  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "RegistrarClickContenidoRequest")
  @ResponsePayload
  public RegistrarClickContenidoResponse insertarLocalidad(@RequestPayload RegistrarClickContenido request) {
    String body = request.getBody();
    saboresdelnorteService.registrarClickContenido(body);
    return new RegistrarClickContenidoResponse();
  }

  // OBTENER CONTENIDOS NO PUBLICADOS
  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ObtenerContenidosNoPublicadosRequest")
  @ResponsePayload
  public ObtenerContenidosNoPublicadosResponse obtenerContenidosNoPublicados(
      @RequestPayload ObtenerContenidosNoPublicados request) {
    String responseString = saboresdelnorteService.obtenerContenidosNoPublicados();
    ObtenerContenidosNoPublicadosResponse response = new ObtenerContenidosNoPublicadosResponse();
    response.setSoapStringResponse(responseString);
    return response;
  }

  // CREAR RESERVA EN UNA SUCURSAL
  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CrearReservaDesdeRistorinoRequest")
  @ResponsePayload
  public CrearReservaDesdeRistorinoResponse crearReservaDesdeRistorino(
      @RequestPayload CrearReservaDesdeRistorino request) {
    String bodyString = request.getBody();
    String codReserva = saboresdelnorteService.crearReservaDesdeRistorino(bodyString);
    CrearReservaDesdeRistorinoResponse response = new CrearReservaDesdeRistorinoResponse();
    response.setSoapStringResponse(codReserva);
    return response;
  }

  // ACTUALIZAR LOS CONTENIDOS NO PUBLICADOS A PUBLICADOS
  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ActualizarContenidosNoPublicadosRequest")
  @ResponsePayload
  public ActualizarContenidosNoPublicadosResponse actualizarContenidosNoPublicados(
      @RequestPayload ActualizarContenidosNoPublicados request) {
    String body = request.getBody();
    saboresdelnorteService.ActualizarContenidosNoPublicados(body);
    return new ActualizarContenidosNoPublicadosResponse();
  }

  // OBTENER CONTENIDOS NO PUBLICADOS
  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ObtenerDisponibilidadHorariaZonaRequest")
  @ResponsePayload
  public ObtenerDisponibilidadHorariaZonaResponse obtenerDisponibilidadHorariaZona(
      @RequestPayload ObtenerDisponibilidadHorariaZona request) {
    String body = request.getBody();
    String responseString = saboresdelnorteService.obtenerDisponibilidadHorariaZona(body);
    ObtenerDisponibilidadHorariaZonaResponse response = new ObtenerDisponibilidadHorariaZonaResponse();
    response.setSoapStringResponse(responseString);
    return response;
  }
}