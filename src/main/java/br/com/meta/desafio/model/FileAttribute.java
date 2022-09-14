package br.com.meta.desafio.model;

import lombok.Data;

@Data
public class FileAttribute {
    private String extension;
    private int lines;
    private long size;
}
