
package View;

import javax.swing.ImageIcon;

/**
 *
 * @author nttma
 */
public class DrinkItem {
    private String name;
    private double price;
    private ImageIcon image;

    public DrinkItem(String name, double price, ImageIcon image) {
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public ImageIcon getImage() {
        return image;
    }
    
}
