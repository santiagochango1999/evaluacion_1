package com.distribuida.service;

import com.distribuida.db.Book;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

@ApplicationScoped
public class BookServiceImpl implements  IBookService {

    @Inject
    EntityManager entityManager;

    @Override
    public Book buscarId(Integer id) {
        return entityManager.find(Book.class, id);
    }

    @Override
    public List<Book> buscarLibros() {
        return entityManager.createQuery("select b from Book b order by b.id asc", Book.class).getResultList();
    }

    @Override
    public void insertar(Book book) {

        EntityTransaction tr=entityManager.getTransaction();
        tr.begin();
        entityManager.persist(book);
        tr.commit();

    }

    @Override
    public void modificar(Book book) {
        entityManager.merge(book);
    }

    @Override
    public void eliminar(Integer id) {
        Book libro=entityManager.find(Book.class,id);
        entityManager.remove(libro);
    }
}
