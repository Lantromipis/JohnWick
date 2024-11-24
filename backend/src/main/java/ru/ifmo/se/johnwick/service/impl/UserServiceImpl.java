package ru.ifmo.se.johnwick.service.impl;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.RSQLParserException;
import cz.jirutka.rsql.parser.UnknownOperatorException;
import cz.jirutka.rsql.parser.ast.Node;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import ru.ifmo.se.johnwick.exception.EntityNotFoundByIdException;
import ru.ifmo.se.johnwick.exception.UnsupportedRsqlOperatorException;
import ru.ifmo.se.johnwick.exception.ValidationException;
import ru.ifmo.se.johnwick.mapper.UserMapper;
import ru.ifmo.se.johnwick.model.dto.UserDto;
import ru.ifmo.se.johnwick.model.entity.UserEntity;
import ru.ifmo.se.johnwick.repository.UserRepository;
import ru.ifmo.se.johnwick.rsql.JpaRsqlVisitorParams;
import ru.ifmo.se.johnwick.rsql.visitor.UserEntityJpaRsqlVisitor;
import ru.ifmo.se.johnwick.service.api.UserService;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UserServiceImpl implements UserService {

    @Inject
    UserMapper userMapper;

    @Inject
    UserRepository userRepository;

    @Override
    public List<UserDto> listUsers(String rsqlPredicate) {
        EntityManager em = userRepository.getEntityManager();
        Predicate predicate = null;

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<UserEntity> query = criteriaBuilder.createQuery(UserEntity.class);
        Root<UserEntity> root = query.from(UserEntity.class);
        CriteriaQuery<UserEntity> select = query.select(root);

        if (rsqlPredicate != null && !rsqlPredicate.isEmpty()) {
            JpaRsqlVisitorParams<UserEntity> jpaRsqlVisitorParams = new JpaRsqlVisitorParams<>(
                    criteriaBuilder,
                    query,
                    root,
                    select
            );

            RSQLParser rsqlParser = new RSQLParser();
            Node rootRsqlNode;
            try {
                rootRsqlNode = rsqlParser.parse(rsqlPredicate);
            } catch (RSQLParserException e) {
                Throwable cause = e.getCause();
                if (cause instanceof UnknownOperatorException unknownOperatorException) {
                    throw new UnsupportedRsqlOperatorException(unknownOperatorException.getOperator());
                }
                throw e;
            }
            predicate = rootRsqlNode.accept(new UserEntityJpaRsqlVisitor(), jpaRsqlVisitorParams);
        }

        if (predicate != null) {
            select = select.where(predicate);
        }

        select.orderBy(criteriaBuilder.desc(root.get("username")));

        TypedQuery<UserEntity> typedQuery = em.createQuery(select);

        typedQuery.setMaxResults(1000);
        typedQuery.setFirstResult(0);
        List<UserEntity> entities = typedQuery.getResultList();

        return userMapper.toDto(entities);
    }

    @Override
    public UserDto getUserById(UUID id) {
        UserEntity entity = userRepository.findById(id);
        if (entity == null) {
            throw new EntityNotFoundByIdException("User", id.toString());
        }
        return userMapper.toDto(entity);
    }

    @Override
    public UserDto getUserByUsername(String username) {
        UserEntity entity = userRepository.findByUsername(username);
        if (entity == null) {
            throw new EntityNotFoundByIdException("User", username);
        }
        return userMapper.toDto(entity);
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto user) {
        UserEntity userEntity = userMapper.fromDtoWithPassword(user);
        UserEntity existingUser = userRepository.findByUsername(userEntity.getUsername());
        if (existingUser != null) {
            throw new ValidationException("User with provided username already exists");
        }
        userEntity.setId(null);
        userRepository.persistAndFlush(userEntity);
        return userMapper.toDto(userEntity);
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto user) {
        UserEntity existingUser = userRepository.findById(user.getId());
        if (existingUser == null) {
            throw new EntityNotFoundByIdException("User", user.getId().toString());
        }

        UserEntity newUserEntity = userMapper.fromDto(user);
        if (newUserEntity.getPassword() != null) {
            existingUser.setPassword(newUserEntity.getPassword());
        }
        if (newUserEntity.getDisplayName() != null) {
            existingUser.setDisplayName(newUserEntity.getDisplayName());
        }
        if (newUserEntity.getUsername() != null) {
            existingUser.setUsername(newUserEntity.getUsername());
        }

        userRepository.persistAndFlush(existingUser);
        return userMapper.toDto(existingUser);
    }
}
