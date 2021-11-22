package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.AbstractNamedEntity;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.validation.constraints.NotNull;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    @NotNull
    private final JdbcTemplate jdbcTemplate;

    @NotNull
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @NotNull
    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(@NotNull JdbcTemplate jdbcTemplate, @NotNull NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(@NotNull User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        int userId;
        String sql = """
                UPDATE users SET name=:name, email=:email, password=:password,
                registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """;
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            userId = newKey.intValue();
        } else if (namedParameterJdbcTemplate.update(sql, parameterSource) != 0) {
            deleteUserRole(user.getId());
            userId = user.getId();
        } else {
            return null;
        }

        List<Role> roles = user.getRoles().stream().toList();
        saveUserRole(roles, userId);
        user = get(userId);
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        String sql = "SELECT * FROM users u LEFT OUTER JOIN user_roles r ON u.id = r.user_id WHERE id=?";
        List<User> users = jdbcTemplate.query(sql, getExtractor(), id);
        return DataAccessUtils.singleResult(users);
    }

    private ResultSetExtractor<List<User>> getExtractor() {
        return rs -> {
            Map<Integer, User> map = new HashMap<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                Role role = Role.valueOf(rs.getString("role"));

                if (map.containsKey(id)) {
                    User user = map.get(id);
                    Set<Role> roles = user.getRoles();
                    roles.add(role);
                } else {
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    String password = rs.getString("password");
                    Date registered = rs.getDate("registered");
                    boolean enabled = rs.getBoolean("enabled");
                    int calories = rs.getInt("calories_per_day");

                    map.put(id, new User(
                            id,
                            name,
                            email,
                            password,
                            calories,
                            enabled,
                            registered,
                            new ArrayList<>(Collections.singleton(role))
                    ));
                }
            }

            return map.values().stream()
                    .sorted(Comparator.comparing((Function<User, String>) AbstractNamedEntity::getName).thenComparing(User::getEmail))
                    .collect(Collectors.toList());
        };
    }

    @Override
    public User getByEmail(@NotNull String email) {
        String sql = "SELECT * FROM users u LEFT OUTER JOIN user_roles r ON u.id = r.user_id WHERE email=?";
        List<User> users = jdbcTemplate.query(sql, getExtractor(), email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM users u LEFT OUTER JOIN user_roles r ON u.id = r.user_id ORDER BY name, email";
        return jdbcTemplate.query(sql, getExtractor());
    }

    public void saveUserRole(List<Role> userRoles, int userId) {
        String sql = "INSERT INTO user_roles (user_id, role) VALUES (?,?)";
        this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, userId);
                Role userRole = userRoles.get(i);
                ps.setString(2, userRole.name());
            }

            @Override
            public int getBatchSize() {
                return userRoles.size();
            }
        });
    }

    public void deleteUserRole(int userId) {
        String sql = "DELETE FROM user_roles WHERE user_id=?";
        jdbcTemplate.update(sql, userId);
    }
}
