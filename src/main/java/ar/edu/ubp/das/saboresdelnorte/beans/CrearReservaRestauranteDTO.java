package ar.edu.ubp.das.saboresdelnorte.beans;

public class CrearReservaRestauranteDTO {

  private String codReserva;
  private Integer nroCliente;
  private String fechaReserva;
  private Integer nroRestaurante;
  private Integer nroSucursal;
  private String codZona;
  private String horaReserva;
  private Integer cantAdultos;
  private Integer cantMenores;
  private Double costoReserva;

  public CrearReservaRestauranteDTO() {
  }

  public String getCodReserva() {
    return codReserva;
  }

  public void setCodReserva(String codReserva) {
    this.codReserva = codReserva;
  }

  public Integer getNroCliente() {
    return nroCliente;
  }

  public void setNroCliente(Integer nroCliente) {
    this.nroCliente = nroCliente;
  }

  public String getFechaReserva() {
    return fechaReserva;
  }

  public void setFechaReserva(String fechaReserva) {
    this.fechaReserva = fechaReserva;
  }

  public Integer getNroRestaurante() {
    return nroRestaurante;
  }

  public void setNroRestaurante(Integer nroRestaurante) {
    this.nroRestaurante = nroRestaurante;
  }

  public Integer getNroSucursal() {
    return nroSucursal;
  }

  public void setNroSucursal(Integer nroSucursal) {
    this.nroSucursal = nroSucursal;
  }

  public String getCodZona() {
    return codZona;
  }

  public void setCodZona(String codZona) {
    this.codZona = codZona;
  }

  public String getHoraReserva() {
    return horaReserva;
  }

  public void setHoraReserva(String horaReserva) {
    this.horaReserva = horaReserva;
  }

  public Integer getCantAdultos() {
    return cantAdultos;
  }

  public void setCantAdultos(Integer cantAdultos) {
    this.cantAdultos = cantAdultos;
  }

  public Integer getCantMenores() {
    return cantMenores;
  }

  public void setCantMenores(Integer cantMenores) {
    this.cantMenores = cantMenores;
  }

  public Double getCostoReserva() {
    return costoReserva;
  }

  public void setCostoReserva(Double costoReserva) {
    this.costoReserva = costoReserva;
  }
}
