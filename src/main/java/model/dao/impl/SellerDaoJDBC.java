package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller obj) {
        PreparedStatement st = null;

        try{
            st = conn.prepareStatement("INSERT INTO seller\n" +
                    "(Name, Email, BirthDate, BaseSalary, DepartmentId) \n" +
                    "VALUES \n" +
                    "(%s, %s, ?, %d, %d)".formatted(obj.getName(), obj.getEmail()
                    , obj.getBaseSalary(), obj.getDepartment().getId()), Statement.RETURN_GENERATED_KEYS);

            st.setDate(1, new java.sql.Date(obj.getBirthDate()
                    .getTime()));

            int rowsAffected = st.executeUpdate();

            if(rowsAffected > 0){
                ResultSet rs = st.getGeneratedKeys();
                if(rs.next()){
                    obj.setId(rs.getInt(1));
                }
                DB.closeResultSet(rs);
            }else {
                throw new DbException("Error, No rows Affected");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Seller obj) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "ON seller.DepartmentId = department.Id " +
                            "WHERE seller.Id = %d".formatted(id)
            );

            rs = st.executeQuery();
            if (rs.next()) {
                Department department = Department.builder()
                        .id(rs.getInt("DepartmentId"))
                        .name(rs.getString("DepName"))
                        .build();

                Seller obj = Seller.builder()
                        .id(rs.getInt("Id"))
                        .name(rs.getString("Name"))
                        .email(rs.getString("Email"))
                        .baseSalary(rs.getDouble("BaseSalary"))
                        .birthDate(rs.getDate("BirthDate"))
                        .department(department)
                        .build();

                return obj;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName FROM seller INNER JOIN department ON seller.DepartmentId = department.Id ORDER BY Name"
            );

            rs = st.executeQuery();

            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {

                Department dep = map.get(rs.getInt("DepartmentId"));

                if (dep == null) {
                    dep = buildDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }


                Seller obj = buildSeller(rs, dep);

                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName FROM seller INNER JOIN department ON seller.DepartmentId = department.Id WHERE DepartmentId = %d ORDER BY Name"
                            .formatted(department.getId())
            );

            rs = st.executeQuery();

            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {

                Department dep = map.get(rs.getInt("DepartmentId"));

                if (dep == null) {
                    dep = buildDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }


                Seller obj = buildSeller(rs, dep);

                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }


    private Department buildDepartment(ResultSet rs) throws SQLException {
        return Department.builder()
                .id(rs.getInt("DepartmentId"))
                .name(rs.getString("DepName"))
                .build();
    }

    private Seller buildSeller(ResultSet rs, Department dep) throws SQLException {
        return Seller.builder()
                .id(rs.getInt("Id"))
                .name(rs.getString("Name"))
                .email(rs.getString("Email"))
                .baseSalary(rs.getDouble("BaseSalary"))
                .birthDate(rs.getDate("BirthDate"))
                .department(dep)
                .build();

    }
}
