package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Arrays;
import java.util.List;

public class Program {
    public static void main(String[] args) {

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("=== Test1 ===");
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);

        System.out.println("=== Test2 ===");
        Department department = Department.builder().id(2).name(null).build();
        List<Seller> list = sellerDao.findByDepartment(department);

        System.out.println(Arrays.toString(list.toArray()));


        System.out.println("=== Test3 ===");
        list = sellerDao.findAll();

        System.out.println(Arrays.toString(list.toArray()) + "\n");
    }
}
