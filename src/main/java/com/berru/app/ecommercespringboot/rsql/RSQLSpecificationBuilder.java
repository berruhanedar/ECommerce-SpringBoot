package com.berru.app.ecommercespringboot.rsql;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.data.jpa.domain.Specification;

public class RSQLSpecificationBuilder<T> {

    public Specification<T> createSpecification(String query) {
        Node rootNode = new RSQLParser().parse(query);
        return rootNode.accept(new RSQLCustomVisitor<T>());
    }
}