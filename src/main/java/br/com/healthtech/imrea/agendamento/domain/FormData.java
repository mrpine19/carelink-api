package br.com.healthtech.imrea.agendamento.domain;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

public class FormData {
    @RestForm("planilha")
    public FileUpload fileUpload;
}
