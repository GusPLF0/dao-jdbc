package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;

public class Program {
    public static void main(String[] args) {

        Department books = Department
                .builder()
                .id(1)
                .name("Books")
                .build();


        Seller seller = Seller
                .builder()
                .id(21)
                .name("Bob")
                .email("testBob@gmail.com")
                .birthDate(new Date())
                .baseSalary(3000.0)
                .department(books)
                .build();

        SellerDao sellerDao = DaoFactory.createSellerDao();

        Seller seller2 = sellerDao.findById(3);

        System.out.println(seller2);
        System.out.println(seller);
    }
}
