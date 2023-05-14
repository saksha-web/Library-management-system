package com.bookworm.springbootlibrary.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.bookworm.springbootlibrary.entity.Book;
import com.bookworm.springbootlibrary.responsemodels.ShelfCurrentLoansResponse;
import com.bookworm.springbootlibrary.service.BookService;
import com.bookworm.springbootlibrary.utils.ExtractJWT;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import com.razorpay.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/secure/currentloans")
    public List<ShelfCurrentLoansResponse> currentLoans(@RequestHeader(value = "Authorization") String token)
            throws Exception
    {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        return bookService.currentLoans(userEmail);
    }

    @GetMapping("/secure/currentloans/count")
    public int currentLoansCount(@RequestHeader(value = "Authorization") String token) {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        return bookService.currentLoansCount(userEmail);
    }

    @GetMapping("/secure/ischeckedout/byuser")
    public Boolean checkoutBookByUser(@RequestHeader(value = "Authorization") String token,
                                      @RequestParam Long bookId) {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        return bookService.checkoutBookByUser(userEmail, bookId);
    }

    @PutMapping("/secure/checkout")
    public Book checkoutBook (@RequestHeader(value = "Authorization") String token,
                              @RequestParam Long bookId) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        return bookService.checkoutBook(userEmail, bookId);
    }

    @PutMapping("/secure/return")
    public void returnBook(@RequestHeader(value = "Authorization") String token,
                           @RequestParam Long bookId) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        bookService.returnBook(userEmail, bookId);
    }
    @PutMapping("/secure/renew/loan")
    public void renewLoan(@RequestHeader(value="Authorization") String token,
                          @RequestParam Long bookId) throws Exception{
        String userEmail = ExtractJWT.payloadJWTExtraction(token,"\"sub\"");
        bookService.renewLoan(userEmail, bookId);
    }
    //creating order for payment

    @PostMapping("/secure/create_order")
    @ResponseBody
    public String createOrder(@RequestBody Map<String, Object> data) throws Exception {
        int amt = Integer.parseInt(data.get("amount").toString());
        var client =new RazorpayClient ("rzp_test_DhdWTndep3kw6K","VW4s04fqX95f6drctSkIG4Y4");

        JSONObject ob = new JSONObject();
        ob.put("amount", amt*100);
        ob.put("currency","INR");
        ob.put("receipt","txn_235425");

        //creating new order

     Order order = client.Orders.create(ob);
        System.out.println(order);
        return "done";
    }

}




