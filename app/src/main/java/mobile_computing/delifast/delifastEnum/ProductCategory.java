package mobile_computing.delifast.delifastEnum;

public enum ProductCategory {

    MEAT("Fleisch"),
    MILKPRODUCT("Milchprodukt"),
    PASTA("Teigwaren"),
    BREAD("Brot"),
    OIL("Öl"),
    FRUIT("Obst"),
    VEGETABLES("Gemüse"),
    DRINK("Getränk"),
    SWEETS("Süßwaren"),
    SEASONING("Gewürz"),
    OTHERS("Sonstige");

    private String productCategory;

    private ProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductCategory() {
        return this.productCategory;
    }
}
