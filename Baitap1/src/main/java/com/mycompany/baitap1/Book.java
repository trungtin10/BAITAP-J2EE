/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.baitap1;

import java.util.Scanner;

/**
 *
 * @author tintr
 */
public class Book {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Book{");
        sb.append("id=").append(id);
        sb.append(", tiltle=").append(tiltle);
        sb.append(", author=").append(author);
        sb.append(", price=").append(price);
        sb.append('}');
        return sb.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTiltle() {
        return tiltle;
    }

    public void setTiltle(String tiltle) {
        this.tiltle = tiltle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public Book(int id, String tiltle, String author, long price) {
        this.id = id;
        this.tiltle = tiltle;
        this.author = author;
        this.price = price;
    }
    private int id;
    private String tiltle;
    private String author;
    private long price;
    
public Book() {
    }
    public void input() {
        Scanner x = new Scanner(System.in);
        System.out.print("Nhap ma sach:");
        this.id = Integer.parseInt(x.nextLine());
        System.out.print("Nhap ten sach:");
        this.tiltle = x.nextLine();
        System.out.print("Nhap Tac gia:");
        this.author = x.nextLine();
        System.out.print("Nhap don Gia:");
        this.price = x.nextLong();
}
    public void output(){
        String msg ="""
                    
                BOOK: id= %d, title=%s, author=%s, price=%d""".formatted(id,tiltle,author,price);
        System.out.println(msg);
    }
}
