package com.dpend.shopkeeper;

public class Receipt_Item {
    private int Qty;
    private float Mrp;
    private float Amount;
    private String Name;

    public Receipt_Item(){};
    public Receipt_Item(int mQty,float mAmount,float mMrp,String mName){
        this.Qty=mQty;
        this.Mrp=mMrp;
        this.Amount=mAmount;
        this.Name=mName;

    }

    public void Set_Qty(int qty){this.Qty=qty;}
    public void Set_Mrp(float mrp){this.Mrp=mrp;}
    public void Set_Amount(float amount){this.Amount=amount;}
    public void Set_Name(String name){this.Name=name;}

    public int Get_Qty(){return Qty;}
    public float Get_Mrp(){return Mrp;}
    public float Get_Amount(){return Amount;}
    public String Get_Name(){return Name;}

}
