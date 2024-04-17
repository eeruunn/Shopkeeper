package com.dpend.shopkeeper;

public class Item_aapbatch {
    private String Batchno;
    private String Expdate;
    private float Pprice;
    private float Rprice;
    private int Stock;
    private int Id;
    private int Qty;

    public Item_aapbatch(){}
    public Item_aapbatch(String batchno,String expdate, float pprice, float rprice, int stock,int id,int qty){
        this.Batchno=batchno;
        this.Expdate=expdate;
        this.Pprice=pprice;
        this.Rprice=rprice;
        this.Stock=stock;
        this.Id=id;
        this.Qty=qty;
    }

    public void Set_Batchno(String mbatchno){
        this.Batchno=mbatchno;
    }
    public void Set_Pprice(float mpprice){
        this.Pprice=mpprice;
    }
    public void Set_Rprice(float mrprice){
        this.Rprice=mrprice;
    }
    public void Set_Stock(int mstock){
        this.Stock=mstock;
    }
    public void Set_Id(int mid){this.Id=mid;}
    public void Set_Expdate(String mexpdate){
        this.Expdate=mexpdate;
    }
    public void Set_Qty(int mqty){
        this.Qty=mqty;
    }

    public String Get_Batchno(){
        return Batchno;
    }
    public String Get_Expdate(){
        return Expdate;
    }
    public float Get_Pprice(){
        return Pprice;
    }
    public float Get_Rprice(){
        return Rprice;
    }
    public int Get_Stock(){
        return Stock;
    }
    public int Get_Id(){return Id;}
    public int Get_Qty(){return Qty;}
}

