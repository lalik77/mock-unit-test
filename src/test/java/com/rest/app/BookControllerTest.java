package com.rest.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
class BookControllerTest {


    private  MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private static BookController bookController;

    Book RECORD_1 = new Book(1L,"abc","about abc",5);
    Book RECORD_2 = new Book(2L,"cde","about cde",4);
    Book RECORD_3 = new Book(3L,"fgh","about fgh",6);

    @BeforeEach
    public  void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    void getAllBookRecords_success() throws Exception {
        List<Book> records = new ArrayList<>(Arrays.asList(RECORD_1,RECORD_2,RECORD_3));

        when(bookRepository.findAll()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/book")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[2].name",is("fgh")));
    }

    @Test
    void getBookRecordById_success () throws Exception {
        List<Book> records = new ArrayList<>(Arrays.asList(RECORD_1,RECORD_2,RECORD_3));
        when(bookRepository.findById(records.stream()
                .findFirst()
                .get()
                .getBookId())
        )
                .thenReturn(
                Optional.of(RECORD_1));


        mockMvc.perform(MockMvcRequestBuilders
                .get("/book/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.name",is("abc")));

    }


     @Test
    void createRecord_success() throws Exception{
        Book record = Book.builder()
                .bookId(4L)
                .name("Introduction to C")
                .summary("About book")
                .rating(6)
                .build();

        when(bookRepository.save(record)).thenReturn(record);

        String content = objectWriter.writeValueAsString(record);

         MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/book")
                 .contentType(MediaType.APPLICATION_JSON)
                 .accept(MediaType.APPLICATION_JSON)
                 .content(content);

         mockMvc.perform(mockRequest)
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$",notNullValue()))
                 .andExpect(jsonPath("$.name",is("Introduction to C")));




     }

     @Test
    public void updateRecord_success() throws Exception{
        Book updareRecord = Book.builder()
                .bookId(1L)
                .name("Updated book Name")
                .summary("Updated summary")
                .rating(5)
                .build();

        when(bookRepository.findById(RECORD_1.getBookId())).thenReturn(Optional.ofNullable(RECORD_1));
        when(bookRepository.save(updareRecord)).thenReturn(updareRecord);


         String updatedContent = objectWriter.writeValueAsString(updareRecord);

         MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/book")
                 .contentType(MediaType.APPLICATION_JSON)
                 .accept(MediaType.APPLICATION_JSON)
                 .content(updatedContent);

         mockMvc.perform(mockRequest)
                 .andExpect(jsonPath("$",notNullValue()))
                 .andExpect(jsonPath("$.name",is("Updated book Name")));


     }

     @Test
    public void deleteRecord_success() throws Exception {

        when(bookRepository.findById(RECORD_2.getBookId())).thenReturn(Optional.of(RECORD_2));

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/book/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

     }



}