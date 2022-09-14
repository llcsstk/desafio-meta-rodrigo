package br.com.meta.desafio.viewModel;

import lombok.Data;

import java.util.List;

@Data
public class RepositoryInfo {
    private RepositoryResume resume;
    private List<FileInfo> fileInfos;
}
