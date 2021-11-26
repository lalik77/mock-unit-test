package com.rest.app;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value ="/book")
public class BookController {

    private BookRepository bookRepository;

    @Autowired
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public List<Book> getAllBookRecords(){

        List<Book> allRecords = bookRepository.findAll();

        return allRecords;
    }


    @GetMapping(value = "{bookId}")
    public Book getBookById(@PathVariable(value = "bookId") Long bookId) {

        return bookRepository.findById(bookId).get();


    }

    @PostMapping
    public Book createBookRecord(@RequestBody @Valid Book bookRecord) {
        return bookRepository.save(bookRecord);
    }


    @PutMapping
    public Book updateBookRecord(@RequestBody @Valid Book bookRecord) throws NotFoundException {

       /* if(bookRecord == null || bookRecord.getBookId() == null) {}

        throw new NotFoundException("BookRecord or id must not be null");*/

        Optional<Book> optionalBook = bookRepository.findById(bookRecord.getBookId());

        if(!optionalBook.isPresent()) {

            throw new NotFoundException("BookRecord or id must not be null");

        }

        Book existingBookRecord = optionalBook.get();
        existingBookRecord.setName(bookRecord.getName());
        existingBookRecord.setSummary(bookRecord.getSummary());
        existingBookRecord.setRating(bookRecord.getRating());

        return bookRepository.save(existingBookRecord);

    }


//TODO write /delete endpoint using tdd

    @DeleteMapping(value = "{bookId}")
    public void deleteBookRecord(@PathVariable(value = "bookId") Long bookId) throws Exception  {

       /* if (!bookRepository.findById(bookId).isPresent()) {
            throw new NotFoundException("book id" + bookId + "not present");
        }

        bookRepository.deleteById(bookId);*/
    }



}
