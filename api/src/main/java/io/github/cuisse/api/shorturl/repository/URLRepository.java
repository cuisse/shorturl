package io.github.cuisse.api.shorturl.repository;

import io.github.cuisse.api.shorturl.model.URL;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface URLRepository extends JpaRepository<URL, String> {

}
