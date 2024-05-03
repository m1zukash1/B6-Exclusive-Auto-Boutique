package com.b6exclusiveautoboutique.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private User owner;
    private String title;
    private String commentBody;
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Comment> replies;
    @ManyToOne
    private Comment parentComment;
    @ManyToOne
    @JoinColumn(name = "product_id")  // make sure this matches the database column
    private Product product;

    @Override
    public String toString() {
        return title + "\n" + commentBody;
    }
}
