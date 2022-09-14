package br.com.meta.desafio.service;

import br.com.meta.desafio.mapper.FileMapper;
import br.com.meta.desafio.model.FileAttribute;
import br.com.meta.desafio.model.FileCache;
import br.com.meta.desafio.model.Repository;
import br.com.meta.desafio.repository.FileCacheRepository;
import br.com.meta.desafio.repository.RepositoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepositoryService {
    private final RepositoryRepository repository;
    private final FileCacheRepository fileCacheRepository;

    public RepositoryService(RepositoryRepository repository, FileCacheRepository fileCacheRepository) {
        this.repository = repository;
        this.fileCacheRepository = fileCacheRepository;
    }

    public void cacheRepository(String url, String lastCommit, List<FileAttribute> files) {
        repository.deleteByUrl(url);

        Repository repo = new Repository();
        repo.setUrl(url);
        repo.setLastCommit(lastCommit);
        repo = repository.save(repo);

        for (FileCache cache : files.stream()
                .map(FileMapper::toCache)
                .collect(Collectors.toList())) {
            cache.setRepository(repo);
            fileCacheRepository.save(cache);
        }
    }

    public Repository getCachedRepository(String url) {
        return repository.findFirstByUrl(url);
    }
}
