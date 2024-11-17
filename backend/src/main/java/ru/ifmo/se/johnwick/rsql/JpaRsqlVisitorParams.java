package ru.ifmo.se.johnwick.rsql;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JpaRsqlVisitorParams<T> {
    CriteriaBuilder criteriaBuilder;
    CriteriaQuery<T> query;
    Root<T> root;
    CriteriaQuery<T> select;
}
