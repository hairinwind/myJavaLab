package my.javalab.spring.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerDaoImpl implements CustomerDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Customer> getCustomers() {
		return jdbcTemplate.query("select * from customers",
				new RowMapper<Customer>() {
					public Customer mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						return new Customer(rs.getLong("id"), rs
								.getString("first_name"), rs
								.getString("last_name"));
					}
				});
	}
}
