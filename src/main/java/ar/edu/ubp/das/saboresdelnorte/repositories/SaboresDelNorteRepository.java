package ar.edu.ubp.das.saboresdelnorte.repositories;

import ar.edu.ubp.das.saboresdelnorte.beans.ClienteResponseBean;
import ar.edu.ubp.das.saboresdelnorte.beans.ContenidoNoPublicadoResponseBean;
import ar.edu.ubp.das.saboresdelnorte.beans.ObtenerDisponibilidadHorariaZonaResponseBean;
import ar.edu.ubp.das.saboresdelnorte.components.SimpleJdbcCallFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public class SaboresDelNorteRepository {
  @Autowired
  private SimpleJdbcCallFactory jdbcCallFactory;

  // REGISTRAR CLICKS DE UN CONTENIDO
  public void registrarClickContenido(String codContenidoRestaurante,
      int nroContenido,
      int nroClick,
      String fechaHoraRegistro,
      Integer nroCliente,
      BigDecimal costoClick) {
    MapSqlParameterSource p = new MapSqlParameterSource()
        .addValue("cod_contenido_restaurante", codContenidoRestaurante)
        .addValue("nro_contenido", nroContenido)
        .addValue("nro_click", nroClick)
        .addValue("fecha_hora_registro", fechaHoraRegistro)
        .addValue("nro_cliente", nroCliente)
        .addValue("costo_click", costoClick);
    jdbcCallFactory.executeWithOutputs("sp_insert_click_contenido", "dbo", p);
  }

  // OBTENER CONTENIDOS NO PUBLICADOS
  public List<ContenidoNoPublicadoResponseBean> getContenidosNoPublicados() {
    MapSqlParameterSource p = new MapSqlParameterSource();

    return jdbcCallFactory.executeQuery(
        "sp_get_contenidos_no_publicados",
        "dbo",
        p,
        "contenidos_no_publicados",
        ContenidoNoPublicadoResponseBean.class);
  }

  // ===============================
  // INSERT CLIENTE DESDE RISTORINO
  // ===============================
  public Integer insertarClienteDesdeRistorino(
      String apellido,
      String nombre,
      String correo,
      String telefonos) {

    MapSqlParameterSource p = new MapSqlParameterSource()
        .addValue("apellido", apellido)
        .addValue("nombre", nombre)
        .addValue("correo", correo)
        .addValue("telefonos", telefonos);

    List<ClienteResponseBean> result = jdbcCallFactory.executeQuery(
        "sp_insert_cliente_desde_ristorino",
        "dbo",
        p,
        "cliente",
        ClienteResponseBean.class);

    return result.get(0).getNroCliente();
  }

  // ===============================
  // INSERT RESERVA DESDE RISTORINO
  // ===============================
  public void crearReservaSucursal(
      String codReserva,
      Integer nroCliente,
      LocalDate fechaReserva,
      Integer nroSucursal,
      String codZona,
      LocalTime horaReserva,
      Integer cantAdultos,
      Integer cantMenores,
      Double costoReserva) {

    MapSqlParameterSource p = new MapSqlParameterSource()
        .addValue("cod_reserva", codReserva)
        .addValue("nro_cliente", nroCliente)
        .addValue("fecha_reserva", fechaReserva)
        .addValue("nro_sucursal", nroSucursal)
        .addValue("cod_zona", codZona)
        .addValue("hora_reserva", horaReserva)
        .addValue("cant_adultos", cantAdultos)
        .addValue("cant_menores", cantMenores)
        .addValue("costo_reserva", costoReserva);

    jdbcCallFactory.execute(
        "sp_crear_reserva_sucursal",
        "dbo",
        p);
  }

  // ACTUALIZAR LOS CONTENIDOS NO PUBLICADOS A PUBLICADOS
  public void actualizarContenidoPublicado(String contenidos) {
    MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("json", contenidos);

    jdbcCallFactory.executeWithOutputs(
        "sp_actualizar_contenido_no_publicado",
        "dbo",
        params);
  }

  public List<ObtenerDisponibilidadHorariaZonaResponseBean> obtenerDisponibilidadHorariaZona(
      Integer nroSucursal, String codZona, String fechaReserva) {

    MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("nro_sucursal", nroSucursal)
        .addValue("fecha_reserva", fechaReserva)
        .addValue("cod_zona", codZona);

    return jdbcCallFactory.executeQuery(
        "sp_obtener_disponibilidad_por_zona",
        "dbo",
        params,
        "disponibilidad_horarios",
        ObtenerDisponibilidadHorariaZonaResponseBean.class);
  }

}
