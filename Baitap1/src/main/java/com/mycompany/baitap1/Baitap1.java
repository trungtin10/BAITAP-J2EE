package com.mycompany.baitap1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Comparator; // Bắt buộc có để so sánh giá (Case 6)

public class Baitap1 {

    public static void main(String[] args) {
        List<Book> listBook = new ArrayList<>();
        Scanner x = new Scanner(System.in);
        
        String msg = """
            ------------------------------------
            CHUONG TRINH QUAN LY SACH
                1. Them 1 cuon sach
                2. Xoa 1 cuon sach
                3. Thay doi thong tin sach
                4. Xuat thong tin
                5. Tim sach Lap trinh
                6. Lay sach gia cao nhat
                7. Tim kiem theo tac gia
            0. Thoat
            ------------------------------------
            Chon chuc nang: """;

        int chon = 0;
        do {
            System.out.print(msg);
            chon = x.nextInt();
            x.nextLine(); // QUAN TRỌNG: Xóa bộ nhớ đệm để tránh trôi lệnh

            switch (chon) {
                // --- CASE 1: THEM SACH ---
                case 1 -> {
                    System.out.println("--- THEM SACH MOI ---");
                    Book newBook = new Book();
                    newBook.input(); 
                    listBook.add(newBook);
                    System.out.println("Da them sach thanh cong!");
                }

                // --- CASE 2: XOA SACH ---
                case 2 -> {
                    System.out.println("--- XOA SACH ---");
                    System.out.print("Nhap ma sach can xoa: ");
                    int bookid = x.nextInt();
                    x.nextLine(); 
                    
                    Book find = listBook.stream()
                            .filter(p -> p.getId() == bookid)
                            .findFirst()
                            .orElse(null);

                    if (find != null) {
                        listBook.remove(find);
                        System.out.println("Da xoa sach ID " + bookid + " thanh cong.");
                    } else {
                        System.out.println("Loi: Khong tim thay sach co ma: " + bookid);
                    }
                }

                // --- CASE 3: SUA SACH ---
                case 3 -> {
                    System.out.println("--- THAY DOI THONG TIN ---");
                    System.out.print("Nhap ma sach can sua: ");
                    int bookid = x.nextInt();
                    x.nextLine(); 

                    Book find = listBook.stream()
                            .filter(p -> p.getId() == bookid)
                            .findFirst()
                            .orElse(null);

                    if (find != null) {
                        System.out.println("Tim thay sach! Vui long nhap thong tin moi:");
                        find.input(); // Nhập đè thông tin cũ
                        System.out.println("Da cap nhat thong tin sach.");
                    } else {
                        System.out.println("Loi: Khong tim thay sach co ma: " + bookid);
                    }
                }

                // --- CASE 4: XUAT DANH SACH ---
                case 4 -> {
                    System.out.println("\n--- DANH SACH SACH ---");
                    if (listBook.isEmpty()) {
                        System.out.println("Danh sach dang trong!");
                    } else {
                        listBook.forEach(Book::output);
                    }
                }

                // --- CASE 5: TIM SACH LAP TRINH ---
                case 5 -> {
                    System.out.println("\n--- TIM SACH LAP TRINH ---");
                    // Lưu ý: getTiltle() là tên hàm trong class Book của bạn (nếu bạn đã sửa thành getTitle thì sửa lại ở đây nhé)
                    List<Book> list5 = listBook.stream()
                            .filter(u -> u.getTiltle().toLowerCase().contains("lap trinh"))
                            .toList();

                    if (list5.isEmpty()) {
                        System.out.println("Khong tim thay quyen sach 'Lap trinh' nao.");
                    } else {
                        list5.forEach(Book::output);
                    }
                }

                // --- CASE 6: LAY SACH GIA CAO NHAT (MỚI) ---
                case 6 -> {
                    System.out.println("\n--- SACH GIA CAO NHAT ---");
                    if (listBook.isEmpty()) {
                        System.out.println("Danh sach trong!");
                    } else {
                        // So sánh dựa trên getPrice()
                        Book maxBook = listBook.stream()
                                .max(Comparator.comparingDouble(Book::getPrice)) 
                                .orElse(null);
                        
                        if (maxBook != null) {
                            maxBook.output();
                        }
                    }
                }

                // --- CASE 7: TIM KIEM THEO TAC GIA (MỚI) ---
                case 7 -> {
                    System.out.println("\n--- TIM KIEM THEO TAC GIA ---");
                    System.out.print("Nhap ten tac gia can tim: ");
                    String authorKey = x.nextLine().toLowerCase();

                    List<Book> listAuthor = listBook.stream()
                            .filter(b -> b.getAuthor().toLowerCase().contains(authorKey))
                            .toList();

                    if (listAuthor.isEmpty()) {
                        System.out.println("Khong tim thay sach cua tac gia: " + authorKey);
                    } else {
                        System.out.println("Tim thay " + listAuthor.size() + " cuon sach:");
                        listAuthor.forEach(Book::output);
                    }
                }

                case 0 -> System.out.println("Da thoat chuong trinh.");
                default -> System.out.println("Chuc nang khong hop le!");
            }
        } while (chon != 0);
    }
}