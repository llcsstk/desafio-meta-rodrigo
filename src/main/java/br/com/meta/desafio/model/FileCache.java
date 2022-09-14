package br.com.meta.desafio.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@ToString(exclude = "repository")
@Entity
@Table(name = "filecache")
public class FileCache {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Repository repository;
    @Column
    private String extension;
    @Column
    private int lines;
    @Column
    private long size;
}
