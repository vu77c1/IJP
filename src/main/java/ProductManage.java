
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ProductManage {
    public static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    public static SimpleDateFormat formatterInput = new SimpleDateFormat("yyyy-MM-dd");
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int n;
        do {
            n = menu(sc);
            productManagement(n, sc);

        } while (!"0".equalsIgnoreCase(String.valueOf(n)));

        sc.close();
    }


    public static ArrayList<Product> getProduct() {
        ArrayList<Product> productInfoList = new ArrayList<>();
        try {
            JDBCQueryExecutor.openConnection();
            String sql = "select * from Products";
            ResultSet rs = JDBCQueryExecutor.executeSelectQuery(sql);
            if (rs != null) {
                try {
                    while (rs.next()) {
                        productInfoList.add(new Product(
                                rs.getString("id"),
                                rs.getString("name"),
                                rs.getDate("expirationDate"),
                                rs.getInt("quantityInStock"),
                                rs.getInt("quantitySold")
                        ));
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex2) {
            ex2.printStackTrace();

        } finally {
            JDBCQueryExecutor.closeConnection();

        }
        return productInfoList;
    }

    public static ArrayList<Product> getProductSql(String sql) {
        ArrayList<Product> productInfoList = new ArrayList<>();
        JDBCQueryExecutor.openConnection();
        ResultSet rs = JDBCQueryExecutor.executeSelectQuery(sql);
        if (rs != null) {
            try {
                while (rs.next()) {
                    productInfoList.add(new Product(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getDate("expirationDate"),
                            rs.getInt("quantityInStock"),
                            rs.getInt("quantitySold")
                    ));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        JDBCQueryExecutor.closeConnection();
        return productInfoList;

    }

    public static void addProduct(Scanner sc) {
        Product product = inputProduct(sc);
        JDBCQueryExecutor.openConnection();
        String sql = "insert into Products (id, name, expirationDate, quantityInStock, quantitySold)\n" +
                "values (?,?,?,?,?)";
        Object[] prams = {product.getId(),
                product.getName(),
                product.getExpirationDate(),
                product.getQuantityInStock(),
                0};
        int rs = JDBCQueryExecutor.executeUpdateQuery(sql, prams);
        if (rs > 0) {
            System.out.println("Add success!!");
        } else {
            System.out.println("Add failed!!");
        }
        JDBCQueryExecutor.closeConnection();

    }

    public static int menu(Scanner sc) {
        int n;

        do {
            System.out.println("----------------DRUG STORE MANAGEMENT----------------");
            System.out.println();

            System.out.println();
            System.out.println("            0. Exit");
            System.out.println("            1. Add Product");
            System.out.println("            2. List Product");
            System.out.println("            3. Update Product");
            System.out.println("            4. Delete expired products");
            System.out.println("            5. Sort the product list by Quantity Sold descending");
            System.out.println("            6. Search Product By Name");
            System.out.println("            7. Get Product By ID");
            System.out.println("            8. Delete Product By ID");
            System.out.println("            9. Delete list Product");
            System.out.println();
            System.out.println("            What do you want to choose?");


            int m = -1;

            do {
                try {
                    System.out.print("Please choose....");
                    m = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid integer.");
                }
            } while (m == -1);

            n = m;


        } while (!(n >= 0 && n <= 9));
        return n;
    }

    public static int productManagement(int n, Scanner sc) {
        ProductManage productManage = new ProductManage();
        try {

            switch (n) {
                case 1:
                    addProduct(sc);
                    break;
                case 2:
                    productManage.printProduct(getProduct());
                    break;
                case 3:
                    productManage.upDateProductById(sc);
                    break;
                case 4:
                    productManage.deleteIsExpired();
                    break;
                case 5:
                    productManage.SortProductDesc();
                    break;
                case 6:
                    productManage.printProduct(searchProductByName(sc));
                    break;
                case 7:
                    productManage.printGetProductById(sc);
                    break;
                case 8:
                    productManage.deleteProductByID(sc);

                    break;
                case 9:
                    productManage.deleteListProduct(sc);
                    break;
                case 0:
                    System.out.println("Close program.....");
                    break;

            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        return n;

    }

    public void upDateProductById(Scanner sc) {
        System.out.print("Input ID: ");
        String Id = sc.nextLine();
        Product product = getProductById(Id);
        if (product != null) {
            int quantitySold;

            do {
                System.out.print("Input QuantitySold: ");
                quantitySold = Integer.parseInt(sc.nextLine());
                if (product.getQuantityInStock() < quantitySold) {
                    System.out.println("Invalid input:QuantitySold > quantityInStock!. Please enter input!!");
                }

            } while (product.getQuantityInStock() < quantitySold);

            try {
                JDBCQueryExecutor.openConnection();
                String sql = "update Products set quantitySold =? where id=?";
                Object[] prams = {quantitySold, product.getId()};
                int rs = JDBCQueryExecutor.executeUpdateQuery(sql, prams);
                if (rs > 0) {
                    printProduct(getProductById(product.getId()));
                    System.out.println("Update success");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                JDBCQueryExecutor.closeConnection();
            }

        } else {
            System.out.println("Product does not exist");
        }

    }

    private static void printProduct(Product product) {
        System.out.println();
        System.out.printf("%-7S | %-15s | %-7s |%-7s |%-7s | \n", "ID", "NAME", "EXPIRATION_DATE", "quantityInStock", "quantitySold");

        System.out.printf("%-7s | %-15s | %-7s      |%-7d         |%-7d      | \n", product.getId(), product.getName(),
                formatter.format(product.getExpirationDate()), product.getQuantityInStock(), product.getQuantitySold());
    }

    public static ArrayList<Product> searchProductByName(Scanner sc) {

        String name;
        System.out.print("Input Name: ");
        name = sc.nextLine();
        ArrayList<Product> list = new ArrayList<>();
        for (Product pr : getProduct()) {
            if (pr.getName().contains(name)) {
                list.add(pr);

            }
        }

        return list;
    }

    public void deleteIsExpired() {
        ArrayList<Product> listProduct = getProductSql("select * from products where expirationDate< GETDATE()");
        if (listProduct.size() > 0) {
            try {
                JDBCQueryExecutor.openConnection();
                String sql = "delete from  products where expirationDate< GETDATE()";
                int rs = JDBCQueryExecutor.executeUpdateQuery(sql);
                if (rs > 0) {
                    printProduct(listProduct);
                    System.out.println("Delete success");

                } else {
                    System.out.println("Product does not exist");

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                JDBCQueryExecutor.closeConnection();
            }


        } else {
            System.out.println("Product does not exist");

        }
    }

    //Sort
    public void SortProductDesc() {
        printProduct(getProductSql("select * from Products order by quantitySold desc"));
    }

    //Print product
    public void printProduct(ArrayList<Product> list) {
        if (list.size() > 0) {
            System.out.println(
                    "============================================================================");
            System.out.println("LIST.....");
            System.out.printf("%-7S | %-15s | %-7s |%-7s |%-7s | \n", "ID", "NAME", "EXPIRATION_DATE", "quantityInStock".toUpperCase(), "quantitySold".toUpperCase());
            for (Product product : list) {
                System.out.printf("%-7s | %-15s | %-7s      |%-7d         |%-7d      | \n", product.getId(), product.getName(),
                        formatter.format(product.getExpirationDate()), product.getQuantityInStock(), product.getQuantitySold());
            }
            System.out.println();
            System.out.println(
                    "============================================================================");
        } else {
            System.out.println(
                    "============================================================================");
            System.out.println("Product does not exist");
            System.out.println(
                    "============================================================================");
        }
    }

    public static boolean isDuplicateId(String id) {
        for (Product product : getProduct()) {
            if (product.getId().equals(id)) {
                return true; // ID bị trùng
            }
        }
        return false; // ID không bị trùng
    }

    public static Product inputProduct(Scanner scanner) {
        String id;
        do {
            System.out.print("id: ");
            id = scanner.nextLine();
            if (isDuplicateId(id)) {
                System.out.println("Invalid Duplicate ID. Please try again.");
            }

        } while (isDuplicateId(id));


        System.out.print("name: ");
        String name = scanner.nextLine();

        Date dateTime = null;
        do {
            System.out.print("expirationDate (DD/mm/yyyy): ");
            String expirationDate = scanner.nextLine();
            try {
                dateTime = (Date) formatter.parse(expirationDate);
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please try again.");
            }

        } while (dateTime == null);


        int quantityInStock = -1;

        do {
            try {
                System.out.print("quantityInStock: ");
                quantityInStock = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        } while (quantityInStock == -1);


        Product pr = new Product();
        pr.setId(id);
        pr.setName(name);
        pr.setExpirationDate(dateTime);
        pr.setQuantityInStock(quantityInStock);
        return pr;

    }

    // Get Product by id
    public void printGetProductById(Scanner sc) {
        System.out.print("Input ID: ");
        String id = sc.nextLine();
        Product product = getProductById(id);
        if (product != null) {
            System.out.println(
                    "============================================================================");
            System.out.println();
            System.out.printf("%-7S | %-15s | %-7s |%-7s |%-7s | \n", "ID", "NAME", "EXPIRATION_DATE", "quantityInStock", "quantitySold");

            System.out.printf("%-7s | %-15s | %-7s      |%-7d         |%-7d      | \n", product.getId(), product.getName(),
                    formatter.format(product.getExpirationDate()), product.getQuantityInStock(), product.getQuantitySold());

            System.out.println();
            System.out.println(
                    "============================================================================");

        } else {
            System.out.println(
                    "============================================================================");
            System.out.println("Product does not exist");
            System.out.println();
            System.out.println(
                    "============================================================================");
        }

    }

    public void deleteProductByID(Scanner sc) {
        System.out.print("Input ID: ");
        String id = sc.nextLine();
        Product product = getProductById(id);
        if (product != null) {
            System.out.println(
                    "============================================================================");
            printProduct(product);

            System.out.println();
            //listProduct.remove(product);
            JDBCQueryExecutor.openConnection();
            try {
                String sql = "delete from Products where id=?";
                Object[] prams = {product.getId()};
                int rs = JDBCQueryExecutor.executeUpdateQuery(sql, prams);
                if (rs > 0) {
                    System.out.println("Product ID=" + product.getId() + " Delete success");
                    System.out.println(
                            "============================================================================");

                } else {
                    System.out.println("Product Delete failed");
                }

            } catch (Exception ex) {
                ex.printStackTrace();

            } finally {
                JDBCQueryExecutor.closeConnection();

            }


        } else {
            System.out.println(
                    "============================================================================");
            System.out.println("Product does not exist");
            System.out.println();
            System.out.println(
                    "============================================================================");
        }

    }

    public Product getProductById(String id) {
        for (Product product : getProduct()) {
            if (product.getId().equals(id)) {
                return product; // Trả về sản phẩm có ID tương ứng
            }
        }
        return null; // Không tìm thấy sản phẩm với ID đã cho
    }

    public void deleteListProduct(Scanner sc) {
        ArrayList<Product> temp = new ArrayList<>();
        String id = null;
        do {
            System.out.println("Add product to list or Press [delete] to delete");
            System.out.print("Input ID: ");
            id = sc.nextLine();
            Product product = getProductById(id);
            if (product!=null&&!id.equals("delete"))
            {
                temp.add(product);
            }
            else {
                System.out.println("product not exist");
            }
        }
        while (!id.equals("delete"));

        if (temp.size()>0)
        {
            printProduct(temp);
            try
            {
                JDBCQueryExecutor.openConnection();
                for (int i = 0; i < temp.size(); i++) {
                    String sql="delete from Products where id=?";
                    int row=JDBCQueryExecutor.executeUpdateQuery(sql,temp.get(i).getId());
                }


            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            finally {
                System.out.println("Delete success");
                JDBCQueryExecutor.closeConnection();
            }
        }
    }
}
