import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Product {
    //ID Sản phẩm
    private String id;
    //Tên sản phẩm
    private String name;
    //Hạn sử dụng
    private Date expirationDate;
    //Số lượng nhập vào
    private int quantityInStock;
    //Số lượng đã bán
    private int quantitySold;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {

        this.expirationDate = expirationDate;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }
    public boolean isExpired() {
        Date currentDate = new Date();
        return expirationDate.before(currentDate);
    }

    public Product(String id, String name, Date expirationDate, int quantityInStock) {
        this.id = id;
        this.name = name;
        this.expirationDate = expirationDate;
        this.quantityInStock = quantityInStock;
        this.quantitySold = 0;
    }

    public Product() {
    }



}
class ProductSalesComparator implements Comparator<Product> {
    @Override
    public int compare(Product product1, Product product2) {
        return Integer.compare(product2.getQuantitySold(), product1.getQuantitySold());
    }
}
