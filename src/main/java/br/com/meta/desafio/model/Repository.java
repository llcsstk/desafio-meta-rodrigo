package br.com.meta.desafio.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString(exclude = "listFiles")
@Entity
@Table(name = "repository")
public class Repository {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    @Column
    private String url;
    @Column
    private String lastCommit;
    @OneToMany(mappedBy = "repository", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FileCache> listFiles = new ArrayList<>();
}
