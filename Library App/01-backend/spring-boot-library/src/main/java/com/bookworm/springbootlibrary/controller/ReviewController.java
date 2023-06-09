package com.bookworm.springbootlibrary.controller;

import com.bookworm.springbootlibrary.requestmodels.ReviewRequest;
import com.bookworm.springbootlibrary.service.ReviewService;
import com.bookworm.springbootlibrary.utils.ExtractJWT;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService){
        this.reviewService = reviewService;
    }

    @GetMapping("/secure/user/book")
    public Boolean reviewBookByUser(@RequestHeader(value="Authorization") String token,
                                    @RequestParam Long bookId) throws Exception{
     String userEmail = ExtractJWT.payloadJWTExtraction(token,"\"sub\"");
     if(userEmail == null){
         throw new Exception("User Email is missing");
     }
     return reviewService.userReviewListed(userEmail,bookId);
    }
 @PostMapping("/secure")
 public void postReview(@RequestHeader(value="Authorization") String token,
                       @RequestBody ReviewRequest reviewRequest) throws Exception{
        String userEmail = ExtractJWT.payloadJWTExtraction(token,"\"sub\"");
        if(userEmail == null){
            throw new Exception("User email is missing");
        }
        reviewService.postReview(userEmail,reviewRequest);
 }
}
