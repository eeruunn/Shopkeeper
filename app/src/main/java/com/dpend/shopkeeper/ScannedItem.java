package com.dpend.shopkeeper;

public class ScannedItem {
    private int ID;
    private int BID;
    private int Qty;
    private int Stock;
    private float Mrp;
    private String Name;
    private String Batchno;


    public ScannedItem(){}
    public ScannedItem(int mID, int mStock, int mQty, float mMrp, String mName,String mBatchno,int mBID){
        this.Mrp=mMrp;
        this.Qty=mQty;
        this.ID=mID;
        this.Name=mName;
        this.Stock=mStock;
        this.Batchno=mBatchno;
        this.BID = mBID;
    }

    public void Set_ID(int id){this.ID=id;}
    public void Set_Qty(int qty){this.Qty=qty;}
    public void Set_Stock(int stock){this.Stock=stock;}
    public void Set_Mrp(float mrp){this.Mrp=mrp;}
    public void Set_Name(String name){this.Name=name;}
    public void Set_Batchno(String batchno){this.Batchno=batchno;}
    public void Set_BID(int bid){this.BID = bid;}

    public int Get_ID(){return ID;}
    public int Get_Qty(){return Qty;}
    public int Get_Stock(){return Stock;}
    public int Get_BID(){return BID;}
    public float Get_Mrp(){return Mrp;}
    public String Get_Name(){return Name;}
    public String Get_Batchno(){return Batchno;}

}
