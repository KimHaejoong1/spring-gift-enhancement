package gift.domain;

public class Wish {
    private int count;
    private Product product;

    public Wish(int count, Product product) {
        this.count = count;
        this.product = product;
    }

    public int getCount() {
        return count;
    }

    public Product getProduct() {
        return product;
    }
}
