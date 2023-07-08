package com.digitalmoney.transactionservice.dto;

import com.digitalmoney.transactionservice.exception.InternalServerError;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class PaginaDTO <T> {

    private long cantidad;
    private String siguiente;
    private String anterior;
    private List<T> resultados;
    @JsonIgnore
    private Integer tamanioDePagina;
    @JsonIgnore
    private Integer numeroDePagina;

    public PaginaDTO( Integer numeroDePagina,Integer tamanioDePagina,long cantidad, List<T> resultados ) {
        this.cantidad = cantidad;
        this.resultados = resultados;
        this.tamanioDePagina = tamanioDePagina==null ? 10 : tamanioDePagina;
        this.numeroDePagina = numeroDePagina==null ? 0 : numeroDePagina;
    }


    public void setTamanioDePagina(Integer tamanioDePagina) {
        this.tamanioDePagina = tamanioDePagina==null?10:tamanioDePagina;
    }

    public void setNumeroDePagina(Integer numeroDePagina) {
        this.numeroDePagina = numeroDePagina==null?0:numeroDePagina;
    }

    public void setUrlBase(String requestUrl) {
        if(requestUrl==null) throw new InternalServerError("Error Interno del Servidor: Url base invalida");

        String urlBase =  requestUrl.contains("?")?
                requestUrl.substring(0, requestUrl.indexOf("?")) : requestUrl;

        boolean existePaginaSiguiente = cantidad > ((long)this.numeroDePagina * this.tamanioDePagina + this.tamanioDePagina);
        if (existePaginaSiguiente)
            this.siguiente = urlBase + "?pagina=" + (this.numeroDePagina + 1) + "&tamanio=" + this.tamanioDePagina;
        else
            this.siguiente = "No hay una pagina siguente.";

        boolean existePaginaAnterior = this.numeroDePagina > 0;
        if (existePaginaAnterior)
            this.anterior = urlBase + "?pagina=" + (this.numeroDePagina - 1) + "&tamanio=" + this.tamanioDePagina;
        else
            this.anterior = "No hay una pagina anterior.";
    }
}
