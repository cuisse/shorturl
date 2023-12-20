package io.github.cuisse.api.shorturl.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "urls")
public class URL {
    @Id
    @Column(unique = true, length = 8)
    private String hash;
    private String url;
    private Date   timestamp;
}
