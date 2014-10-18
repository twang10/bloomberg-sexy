package models;


@Entity
public class Multiverse extends Model {
    string stockSymbol
    
    public Multiverse(String symbol) {
        this.stockSymbol = symbol;
    }
}