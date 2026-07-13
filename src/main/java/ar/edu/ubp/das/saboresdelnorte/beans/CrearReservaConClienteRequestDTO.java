package ar.edu.ubp.das.saboresdelnorte.beans;

public class CrearReservaConClienteRequestDTO {

  private CrearReservaClienteDTO cliente;
  private CrearReservaRestauranteDTO reserva;

  public CrearReservaConClienteRequestDTO() {
  }

  public CrearReservaClienteDTO getCliente() {
    return cliente;
  }

  public void setCliente(CrearReservaClienteDTO cliente) {
    this.cliente = cliente;
  }

  public CrearReservaRestauranteDTO getReserva() {
    return reserva;
  }

  public void setReserva(CrearReservaRestauranteDTO reserva) {
    this.reserva = reserva;
  }
}
