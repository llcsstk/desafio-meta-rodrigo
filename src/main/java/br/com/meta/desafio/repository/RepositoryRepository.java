package br.com.meta.desafio.repository;

import br.com.meta.desafio.model.Repository;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface RepositoryRepository extends CrudRepository<Repository, Long> {
    Repository findFirstByUrl(String url);

    @Transactional
    void deleteByUrl(String url);
}
