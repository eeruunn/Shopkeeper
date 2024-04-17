package com.dpend.shopkeeper;

public class ProductItem {
    private int Stock;
    private int ID;
    private float Price;
    private String Unit;
    private String Name;
    private String Code;

    public ProductItem(){};
    public ProductItem(int mStock,String mUnit,String code,float mPrice,String mName,int mID){
        this.Price=mPrice;
        this.Unit=mUnit;
        this.Stock=mStock;
        this.Name=mName;
        this.ID=mID;
        this.Code=code;
    }

    public void Set_Stock(int stock){this.Stock=stock;}
    public void Set_ID(int id){this.ID=id;}
    public void Set_Price(float price){this.Price=price;}
    public void Set_Unit(String unit){this.Unit=unit;}
    public void Set_Name(String name){this.Name=name;}
    public void Set_Code(String code){this.Code=code;}

    public int Get_Stock(){return Stock;}
    public int Get_ID(){return ID;}
    public float Get_Price(){return Price;}
    public String Get_Unit(){return Unit;}
    public String Get_Name(){return Name;}
    public String Get_Code(){return Code;}
}
