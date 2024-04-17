package com.dpend.shopkeeper;

public class Item_returned {
    private int Billno;
    private int Id;
    private int Qty;
    private String Reason;
    private String Name;
    private String Date;

    public Item_returned(){}
    public Item_returned(int billno,int id,int qty,String reason,String name,String date){
        this.Billno = billno;
        this.Id = id;
        this.Qty = qty;
        this.Reason = reason;
        this.Name = name;
        this.Date = date;
    }

    public void Set_Billno(int billno){
        this.Billno = billno;
    }
    public void Set_Id(int id){
        this.Id = id;
    }
    public void Set_Qty(int qty){
        this.Qty = qty;
    }
    public void Set_Reason(String reason){
        this.Reason = reason;
    }
    public void Set_Name(String name){
        this.Name = name;
    }
    public void Set_Date(String date){
        this.Date = date;
    }

    public int Get_Billno(){
        return Billno;
    }
    public int Get_Id(){
        return Id;
    }
    public int Get_Qty(){
        return Qty;
    }
    public String Get_Reason(){
        return Reason;
    }
    public String Get_Name(){
        return Name;
    }
    public String Get_Date(){
        return Date;
    }

}
