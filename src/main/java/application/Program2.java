package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.util.Arrays;

public class Program2 {
    public static void main(String[] args) {
        DepartmentDao depDao = DaoFactory.createDepDao();

        System.out.println("======= TESTE 1 =======");
        System.out.println(depDao.findById(1));
        System.out.println("\n");
        System.out.println("======= TESTE 2 =======");
        System.out.println(Arrays.toString(depDao.findAll().toArray()));
        System.out.println("\n");
        System.out.println("======= TESTE 3 =======");
        Department dep = Department.builder().name("Food").id(null).build();
        depDao.insert(dep);
    }
}
