
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ProductManage {
    public static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    public static SimpleDateFormat formatterInput = new SimpleDateFormat("yyyy-MM-dd");
    private static ArrayList<Product> listProduct = hashCodeProduct();
    private static ArrayList<Product> teamProduct = new ArrayList<Product>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int n;
        do {
            n = menu(sc);
            productManagement(n, sc);

        } while (!"0".equalsIgnoreCase(String.valueOf(n)));

        sc.close();

    }

    public static ArrayList<Product> hashCodeProduct() {
        ArrayList<Product> productInfoList = new ArrayList<>();
        productInfoList.add(new Product("1", "Product 1", new Date(), 100));
        productInfoList.add(new Product("2", "Product 2", new Date(), 200));
        productInfoList.add(new Product("3", "Product 3", new Date(), 150));
        productInfoList.add(new Product("4", "Product 4", new Date(), 100));
        productInfoList.add(new Product("5", "Product 5", new Date(), 200));
        productInfoList.add(new Product("6", "Product 6", new Date(), 150));
        return productInfoList;
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
                    productManage.inputInfo(sc);
                    productManage.printProduct(listProduct);
                    break;
                case 2:
                    //listProduct=productManage.hashCodeProduct(listProduct);

                    productManage.printProduct(listProduct);
                    break;
                case 3:
                    productManage.upDateProductById(sc);
                    break;
                case 4:
                    productManage.deleteIsExpired(sc);
                    break;
                case 5:
                    productManage.SortProductDesc();
                    break;
                case 6:
                    productManage.printProduct(searchProductByName(sc));
                    ;
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
            product.setQuantitySold(quantitySold);
            System.out.println("Update success product");
            printProduct(product);


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
        for (Product pr : listProduct) {
            if (pr.getName().contains(name)) {
                list.add(pr);

            }
        }

        return list;
    }

    public void deleteIsExpired(Scanner sc) {
        ArrayList<Product> productsToRemove = new ArrayList<>();
        if (listProduct.size() > 0) {
            for (Product pr : listProduct) {
                if (pr.isExpired()) {
                    productsToRemove.add(pr);
                }

            }
            listProduct.removeAll(productsToRemove);
            System.out.println("Delete success");
        } else {
            System.out.println("Product does not exist");

        }
    }

    //Sort
    public void SortProductDesc() {
        List<Product> list = listProduct;
        Collections.sort(list, new ProductSalesComparator());
        System.out.println(
                "=======Sort the product list by Quantity Sold descending======");
        System.out.println();
        System.out.printf("%-7S | %-15s | %-7s |%-15s | \n", "ID", "NAME", "EXPIRATION_DATE", "QUANTITY_SOLD");
        for (Product product : list) {
            System.out.printf("%-7s | %-15s | %-7s      |%-15d | \n", product.getId(), product.getName(),
                    formatter.format(product.getExpirationDate()), product.getQuantitySold());
        }
        System.out.println();
        System.out.println(
                "===========================END===============================");
    }

    public void inputInfo(Scanner scanner) {
        Product product = inputProduct(scanner);
        listProduct.add(0, product);
    }


    //Print product
    public void printProduct(ArrayList<Product> list) {
        if (list.size() > 0) {
            System.out.println(
                    "=====================================LIST PRODUCT==========================");
            System.out.println();
            System.out.printf("%-7S | %-15s | %-7s |%-7s |%-7s | \n", "ID", "NAME", "EXPIRATION_DATE", "quantityInStock", "quantitySold");
            for (Product product : list) {
                System.out.printf("%-7s | %-15s | %-7s      |%-7d         |%-7d      | \n", product.getId(), product.getName(),
                        formatter.format(product.getExpirationDate()), product.getQuantityInStock(), product.getQuantitySold());
            }
            System.out.println();
            System.out.println(
                    "==================================END=====================================");
        } else {
            System.out.println(
                    "=====================================LIST PRODUCT==========================");
            System.out.println("Product does not exist");
            System.out.println(
                    "==================================END=====================================");
        }
    }

    public boolean isDuplicateId(String id) {
        for (Product product : listProduct) {
            if (product.getId().equals(id)) {
                return true; // ID bị trùng
            }
        }
        return false; // ID không bị trùng
    }

    public Product inputProduct(Scanner scanner) {
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
                    "===================================GET PRODUCT BY ID========================");
            System.out.println();
            System.out.printf("%-7S | %-15s | %-7s |%-7s |%-7s | \n", "ID", "NAME", "EXPIRATION_DATE", "quantityInStock", "quantitySold");

            System.out.printf("%-7s | %-15s | %-7s      |%-7d         |%-7d      | \n", product.getId(), product.getName(),
                    formatter.format(product.getExpirationDate()), product.getQuantityInStock(), product.getQuantitySold());

            System.out.println();
            System.out.println(
                    "==================================END======================================");

        } else {
            System.out.println(
                    "===================================GET PRODUCT BY ID========================");
            System.out.println("Product does not exist");
            System.out.println();
            System.out.println(
                    "==================================END======================================");
        }

    }

    public void deleteProductByID(Scanner sc) {
        System.out.print("Input ID: ");
        String id = sc.nextLine();
        Product product = getProductById(id);
        if (product != null) {
            System.out.println(
                    "===================================CHECK EXISTENCE PRODUCT========================");
            printProduct(product);

            System.out.println();
            listProduct.remove(product);
            System.out.println("Product ID=" + product.getId() + " Delete success");
            System.out.println(
                    "==================================END======================================");

        } else {
            System.out.println(
                    "===================================DELETE PRODUCT BY I========================");
            System.out.println("Product does not exist");
            System.out.println("Product Delete failed");
            System.out.println();
            System.out.println(
                    "==================================END======================================");
        }

    }

    public Product getProductById(String id) {
        for (Product product : listProduct) {
            if (product.getId().equals(id)) {
                return product; // Trả về sản phẩm có ID tương ứng
            }
        }
        return null; // Không tìm thấy sản phẩm với ID đã cho
    }

    public int menuDeleteList(Scanner sc) {
        int n;
        do {
            System.out.println("-------------------------------------------Delete list Product------------------------------------------");
            System.out.println();
            System.out.println("            0. Exit");
            System.out.println("            1. Add Product to list delete");
            System.out.println("            2. Delete");
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
        } while (!(n >= 1 && n <= 2));
        return n;
    }

    public void deleteListProduct(Scanner sc) {
       // printProduct(listProduct);
        int key = menuDeleteList(sc);
        switch (key) {
            case 1:
                System.out.print("Add Product to list: ");
                System.out.print("Input ID: ");
                String id = sc.nextLine();
                Product product = getProductById(id);
                if (product != null) {
                    teamProduct.add(product);
                    printProduct(teamProduct);
                } else {
                    System.out.println("Product does not exist");

                }

                break;
            case 2:
                System.out.print("Delete list Product: ");
                if (teamProduct.size() > 0) {
                    listProduct.removeAll(teamProduct);
                    System.out.println("Delete success");
                } else {
                    System.out.println("Delete failed");
                }
                break;

            default:
                System.out.println("Wrong choice");
                break;
        }
    }


}
