package com.mobile.base.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public abstract class AbstractService<T, ID, R extends JpaRepository<T, ID>> {

    protected final R repo;

    public AbstractService(R repo) {
        this.repo = repo;
    }

    public R getRepo() {
        return repo;
    }

    @Transactional
    public T save(T entity) {
        return repo.save(entity);
    }

    @Transactional
    public void delete(T entity) {
        repo.delete(entity);
    }

    @Transactional(readOnly = true)
    public List<T> findAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Page<T> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<T> findById(ID id) {
        return repo.findById(id);
    }

    @Transactional
    public List<T> saveAll(List<T> entities) {
        return repo.saveAll(entities);
    }
}
