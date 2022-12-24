package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SellerDaoJDBC implements SellerDao {

    private Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller obj) {

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

        try{
            st = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "ON seller.DepartmentId = department.Id " +
                            "WHERE seller.Id = %d".formatted(id)
            );

            rs = st.executeQuery();
            if (rs.next()){
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
        return null;
    }
}
