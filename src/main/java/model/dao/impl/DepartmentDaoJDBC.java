package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.nio.file.ProviderNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {
    private Connection conn;

    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Department obj) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("INSERT INTO coursejdbc.department (Name) VALUES ('%s');".formatted(obj.getName()), Statement.RETURN_GENERATED_KEYS);

            int affectedRows = st.executeUpdate();

            if (affectedRows > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    obj.setId(rs.getInt(1));
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Error, No rows Affected");
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Department obj) {


        try (PreparedStatement st = conn.prepareStatement("UPDATE `coursejdbc`.`department` SET `Name` = '%s' WHERE (`Id` = '%d');".formatted(obj.getName(),obj.getId()))) {

            int affectedRows = st.executeUpdate();

            if(affectedRows > 0){
                System.out.println("Affected Rows = " + affectedRows);
            }else {
                throw new ProviderNotFoundException("Affected Rows = 0");
            }


        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("DELETE FROM department WHERE Id = ?");

            st.setInt(1, id);

            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("SELECT * FROM department WHERE department.Id = %d".formatted(id));
            rs = st.executeQuery();

            if (rs.next()) {
                return buildDepartment(rs);
            } else {
                throw new ProviderNotFoundException("Nothing was found");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Department> depList = new ArrayList<>();
        try {
            st = conn.prepareStatement("SELECT * FROM department");
            rs = st.executeQuery();

            while (rs.next()) {
                depList.add(buildDepartment(rs));
            }
            return depList;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private Department buildDepartment(ResultSet rs) throws SQLException {
        return Department
                .builder()
                .name(rs.getString("Name"))
                .id(rs.getInt("Id"))
                .build();
    }
}
