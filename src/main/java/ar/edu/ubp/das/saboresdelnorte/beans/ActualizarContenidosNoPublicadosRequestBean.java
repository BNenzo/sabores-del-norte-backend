package ar.edu.ubp.das.saboresdelnorte.beans;

import java.util.List;

public class ActualizarContenidosNoPublicadosRequestBean {
  List<ContenidoNoPublicadoResponseBean> contenidos;

  public List<ContenidoNoPublicadoResponseBean> getContenidos() {
    return contenidos;
  }

  public void setContenidos(List<ContenidoNoPublicadoResponseBean> contenidos) {
    this.contenidos = contenidos;
  }
}
