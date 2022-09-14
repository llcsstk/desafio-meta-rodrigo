package br.com.meta.desafio.viewModel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileInfo {
    private String extension;
    private int count;
    private int lines;
    private Double percentInRepo;
    private String size;
}
