package com.dpend.shopkeeper;

public class Sales_Item {
    private String Date;
    private String Time;
    private String Price;
    private String Items;
    private int Bill_NO;

    public Sales_Item(){};
    public Sales_Item(String mdate,String mtime,String mPrice,String mitems,int mbill_no){
        this.Price=mPrice;
        this.Time=mtime;
        this.Items=mitems;
        this.Date=mdate;
        this.Bill_NO=mbill_no;
    }

    public void Set_Date(String date){this.Date=date;}
    public void Set_Time(String time){this.Time=time;}
    public void Set_Price(String price){this.Price=price;}
    public void Set_Items(String items){this.Items=items;}
    public void Set_Billno(int billno){this.Bill_NO=billno;}

    public String Get_Date(){return Date;}
    public String Get_Time(){return Time;}
    public String Get_Price(){return Price;}
    public String Get_Items(){return Items;}
    public int Get_Billno(){return Bill_NO;}
}
